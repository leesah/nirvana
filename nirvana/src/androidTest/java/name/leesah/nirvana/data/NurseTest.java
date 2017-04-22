package name.leesah.nirvana.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import name.leesah.nirvana.model.reminder.Reminder;
import name.leesah.nirvana.utils.AdaptedGsonFactory;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static java.util.Collections.emptySet;
import static java.util.stream.IntStream.range;
import static name.leesah.nirvana.data.Nurse.PREFERENCE_KEY_DAILY_REMINDERS;
import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static name.leesah.nirvana.utils.IdentityHelper.uniqueInt;
import static org.hamcrest.Matchers.is;
import static org.joda.time.LocalTime.now;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by sah on 2017-04-06.
 */
public class NurseTest {
    public static final int ZERO = 0;
    public static final int RANDOM_INT_MAX_VALUE = 32;
    private final Random random = new Random();
    private Nurse nurse;
    private SharedPreferences preferences;
    private Gson gson;

    @Before
    public void setUp() throws Exception {
        Context context = getTargetContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        gson = AdaptedGsonFactory.getGson();
        nurse = new Nurse(context);
    }

    @After
    public void tearDown() throws Exception {
        preferences.edit().remove(PREFERENCE_KEY_DAILY_REMINDERS).apply();
    }

    @Test
    public void addXRemindersWhen0Existing() throws Exception {
        int numberOfNewReminders = randomNonZeroInteger();

        givenExistingReminders(ZERO);

        Set<Reminder> reminders = whenAddReminders(numberOfNewReminders);

        thenNumberOfAllRemindersIs(numberOfNewReminders);
        thenNewRemindersAreAllSaved(reminders);
    }

    @Test
    public void add0ReminderWhen0Existing() throws Exception {
        givenExistingReminders(ZERO);

        whenAddReminders(ZERO);

        thenNumberOfAllRemindersIs(ZERO);
    }

    @Test
    public void addXRemindersWhenYExisting() throws Exception {
        int numberOfExistingReminders = randomNonZeroInteger();
        int numberOfNewReminders = randomNonZeroInteger();

        givenExistingReminders(numberOfExistingReminders);

        Set<Reminder> reminders = whenAddReminders(numberOfNewReminders);

        thenNumberOfAllRemindersIs(numberOfExistingReminders + numberOfNewReminders);
        thenNewRemindersAreAllSaved(reminders);
    }

    @Test
    public void add0ReminderWhenYExisting() throws Exception {
        int numberOfExistingReminders = randomNonZeroInteger();

        givenExistingReminders(numberOfExistingReminders);

        whenAddReminders(ZERO);

        thenNumberOfAllRemindersIs(numberOfExistingReminders);
    }

    @Test
    public void replace() throws Exception {

    }

    @Test
    public void setNotified() throws Exception {

    }

    @Test
    public void setDone() throws Exception {

    }

    @Test
    public void getReminders() throws Exception {

    }

    @Test
    public void getReminder() throws Exception {

    }

    private void givenExistingReminders(int n) {
        preferences.edit().putStringSet(PREFERENCE_KEY_DAILY_REMINDERS, n == ZERO ? emptySet() : randomRemindersAsStringSet(n)).apply();
    }

    private Set<Reminder> whenAddReminders(int n) {
        Set<Reminder> reminders = n == ZERO ? emptySet() : randomRemindersAsSet(n);
        nurse.add(reminders);
        return reminders;
    }

    private void thenNumberOfAllRemindersIs(int n) {
        assertThat(getSavedStringSet().size(), is(n));
    }

    private void thenNewRemindersAreAllSaved(Set<Reminder> reminders) {
        Set<Integer> allIds = getSavedStringSet().stream().map(json -> gson.fromJson(json, Reminder.class).getId()).collect(Collectors.toSet());
        Set<Integer> newIds = reminders.stream().map(Reminder::getId).collect(Collectors.toSet());
        newIds.forEach(id -> assertTrue(allIds.contains(id)));
    }

    private Set<String> getSavedStringSet() {
        return preferences.getStringSet(PREFERENCE_KEY_DAILY_REMINDERS, emptySet());
    }

    private Set<Reminder> randomRemindersAsSet(int n) {
        return randomRemindersAsStream(n).collect(Collectors.toSet());
    }

    private Set<String> randomRemindersAsStringSet(int n) {
        return randomRemindersAsStream(n).map(r -> gson.toJson(r)).collect(Collectors.toSet());
    }

    private Stream<Reminder> randomRemindersAsStream(int n) {
        return range(ZERO, n).mapToObj(x -> randomReminder());
    }

    private Reminder randomReminder() {
        return new Reminder(today(), now(), uniqueInt(), randomNonZeroInteger());
    }

    private int randomNonZeroInteger() {
        return random.nextInt(RANDOM_INT_MAX_VALUE) + 1;
    }

}