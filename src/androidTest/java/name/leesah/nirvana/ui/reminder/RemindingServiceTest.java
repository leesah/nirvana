package name.leesah.nirvana.ui.reminder;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import org.joda.time.LocalTime;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.Set;

import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.medication.MedicationBuilder;
import name.leesah.nirvana.model.medication.reminding.AtCertainHours;
import name.leesah.nirvana.model.medication.repeating.Everyday;
import name.leesah.nirvana.model.reminder.Reminder;
import name.leesah.nirvana.model.reminder.ReminderFactory;
import name.leesah.nirvana.model.reminder.TimedDosage;
import name.leesah.nirvana.data.Nurse;
import name.leesah.nirvana.data.Pharmacist;
import name.leesah.nirvana.data.Therapist;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.core.deps.guava.collect.Sets.newHashSet;
import static java.util.Collections.singletonList;
import static name.leesah.nirvana.model.medication.DosageForm.TABLET;
import static name.leesah.nirvana.ui.reminder.RemindingService.ACTION_CONFIRM_REMINDER;
import static name.leesah.nirvana.ui.reminder.RemindingService.ACTION_SHOW_REMINDER;
import static name.leesah.nirvana.ui.reminder.RemindingService.ACTION_SNOOZE_REMINDER;
import static name.leesah.nirvana.ui.reminder.RemindingService.EXTRA_REMINDER_ID;
import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Created by sah on 2017-04-07.
 */
public class RemindingServiceTest {
    private static final LocalTime NINE_AM = new LocalTime(0).withHourOfDay(9);
    private static final LocalTime NINE_PM = new LocalTime(0).withHourOfDay(21);
    private static final Medication FOLACIN = new MedicationBuilder().
            setName("Folacin").
            setManufacturer("folsyra").
            setForm(TABLET).
            setRepeatingModel(new Everyday()).
            setRemindingModel(new AtCertainHours(singletonList(new TimedDosage(NINE_AM, 1)))).
            build();
    private static final Medication VALACICLOVIR = new MedicationBuilder().
            setName("Valaciclovir").
            setManufacturer("Teva").
            setForm(TABLET).
            setRepeatingModel(new Everyday()).
            setRemindingModel(new AtCertainHours(Arrays.asList(new TimedDosage(NINE_AM, 1), new TimedDosage(NINE_PM, 1)))).
            build();

    private RemindingServiceWrapper service;
    private Context context;
    private Pharmacist pharmacist;
    private Nurse nurse;
    private Set<Reminder> reminders;
    private NotificationSecretary notificationSecretary;
    private AlarmSecretary alarmSecretry;

    @Before
    public void setUp() throws Exception {
        context = getTargetContext();
        pharmacist = Pharmacist.getInstance(context);
        nurse = Nurse.getInstance(context);

        notificationSecretary = mock(NotificationSecretary.class);
        NotificationSecretary.setInstance(notificationSecretary);

        alarmSecretry = mock(AlarmSecretary.class);
        AlarmSecretary.setInstance(alarmSecretry);

        newHashSet(VALACICLOVIR, FOLACIN).forEach(m -> pharmacist.addMedication(m));
        reminders = new ReminderFactory(context).createReminders(today());
        nurse.add(reminders);
    }

    @After
    public void tearDown() throws Exception {
        resetAll();
    }

    @BeforeClass
    public static void resetAll() {
        Pharmacist.reset();
        Nurse.reset();
        Therapist.reset();
        NotificationSecretary.setInstance(null);
        AlarmSecretary.setInstance(null);
        PreferenceManager.getDefaultSharedPreferences(getTargetContext()).edit().clear().apply();
    }

    @Test
    public void showReminders() throws Exception {
        reminders.forEach(reminder -> {
            show(reminder);
            verify(notificationSecretary).display(notifIdEquals(reminder), any(Notification.class));
        });
        verifyNoMoreInteractions(notificationSecretary);
        verifyNoMoreInteractions(alarmSecretry);
    }

    @Test
    public void snoozeReminders() throws Exception {
        reminders.forEach(reminder -> {
            show(reminder);
            reset(notificationSecretary);
            reset(alarmSecretry);

            int notifId = nurse.getReminder(reminder.getId()).getNotificationId();
            snooze(reminder);
            verify(notificationSecretary).dismiss(eq(notifId));
            verify(alarmSecretry).setAlarm(medIdEquals(reminder));
        });
        verifyNoMoreInteractions(notificationSecretary);
        verifyNoMoreInteractions(alarmSecretry);
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
        verifyNoMoreInteractions(alarmSecretry);
    }

    @Test
    public void showReminderDetails() {
        reminders.forEach(reminder -> {
            show(reminder);
            reset(notificationSecretary);

            showDetails(reminder);
        });
        verifyNoMoreInteractions(notificationSecretary);
        verifyNoMoreInteractions(alarmSecretry);
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


    private void assertSecretaryCalled(Reminder reminder) {

    }

    private int notifIdEquals(Reminder expected) {
        Reminder reminder = nurse.getReminder(expected.getId());
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