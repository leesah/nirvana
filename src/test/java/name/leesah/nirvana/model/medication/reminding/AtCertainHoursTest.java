package name.leesah.nirvana.model.medication.reminding;


import android.support.v4.util.ArrayMap;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.medication.MedicationBuilder;
import name.leesah.nirvana.model.medication.repeating.RepeatingStrategy;
import name.leesah.nirvana.model.reminder.Reminder;
import name.leesah.nirvana.model.reminder.TimedDosage;

import static java.util.stream.IntStream.range;
import static junit.framework.Assert.assertEquals;
import static name.leesah.nirvana.model.medication.DosageForm.CAPSULE;
import static name.leesah.nirvana.model.reminder.Reminder.State.PLANNED;
import static name.leesah.nirvana.utils.DateTimeHelper.toText;
import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static org.mockito.Mockito.mock;

/**
 * Created by sah on 2017-04-06.
 */
@RunWith(MockitoJUnitRunner.class)
public class AtCertainHoursTest {
    private RemindingStrategy model;
    private ArrayMap<String, TimedDosage> dosages;
    private Random random;
    private Medication medication;
    private LocalDate today;

    @Before
    public void setUp() throws Exception {
        random = new Random();
        today = today();

        dosages = new ArrayMap<>();
        range(0, random.nextInt(4)).forEach(x -> addRandomDosage());
        model = new AtCertainHours(new ArrayList<>(dosages.values()));

        medication = new MedicationBuilder()
                .setName("Foo")
                .setManufacturer("Bar")
                .setForm(CAPSULE)
                .setRepeatingStrategy(mock(RepeatingStrategy.class))
                .setRemindingStrategy(model)
                .build();
    }

    private void addRandomDosage() {
        int hours = random.nextInt(24);
        int minutes = 15 * random.nextInt(4);
        int amount = random.nextInt(8) + 1;
        LocalTime time = new LocalTime(hours, minutes);
        dosages.put(toText(time), new TimedDosage(time, amount));
    }

    @Test
    public void getRemindersThroughDay() throws Exception {
        Set<Reminder> reminders = model.getRemindersThroughDay(medication, today());
        assertEquals("Wrong number of reminders.", dosages.size(), reminders.size());
        reminders.forEach(this::assertContentsOfReminderCorrect);

    }

    private void assertContentsOfReminderCorrect(Reminder r) {
        assertEquals("Medication ID mismatch.", medication.getId(), r.getMedicationId());
        assertEquals(PLANNED, r.getState());
        assertEquals(today, r.getDate());
        assertEquals(dosages.get(toText(r.getTime())).getAmount(), r.getDosageAmount());
    }

    @Test
    public void summarize() throws Exception {

    }

}