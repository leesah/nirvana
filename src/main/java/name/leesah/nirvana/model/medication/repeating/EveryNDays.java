package name.leesah.nirvana.model.medication.repeating;

import android.content.Context;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.treatment.TreatmentCycle;

/**
 * Created by sah on 2016-12-07.
 */
public class EveryNDays implements RepeatingModel {

    private int n;

    public EveryNDays(int n) {
        this.n = n;
    }

    @Override
    public boolean matchesDate(TreatmentCycle cycle, LocalDate date) {
        return cycle.contains(date) && Days.daysBetween(cycle.getFirstDay(), date).getDays() % n == 0;
    }

    @Override
    public String toString(Context context) {
        return context.getString(R.string.medication_repeating_every_var_days, n);
    }
}
