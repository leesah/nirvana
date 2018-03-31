package name.leesah.nirvana.model.medication.reminding;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Set;

import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.reminder.Reminder;
import name.leesah.nirvana.model.reminder.TimedDosage;

import static java.lang.String.format;
import static name.leesah.nirvana.LanternGenie.randomMedication;
import static name.leesah.nirvana.model.medication.reminding.EveryNHours.isCombinationLogical;
import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.internal.util.collections.Sets.newSet;
import static org.robolectric.RuntimeEnvironment.application;

/**
 * Created by esaalii on 2018-02-01.
 */
@RunWith(RobolectricTestRunner.class)
public class EveryNHoursTest {

    private Medication medication;

    @Before
    public void setUp() throws Exception {
        medication = randomMedication(application.getApplicationContext(), false);
    }

    @Test
    public void acceptEvery04HoursFrom0800() {
        assertThat(isCombinationLogical(new LocalTime(8, 0), 4), is(true));
    }

    @Test
    public void acceptEvery12HoursFrom0800() {
        assertThat(isCombinationLogical(new LocalTime(8, 0), 4), is(true));
    }

    @Test
    public void rejectEvery04HoursFrom2000() {
        assertThat(isCombinationLogical(new LocalTime(20, 0), 4), is(false));
    }

    @Test
    public void rejectEvery04HoursFrom2200() {
        assertThat(isCombinationLogical(new LocalTime(22, 0), 4), is(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectEvery03HoursFrom0800() {
        new EveryNHours(new TimedDosage(new LocalTime(8, 0), 1), 3);
    }

    @Test
    public void getRemindersEvery04HoursFrom0800() {
        EveryNHours model = new EveryNHours(new TimedDosage(new LocalTime(8, 0), 1), 4);
        Set<Reminder> reminders = model.getRemindersThroughDay(medication, today());

        assertThat(reminders, containsInAnyOrder(newSet(
                ofMedicationAtTimeOfDay(medication, 8, 0),
                ofMedicationAtTimeOfDay(medication, 12, 0),
                ofMedicationAtTimeOfDay(medication, 16, 0),
                ofMedicationAtTimeOfDay(medication, 20, 0)
        )));
    }

    @Test
    public void getRemindersEvery08HoursFrom0815() {
        EveryNHours model = new EveryNHours(new TimedDosage(new LocalTime(8, 15), 1), 8);
        Set<Reminder> reminders = model.getRemindersThroughDay(medication, today());

        assertThat(reminders, containsInAnyOrder(newSet(
                ofMedicationAtTimeOfDay(medication, 8, 15),
                ofMedicationAtTimeOfDay(medication, 16, 15)
        )));
    }

    private static Matcher<Reminder> ofMedicationAtTimeOfDay(Medication medication, int hour, int minute) {
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

}
