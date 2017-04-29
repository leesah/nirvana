package name.leesah.nirvana.model.medication.reminding;


import android.content.Context;
import android.support.v4.util.ArrayMap;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.reminder.Reminder;
import name.leesah.nirvana.model.reminder.TimedDosage;

import static java.util.stream.IntStream.range;
import static junit.framework.Assert.assertEquals;
import static name.leesah.nirvana.LanternGenie.oneRandomMedicationSilVousPlait;
import static name.leesah.nirvana.model.reminder.Reminder.State.PLANNED;
import static name.leesah.nirvana.utils.DateTimeHelper.toText;
import static name.leesah.nirvana.utils.DateTimeHelper.today;

/**
 * Created by sah on 2017-04-06.
 */
@RunWith(RobolectricTestRunner.class)
public class AtCertainHoursTest {
    private RemindingStrategy model;
    private ArrayMap<String, TimedDosage> dosages;
    private Random random;
    private Medication medication;
    private LocalDate today;
    private Context context;

    @Before
    public void setUp() throws Exception {
        random = new Random();
        today = today();
        context = RuntimeEnvironment.application.getApplicationContext();

        dosages = new ArrayMap<>();
        range(0, random.nextInt(4)).forEach(x -> addRandomDosage());
        model = new AtCertainHours(new ArrayList<>(dosages.values()));

        medication = oneRandomMedicationSilVousPlait(context, false);
    }

    @Test
    public void getRemindersThroughDay() throws Exception {
        Set<Reminder> reminders = model.getRemindersThroughDay(medication, today());
        assertEquals("Wrong number of reminders.", dosages.size(), reminders.size());
        reminders.forEach(this::assertContentsOfReminderCorrect);

    }

    private void addRandomDosage() {
        int hours = random.nextInt(24);
        int minutes = 15 * random.nextInt(4);
        int amount = random.nextInt(8) + 1;
        LocalTime time = new LocalTime(hours, minutes);
        dosages.put(toText(time), new TimedDosage(time, amount));
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