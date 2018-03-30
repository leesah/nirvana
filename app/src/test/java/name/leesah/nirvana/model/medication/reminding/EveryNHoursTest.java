package name.leesah.nirvana.model.medication.reminding;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Set;

import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.reminder.Reminder;
import name.leesah.nirvana.model.reminder.TimedDosage;

import static java.lang.String.format;
import static name.leesah.nirvana.LanternGenie.randomMedication;
import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.mockito.internal.util.collections.Sets.newSet;
import static org.robolectric.RuntimeEnvironment.application;

/**
 * Created by esaalii on 2018-02-01.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = {24, 25, 26})
public class EveryNHoursTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void acceptEvery04HoursFrom0800() throws Exception {
        new EveryNHours(new TimedDosage(new LocalTime(8, 0), 1), 4);
    }

    @Test
    public void acceptEvery12HoursFrom0800() throws Exception {
        new EveryNHours(new TimedDosage(new LocalTime(8, 0), 1), 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectEvery03HoursFrom0800() throws Exception {
        new EveryNHours(new TimedDosage(new LocalTime(8, 0), 1), 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectEvery04HoursFrom2000() throws Exception {
        new EveryNHours(new TimedDosage(new LocalTime(20, 0), 1), 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectEvery04HoursFrom2200() throws Exception {
        new EveryNHours(new TimedDosage(new LocalTime(22, 0), 1), 4);
    }

    @Test
    public void getRemindersEvery04HoursFrom0800() throws Exception {
        EveryNHours model = new EveryNHours(new TimedDosage(new LocalTime(8, 0), 1), 4);
        Medication m = randomMedication(application.getApplicationContext(), false);
        Set<Reminder> reminders = model.getRemindersThroughDay(m, today());

        assertThat(reminders, containsInAnyOrder(newSet(
                ofMedicationAtLocalTime(m, 8),
                ofMedicationAtLocalTime(m, 12),
                ofMedicationAtLocalTime(m, 16),
                ofMedicationAtLocalTime(m, 20)
        )));
    }

    private static Matcher<Reminder> ofMedicationAtLocalTime(Medication medication, int hour) {
        return ofMedicationAtLocalTime(medication, hour, 0);
    }

    private static Matcher<Reminder> ofMedicationAtLocalTime(Medication medication, int hour, int minute) {
        return new BaseMatcher<Reminder>() {
            @Override
            public void describeTo(Description description) {
                description.appendText(format("Reminder for [%s] on [%2d:%2d]", medication.getName(), hour, minute));
            }

            @Override
            public boolean matches(Object item) {
                return item instanceof Reminder &&
                        ((Reminder) item).getMedicationId() == medication.getId() &&
                        ((Reminder) item).getPlannedTime().getHourOfDay() == hour &&
                        ((Reminder) item).getPlannedTime().getMinuteOfHour() == minute;
            }
        };
    }

    @Test
    public void getFirstDose() throws Exception {
    }

    @Test
    public void getN() throws Exception {
    }


}