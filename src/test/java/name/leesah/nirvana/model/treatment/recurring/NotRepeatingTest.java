package name.leesah.nirvana.model.treatment.recurring;

import org.joda.time.Days;
import org.joda.time.LocalDate;

/**
 * Created by sah on 2016-12-06.
 */
public class NotRepeatingTest extends CycleRepeatingModelTest {

    @Override
    protected RecurringStrategy getModelInstance() {
        return new NotRepeating();
    }

    @Override
    protected LocalDate getAVeryLateDate() {
        return firstCycle.getLastDay().plus(Days.TWO);
    }

}