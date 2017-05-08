package name.leesah.nirvana.ui.reminder;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Set;

import name.leesah.nirvana.BuildConfig;
import name.leesah.nirvana.LanternGenie;
import name.leesah.nirvana.model.reminder.Reminder;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static name.leesah.nirvana.LanternGenie.hire;
import static name.leesah.nirvana.LanternGenie.randomMedications;
import static name.leesah.nirvana.PhoneBook.nurse;
import static name.leesah.nirvana.PhoneBook.reminderMaker;
import static name.leesah.nirvana.ui.reminder.RemindingService.ACTION_CONFIRM_REMINDER;
import static name.leesah.nirvana.ui.reminder.RemindingService.ACTION_SHOW_REMINDER;
import static name.leesah.nirvana.ui.reminder.RemindingService.ACTION_SNOOZE_REMINDER;
import static name.leesah.nirvana.ui.reminder.RemindingService.EXTRA_REMINDER_ID;
import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.RuntimeEnvironment.application;

/**
 * Created by sah on 2017-04-07.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = {24, 25})
public class RemindingServiceTest {

    private Context context;
    private Set<Reminder> reminders;
    @Mock
    private NotificationSecretary notificationSecretary;
    @Mock
    private AlarmSecretary alarmSecretary;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        context = application.getApplicationContext();

        hire(notificationSecretary);
        hire(alarmSecretary);

        randomMedications(
                context, true);
        reminders = reminderMaker(context).createReminders(today());
        nurse(context).add(reminders);
    }

    @After
    public void tearDown() throws Exception {
        LanternGenie.everythingVanishes(application.getApplicationContext());
    }

    @Test
    public void showReminders() throws Exception {
        reminders.forEach(reminder -> {
            show(reminder);
            verify(notificationSecretary).display(notifIdEquals(reminder), any(Notification.class));
        });
        verifyNoMoreInteractions(notificationSecretary);
        verifyNoMoreInteractions(alarmSecretary);
    }

    @Test
    public void snoozeReminders() throws Exception {
        reminders.forEach(reminder -> {
            show(reminder);
            reset(notificationSecretary);
            reset(alarmSecretary);

            int notifId = nurse(context).getReminder(reminder.getId()).getNotificationId();
            snooze(reminder);
            verify(notificationSecretary).dismiss(eq(notifId));
            verify(alarmSecretary).setAlarm(medIdEquals(reminder));
        });
        verifyNoMoreInteractions(notificationSecretary);
        verifyNoMoreInteractions(alarmSecretary);
    }

    @Test
    public void confirmReminders() throws Exception {
        reminders.forEach(reminder -> {
            show(reminder);
            reset(notificationSecretary);

            confirm(reminder);
            verify(notificationSecretary).dismiss(notifIdEquals(reminder));
        });
        verifyNoMoreInteractions(notificationSecretary);
        verifyNoMoreInteractions(alarmSecretary);
    }

    @Test
    public void showReminderDetails() {
        reminders.forEach(reminder -> {
            show(reminder);
            reset(notificationSecretary);

            showDetails(reminder);
        });
        verifyNoMoreInteractions(notificationSecretary);
        verifyNoMoreInteractions(alarmSecretary);
    }

    private void show(Reminder reminder) {
        Intent intent = new Intent(context, RemindingService.class)
                .setAction(ACTION_SHOW_REMINDER)
                .putExtra(EXTRA_REMINDER_ID, reminder.getId());
        new RemindingServiceWrapper(context).startWithIntent(intent);
    }

    private void snooze(Reminder reminder) {
        Intent intent = new Intent(context, SchedulingService.class)
                .setAction(ACTION_SNOOZE_REMINDER)
                .putExtra(EXTRA_REMINDER_ID, reminder.getId());
        new RemindingServiceWrapper(context).startWithIntent(intent);
    }

    private void confirm(Reminder reminder) {
        Intent intent = new Intent(context, SchedulingService.class)
                .setAction(ACTION_CONFIRM_REMINDER)
                .putExtra(EXTRA_REMINDER_ID, reminder.getId());
        new RemindingServiceWrapper(context).startWithIntent(intent);
    }

    private void showDetails(Reminder reminder) {
        Intent intent = new Intent(context, ReminderDetailsActivity.class)
                .setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK)
                .putExtra(EXTRA_REMINDER_ID, reminder.getId());
    }

    private int notifIdEquals(Reminder expected) {
        Reminder reminder = nurse(context).getReminder(expected.getId());
        return eq(reminder.getNotificationId());
    }

    private Reminder medIdEquals(Reminder expected) {
        return argThat(actual -> expected.getMedicationId() == actual.getMedicationId());
    }

    private class RemindingServiceWrapper extends RemindingService {
        public RemindingServiceWrapper(Context context) {
            super();
            attachBaseContext(context);
        }

        private void startWithIntent(Intent intent) {
            onHandleIntent(intent);
        }
    }
}