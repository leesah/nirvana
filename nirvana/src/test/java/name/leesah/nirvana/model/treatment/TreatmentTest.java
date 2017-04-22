package name.leesah.nirvana.model.treatment;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Created by sah on 2016-12-07.
 */
public class TreatmentTest {

    private int lengthNumber;
    private LocalDate firstDay;

    @Before
    public void setUp() {
        lengthNumber = new Random().nextInt(365);
        firstDay = LocalDate.now();
    }

    @Test
    public void getFirstCycleLastingDays() throws Exception {
        Treatment treatment = new Treatment(firstDay, Period.days(lengthNumber), null);
        assertEquals(new TreatmentCycle(firstDay, firstDay.plusDays(lengthNumber).minusDays(1)), treatment.getFirstCycle());
    }

    @Test
    public void getFirstCycleLastingWeeks() throws Exception {
        Treatment treatment = new Treatment(firstDay, Period.weeks(lengthNumber), null);
        assertEquals(new TreatmentCycle(firstDay, firstDay.plusWeeks(lengthNumber).minusDays(1)), treatment.getFirstCycle());
    }

    @Test
    public void getFirstCycleLastingMonths() throws Exception {
        Treatment treatment = new Treatment(firstDay, Period.months(lengthNumber), null);
        assertEquals(new TreatmentCycle(firstDay, firstDay.plusMonths(lengthNumber).minusDays(1)), treatment.getFirstCycle());
    }

}