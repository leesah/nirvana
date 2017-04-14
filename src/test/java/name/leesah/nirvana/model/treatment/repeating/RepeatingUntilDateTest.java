package name.leesah.nirvana.model.treatment.repeating;

import org.joda.time.LocalDate;

import java.util.Random;

import name.leesah.nirvana.model.treatment.TreatmentCycle;

/**
 * Created by sah on 2016-12-06.
 */
public class RepeatingUntilDateTest extends CycleRepeatingModelTest {

    private LocalDate untilDate;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        untilDate = TreatmentCycle.makeNext(firstCycle).getLastDay().plusDays(new Random().nextInt(365));
    }

    @Override
    protected TreatmentCycleRepeatingModel getModelInstance() {
        return new RepeatingUntilDate(untilDate);
    }

    @Override
    protected LocalDate getAVeryLateDate() {
        return untilDate.plus(firstCycle.length());
    }

}