package name.leesah.nirvana.model.treatment.recurring;

import org.joda.time.LocalDate;

import name.leesah.nirvana.model.treatment.TreatmentCycle;

/**
 * Created by sah on 2016-12-03.
 */

public class NotRepeating implements RecurringStrategy {

    @Override
    public TreatmentCycle getCycleForDate(final TreatmentCycle firstCycle, final LocalDate date) {
        return firstCycle.contains(date) ? firstCycle : null;
    }

    @Override
    public String toString() {
        return "Not repeating";
    }
}