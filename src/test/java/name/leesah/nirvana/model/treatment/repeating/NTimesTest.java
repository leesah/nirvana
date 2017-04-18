package name.leesah.nirvana.model.treatment.repeating;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.junit.Test;

import java.util.Random;

import name.leesah.nirvana.model.treatment.TreatmentCycle;

import static junit.framework.Assert.assertEquals;

/**
 * Created by sah on 2016-12-06.
 */
public class NTimesTest extends CycleRepeatingModelTest {

    private int n = 2 + new Random().nextInt(128);

    @Override
    protected TreatmentCycleRepeatingModel getModelInstance() {
        return new NTimes(n);
    }

    @Override
    protected LocalDate getAVeryLateDate() {
        return getLastDayOfLastCycle().plus(Days.ONE);
    }

    private LocalDate getLastDayOfLastCycle() {
        return firstCycle.getFirstDay().plus(firstCycle.length().multipliedBy(n + 1)).minus(Days.ONE);
    }

    @Test
    public void verifyLastDayOfLastCycle(){
        TreatmentCycle lastCycle = new TreatmentCycle(firstCycle.getFirstDay().plus(firstCycle.length().multipliedBy(n)), firstCycle.getLastDay().plus(firstCycle.length().multipliedBy(n)));
        assertEquals(lastCycle, getModelInstance().getCycleForDate(firstCycle, getLastDayOfLastCycle()));
    }

}