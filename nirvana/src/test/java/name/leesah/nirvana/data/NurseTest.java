package name.leesah.nirvana.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.common.collect.Sets;
import com.google.gson.Gson;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Set;

import name.leesah.nirvana.BuildConfig;
import name.leesah.nirvana.model.reminder.Reminder;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toSet;
import static name.leesah.nirvana.LanternGenie.everythingVanishes;
import static name.leesah.nirvana.LanternGenie.randomDay;
import static name.leesah.nirvana.LanternGenie.randomReminders;
import static name.leesah.nirvana.PhoneBook.nurse;
import static name.leesah.nirvana.data.Nurse.PREFERENCE_KEY_REMINDERS;
import static name.leesah.nirvana.utils.AdaptedGsonFactory.getGson;
import static name.leesah.nirvana.utils.IdentityHelper.uniqueInt;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.joda.time.LocalTime.now;
import static org.junit.Assert.assertThat;
import static org.robolectric.RuntimeEnvironment.application;

/**
 * Created by sah on 2017-04-06.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = {24, 25})
public class NurseTest {

    private Gson gson = getGson();
    private SharedPreferences preferences;
    private Context context;

    @Before
    public void setUp() throws Exception {
        context = application.getApplicationContext();
        preferences = getDefaultSharedPreferences(context);
    }

    @After
    public void tearDown() throws Exception {
        everythingVanishes(context);
    }

    @Test
    public void addXRemindersWhen0Existing() throws Exception {
        Set<Reminder> newReminders = randomReminders(context, false);

        nurse(context).add(newReminders);

        assertThat(
                preferences.getStringSet(PREFERENCE_KEY_REMINDERS, null).stream()
                        .map(json -> gson.fromJson(json, Reminder.class))
                        .collect(toSet()),
                containsInAnyOrder(newReminders.toArray(new Reminder[0])));
    }

    @Test
    public void add0ReminderWhen0Existing() throws Exception {
        nurse(context).add(emptySet());
        assertThat(preferences.getStringSet(PREFERENCE_KEY_REMINDERS, null).size(), is(0));
    }

    @Test
    public void addXRemindersWhenYExisting() throws Exception {
        Set<Reminder> existingReminders = randomReminders(context, true);
        Set<Reminder> newReminders = randomReminders(context, false);

        nurse(context).add(newReminders);

        assertThat(
                preferences.getStringSet(PREFERENCE_KEY_REMINDERS, null).stream()
                        .map(json -> gson.fromJson(json, Reminder.class))
                        .collect(toSet()),
                containsInAnyOrder(Sets.union(existingReminders, newReminders).toArray(new Reminder[0])));
    }

    @Test
    public void add0ReminderWhenYExisting() throws Exception {
        Set<Reminder> existingReminders = randomReminders(context, true);

        nurse(context).add(emptySet());

        assertThat(
                preferences.getStringSet(PREFERENCE_KEY_REMINDERS, null).stream()
                        .map(json -> gson.fromJson(json, Reminder.class))
                        .collect(toSet()),
                containsInAnyOrder(existingReminders.toArray(new Reminder[0])));
    }

    @Test
    public void replace() throws Exception {
        Set<Reminder> existingReminders = randomReminders(context, true);
        Set<Reminder> existingRemindersWithOddMedicationIds = existingReminders.stream()
                .filter(reminder -> reminder.getMedicationId() % 2 != 0)
                .collect(toSet());
        Set<Reminder> newReminders = singleton(new Reminder(randomDay(), now(), uniqueInt() * 2 + 1, 1));

        nurse(context).replace(
                reminder -> reminder.getMedicationId() % 2 == 0,
                newReminders);

        assertThat(
                preferences.getStringSet(PREFERENCE_KEY_REMINDERS, null).size(),
                is(existingRemindersWithOddMedicationIds.size() + newReminders.size()));

        assertThat(
                preferences.getStringSet(PREFERENCE_KEY_REMINDERS, null).stream()
                        .map(json -> gson.fromJson(json, Reminder.class))
                        .collect(toSet()),
                containsInAnyOrder(Sets.union(
                        existingRemindersWithOddMedicationIds,
                        newReminders).toArray(new Reminder[0])));
    }

    @Test
    public void setNotified() throws Exception {
        Reminder firstReminder = new ArrayList<>(
                randomReminders(context, true)
        ).get(0);
        int notificationId = uniqueInt();

        nurse(context).setNotified(firstReminder.getId(), notificationId);

        Reminder saved = preferences.getStringSet(PREFERENCE_KEY_REMINDERS, null).stream()
                .map(json -> gson.fromJson(json, Reminder.class))
                .filter(reminder -> reminder.equals(firstReminder))
                .findFirst().get();
        assertThat(saved.getState(), equalTo(Reminder.State.NOTIFIED));
        assertThat(saved.getNotificationId(), equalTo(notificationId));
    }

    @Test
    public void setDone() throws Exception {
        Reminder firstReminder = new ArrayList<>(
                randomReminders(context, true)
        ).get(0);

        nurse(context).setDone(firstReminder.getId());

        Reminder saved = preferences.getStringSet(PREFERENCE_KEY_REMINDERS, null).stream()
                .map(json -> gson.fromJson(json, Reminder.class))
                .filter(reminder -> reminder.equals(firstReminder))
                .findFirst().get();
        assertThat(saved.getState(), equalTo(Reminder.State.DONE));
    }

    @Test
    public void getReminders() throws Exception {
        LocalDate date = new ArrayList<>(
                randomReminders(context, true)
        ).get(0).getDate();

        Set<Reminder> reminders = nurse(context).getReminders(date);

        reminders.forEach(reminder -> assertThat(reminder.getDate(), equalTo(date)));
    }

    @Test
    public void getReminder() throws Exception {

    }

}