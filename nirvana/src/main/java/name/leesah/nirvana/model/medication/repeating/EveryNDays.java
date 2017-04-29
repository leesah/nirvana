package name.leesah.nirvana.model.medication.repeating;

import android.content.Context;

import org.joda.time.LocalDate;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.starting.StartingStrategy;
import name.leesah.nirvana.model.treatment.Treatment;

import static org.joda.time.Days.*;

/**
 * Created by sah on 2016-12-07.
 */
public class EveryNDays implements RepeatingStrategy {

    private int n;

    public EveryNDays(int n) {
        this.n = n;
    }

    @Override
    public boolean matches(Treatment treatment, StartingStrategy startingStrategy, LocalDate date) {
        LocalDate realStartDate = startingStrategy.getRealStartDate(treatment, date);
        int between = daysBetween(realStartDate, date).getDays();
        return treatment.contains(date) && between % n == 0;
    }

    @Override
    public String toString(Context context) {
        return context.getString(R.string.to_string_medication_repeating_every_n_days, n);
    }
}
