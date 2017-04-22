package name.leesah.nirvana.model.medication.starting;

import android.content.Context;

import org.joda.time.LocalDate;
import org.joda.time.Period;

import name.leesah.nirvana.model.treatment.TreatmentCycle;

/**
 * Created by sah on 2017-04-21.
 */

public class DelayForPeriod implements StartingStrategy {

    private Period period;

    public DelayForPeriod(Period period) {
        this.period = period;
    }

    @Override
    public LocalDate getFirstDay(TreatmentCycle cycle) {
        return cycle.getFirstDay().plus(period);
    }

    @Override
    public String toString(Context context) {
        return null;
    }
}
