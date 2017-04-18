package name.leesah.nirvana.model.treatment.repeating;

import org.joda.time.LocalDate;

import name.leesah.nirvana.model.treatment.TreatmentCycle;
import name.leesah.nirvana.utils.DateTimeHelper;

import static java.lang.String.format;
import static name.leesah.nirvana.utils.DateTimeHelper.toText;

/**
 * Created by sah on 2016-12-03.
 */

public class UntilDate implements TreatmentCycleRepeatingModel {

    private LocalDate lastDay;

    public UntilDate(LocalDate lastDay) {
        this.lastDay = lastDay;
    }

    @Override
    public TreatmentCycle getCycleForDate(TreatmentCycle cycle, LocalDate date) {
        if (cycle.getFirstDay().isAfter(lastDay))
            return null;
        else if (cycle.contains(date))
            return cycle;
        else
            return getCycleForDate(TreatmentCycle.makeNext(cycle), date);
    }

    @Override
    public String toString() {
        return format("Repeating until %s", toText(lastDay));
    }
}
