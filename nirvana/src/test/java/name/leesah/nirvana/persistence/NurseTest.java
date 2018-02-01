package name.leesah.nirvana.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.ArrayMap;

import com.google.common.collect.Sets;
import com.google.gson.Gson;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Set;

import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.reminder.Reminder;
import name.leesah.nirvana.model.reminder.ReminderMaker;
import name.leesah.nirvana.ui.reminder.AlarmSecretary;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.google.common.collect.Sets.union;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toSet;
import static name.leesah.nirvana.LanternGenie.everythingVanishes;
import static name.leesah.nirvana.LanternGenie.hire;
import static name.leesah.nirvana.LanternGenie.randomDay;
import static name.leesah.nirvana.LanternGenie.randomMedication;
import static name.leesah.nirvana.LanternGenie.randomReminder;
import static name.leesah.nirvana.LanternGenie.randomReminders;
import static name.leesah.nirvana.LanternGenie.randomRemindersAnHourAgo;
import static name.leesah.nirvana.LanternGenie.randomRemindersAnHourLater;
import static name.leesah.nirvana.PhoneBook.nurse;
import static name.leesah.nirvana.persistence.Nurse.PREFERENCE_KEY_REMINDERS;
import static name.leesah.nirvana.persistence.Nurse.isFor;
import static name.leesah.nirvana.utils.AdaptedGsonFactory.getGson;
import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static name.leesah.nirvana.utils.IdentityHelper.uniqueInt;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.joda.time.LocalTime.now;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;
import static org.robolectric.RuntimeEnvironment.application;

/**
 * Created by sah on 2017-04-06.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = {24, 25, 26})
public class NurseTest {

    private Gson gson = getGson();
    private SharedPreferences preferences;
    private Context context;
    @Mock
    private ReminderMaker reminderMaker;
    @Mock
    private AlarmSecretary alarmSecretary;
    private Nurse nurse;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        context = application.getApplicationContext();
        preferences = getDefaultSharedPreferences(context);
        nurse = new Nurse(context);
    }

    @After
    public void tearDown() throws Exception {
        everythingVanishes(context);
    }

    @Test
    public void add1RemindersWhen0Existing() throws Exception {
        Reminder brandNew = randomReminder(context, false);

        nurse.add(brandNew);

        assertThat(allPersisted(), equalTo(singleton(brandNew)));
    }

    @Test
    public void add0ReminderWhen0Existing() throws Exception {
        nurse.add(emptySet());
        assertThat(preferences.getStringSet(PREFERENCE_KEY_REMINDERS, null).size(), is(0));
    }

    @Test
    public void addXRemindersWhenYExisting() throws Exception {
        Set<Reminder> existing = randomReminders(context, true);
        Set<Reminder> brandNew = randomReminders(context, false);

        nurse.add(brandNew);

        assertThat(allPersisted(), containsInAnyOrder(union(existing, brandNew).toArray()));
    }

    @Test
    public void add0ReminderWhenYExisting() throws Exception {
        Set<Reminder> existing = randomReminders(context, true);

        nurse.add(emptySet());

        assertThat(allPersisted(), containsInAnyOrder(existing.toArray()));
    }

    @Test
    @Ignore("Unstable")
    public void replace() throws Exception {
        Set<Reminder> existing = randomReminders(context, true);
        Set<Reminder> existingWithOddMedicationIds = existing.stream()
                .filter(reminder -> reminder.getMedicationId() % 2 != 0)
                .collect(toSet());
        Set<Reminder> brandNew = singleton(new Reminder(randomDay(), now(), uniqueInt() * 2 + 1, 1));

        nurse.replace(reminder -> reminder.getMedicationId() % 2 == 0, brandNew);

        assertThat(allPersisted(), containsInAnyOrder(union(existingWithOddMedicationIds, brandNew)));
    }

    public Set<Reminder> allPersisted() {
        return preferences.getStringSet(PREFERENCE_KEY_REMINDERS, null).stream()
                .map(json -> gson.fromJson(json, Reminder.class))
                .collect(toSet());
    }

    @Test
    public void setNotified() throws Exception {
        Reminder oneReminder = randomReminders(context, true).stream().findAny().get();
        int notificationId = uniqueInt();

        nurse.setNotified(oneReminder.getId(), notificationId);

        Reminder saved = preferences.getStringSet(PREFERENCE_KEY_REMINDERS, null).stream()
                .map(json -> gson.fromJson(json, Reminder.class))
                .filter(reminder -> reminder.equals(oneReminder))
                .findFirst().get();
        assertThat(saved.getState(), equalTo(Reminder.State.NOTIFIED));
        assertThat(saved.getNotificationId(), equalTo(notificationId));
    }

    @Test
    public void setDone() throws Exception {
        Reminder oneReminder = randomReminders(context, true).stream().findAny().get();

        nurse.setDone(oneReminder.getId());

        Reminder saved = preferences.getStringSet(PREFERENCE_KEY_REMINDERS, null).stream()
                .map(json -> gson.fromJson(json, Reminder.class))
                .filter(reminder -> reminder.equals(oneReminder))
                .findFirst().get();
        assertThat(saved.getState(), equalTo(Reminder.State.DONE));
    }

    @Test
    public void getReminders() throws Exception {
        LocalDate date = new ArrayList<>(
                randomReminders(context, true)
        ).get(0).getDate();

        Set<Reminder> reminders = nurse.getReminders(date);

        reminders.forEach(reminder -> assertThat(reminder.getDate(), equalTo(date)));
    }

    @Test
    public void getExistingReminder() throws Exception {
        int reminderId = randomReminder(context, true).getId();
        assertThat(nurse.getReminder(reminderId), is(notNullValue()));
    }

    @Test
    public void getInexistingReminder() throws Exception {
        int reminderId = randomReminder(context, false).getId();
        assertThat(nurse.getReminder(reminderId), is(nullValue()));
    }

    @Test
    public void newRemindersAreHandedToNurse() throws Exception {
        hire(reminderMaker);
        hire(alarmSecretary);

        Set<Reminder> existing = randomReminders(context, true, today());
        Set<Reminder> brandNew = randomReminders(context, false, today());

        when(reminderMaker.createReminders(any(LocalDate.class))).thenReturn(union(existing, brandNew));

        nurse.scheduleForTheRestOfToday();

        assertThat(nurse.getReminders(today()), containsInAnyOrder(union(existing, brandNew).toArray()));

    }

    @Test
    public void setReminderAlarms() throws Exception {
        hire(reminderMaker);
        hire(alarmSecretary);

        Set<Reminder> hourAgo = randomRemindersAnHourAgo(context, true);
        Set<Reminder> hourLater = randomRemindersAnHourLater(context, true);

        when(reminderMaker.createReminders(any(LocalDate.class))).thenReturn(union(hourAgo, hourLater));

        nurse.scheduleForTheRestOfToday();

        hourLater.forEach(reminder -> verify(alarmSecretary).setAlarm(eq(reminder)));

        verifyNoMoreInteractions(alarmSecretary);

    }

    @Test
    public void arrayMapHasValue() throws Exception {
        Reminder origin = randomReminder(context, false);
        ArrayMap<Integer, Reminder> map = new ArrayMap<>();
        map.put(origin.getId(), origin);

        Reminder copy = new Reminder(origin);
        assertThat(copy, equalTo(origin));

        assertThat(map.containsValue(copy), is(true));
    }

    @Test
    public void reminderForMedication() throws Exception {
        Medication medication = randomMedication(context, true);
        Reminder reminder = mock(Reminder.class);
        when(reminder.getMedicationId()).thenReturn(medication.getId());

        assertThat(isFor(medication).test(reminder), is(true));
    }
}