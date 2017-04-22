package name.leesah.nirvana.model.medication.repeating;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;
import java.util.stream.Stream;

import name.leesah.nirvana.model.treatment.TreatmentCycle;

import static java.util.stream.IntStream.range;
import static junit.framework.Assert.assertFalse;
import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static org.junit.Assert.assertTrue;

/**
 * Created by sah on 2016-12-08.
 */
public class EveryNDaysTest {

    private RepeatingStrategy model;
    private TreatmentCycle random30DayCycle;
    private LocalDate firstDay;
    private int n;

    @Before
    public void setUp() throws Exception {
        n = new Random().nextInt(10) + 1;
        model = new EveryNDays(n);
        firstDay = today().plusDays(new Random().nextInt(365));
        random30DayCycle = new TreatmentCycle(firstDay, firstDay.plusDays(29));
    }

    @Test
    public void multiplesOfN() throws Exception {
        Stream.of(0, n, n * 2).forEach(x -> assertMatches(firstDay.plusDays(x)));
    }

    @Test
    public void between0AndN() throws Exception {
        range(1, n).forEach(x -> assertMatchesNot(firstDay.plusDays(x)));
    }

    @Test
    public void beforeFirstDayOfCycle() throws Exception {
        assertMatchesNot(firstDay.minusDays(n));
    }

    private void assertMatches(LocalDate date) {
        assertTrue(String.format("Date [%s] failed to match Cycle [%s] by Model [every %d days].", date, random30DayCycle, n), model.matchesDate(random30DayCycle, date));
    }

    private void assertMatchesNot(LocalDate date) {
        assertFalse(String.format("Date [%s] matched Cycle [%s] by Model [every %d days] when it was not supposed to.", date, random30DayCycle, n), model.matchesDate(random30DayCycle, date));
    }


}