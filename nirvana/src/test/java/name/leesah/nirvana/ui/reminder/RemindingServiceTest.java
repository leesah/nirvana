package name.leesah.nirvana.ui.reminder;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import name.leesah.nirvana.model.reminder.Reminder;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static name.leesah.nirvana.LanternGenie.everythingVanishes;
import static name.leesah.nirvana.LanternGenie.hire;
import static name.leesah.nirvana.LanternGenie.randomReminder;
import static name.leesah.nirvana.PhoneBook.nurse;
import static name.leesah.nirvana.ui.reminder.RemindingService.ACTION_CONFIRM_REMINDER;
import static name.leesah.nirvana.ui.reminder.RemindingService.ACTION_SHOW_REMINDER;
import static name.leesah.nirvana.ui.reminder.RemindingService.EXTRA_REMINDER_ID;
import static name.leesah.nirvana.ui.reminder.RemindingService.NOTIFICATION_TAG;
import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.RuntimeEnvironment.application;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by sah on 2017-04-07.
 */
@RunWith(RobolectricTestRunner.class)
//TODO: @Config(manifest = Config.NONE, sdk = {24, 25, 26})
public class RemindingServiceTest {

    private Context context;
    private Reminder reminder;
    // TODO: mock nurse
    @Mock
    private AlarmSecretary alarmSecretary;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        context = application.getApplicationContext();
        hire(alarmSecretary);

        reminder = randomReminder(context, true, today());
    }

    @After
    public void tearDown() throws Exception {
        everythingVanishes(context);
    }

    @Test
    public void showReminders() throws Exception {
        show(reminder);
        int notificationId = nurse(context).getReminder(reminder.getId()).getNotificationId();

        assertThat(shadowOf(context.getSystemService(NotificationManager.class)).getNotification(NOTIFICATION_TAG, notificationId), is(notNullValue()));
        verifyNoMoreInteractions(alarmSecretary);
    }

    @Test
    public void confirmReminders() throws Exception {
        show(reminder);
        int notificationId = nurse(context).getReminder(reminder.getId()).getNotificationId();

        confirm(reminder);

        assertThat(shadowOf(context.getSystemService(NotificationManager.class)).getNotification(NOTIFICATION_TAG, notificationId), is(nullValue()));
        verifyNoMoreInteractions(alarmSecretary);
    }

    @Test
    public void showReminderDetails() {
        show(reminder);

        showDetails(reminder);

        // TODO: verify activity started
        verifyNoMoreInteractions(alarmSecretary);
    }

    private void show(Reminder reminder) {
        Intent intent = new Intent(context, RemindingService.class)
                .setAction(ACTION_SHOW_REMINDER)
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