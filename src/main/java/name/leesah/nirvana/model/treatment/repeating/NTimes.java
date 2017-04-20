package name.leesah.nirvana.model.treatment.repeating;

import org.joda.time.LocalDate;

import name.leesah.nirvana.model.treatment.TreatmentCycle;

import static java.lang.String.format;


/**
 * Created by sah on 2016-12-03.
 */

public class NTimes implements TreatmentCycleRecurringStrategy {

    private int n;

    public NTimes(int n) {
        this.n = n;
    }

    @Override
    public TreatmentCycle getCycleForDate(TreatmentCycle firstCycle, LocalDate date) {
        return getCycleForDate(firstCycle, date, n + 1);
    }

    private TreatmentCycle getCycleForDate(TreatmentCycle cycle, LocalDate date, int timesLeft) {
        if (timesLeft == 0)
            return null;
        else if (cycle.contains(date))
            return cycle;
        else
            return getCycleForDate(TreatmentCycle.makeNext(cycle), date, timesLeft - 1);
    }

    @Override
    public String toString() {
        return format("Repeating for %d times", n);
    }
}
