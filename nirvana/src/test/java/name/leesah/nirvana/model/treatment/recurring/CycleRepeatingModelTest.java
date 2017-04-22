package name.leesah.nirvana.model.treatment.recurring;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import name.leesah.nirvana.model.treatment.TreatmentCycle;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * Created by sah on 2016-12-06.
 */
public abstract class CycleRepeatingModelTest {

    TreatmentCycle firstCycle;

    @Before
    public void setUp() throws Exception {
        firstCycle = new TreatmentCycle(LocalDate.now(), LocalDate.now().plusDays(1 + new Random().nextInt(128)));
    }

    @Test
    public void verifyBeforeFirstDayOfFirstCycle(){
        assertNull(getModelInstance().getCycleForDate(firstCycle, firstCycle.getFirstDay().minus(Days.ONE)));
    }

    @Test
    public void verifyFirstDayOfFirstCycle(){
        assertEquals(firstCycle, getModelInstance().getCycleForDate(firstCycle, firstCycle.getFirstDay()));
    }

    @Test
    public void verifyLastDayOfFirstCycle(){
        assertEquals(firstCycle, getModelInstance().getCycleForDate(firstCycle, firstCycle.getLastDay()));
    }

    @Test
    public void verifyAfterLastDayOfLastCycle(){
        assertNull(getModelInstance().getCycleForDate(firstCycle, getAVeryLateDate()));
    }

    protected abstract RecurringStrategy getModelInstance();

    protected abstract LocalDate getAVeryLateDate();

}
