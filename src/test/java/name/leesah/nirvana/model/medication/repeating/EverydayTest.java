package name.leesah.nirvana.model.medication.repeating;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import name.leesah.nirvana.model.treatment.TreatmentCycle;

import static java.util.stream.IntStream.rangeClosed;
import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static org.junit.Assert.assertTrue;

/**
 * Created by sah on 2016-12-08.
 */
public class EverydayTest {

    private RepeatingModel model;
    private TreatmentCycle random7DayCycle;
    private LocalDate firstDay;

    @Before
    public void setUp() throws Exception {
        model = new Everyday();
        firstDay = today().plusDays(new Random().nextInt(365));
        random7DayCycle = new TreatmentCycle(firstDay, firstDay.plusDays(6));
    }

    @Test
    public void everydayMatches() throws Exception {
        rangeClosed(1, 7).forEach(n -> assertMatches(firstDay.plusDays(n)));
    }

    @Test
    public void everydayMatchesEvenOutOfCycle() throws Exception {
        rangeClosed(8, 14).forEach(n -> assertMatches(firstDay.plusDays(n)));
    }

    private void assertMatches(LocalDate date) {
        assertTrue(String.format("Date [%s] failed to match Cycle [%s] by Model [everyday]", date, random7DayCycle), model.matchesDate(random7DayCycle, date));
    }


}