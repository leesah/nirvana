package name.leesah.nirvana.model.medication.stopping;

import android.content.Context;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Period;

import name.leesah.nirvana.R;

import static name.leesah.nirvana.utils.DateTimeHelper.toText;

/**
 * Created by sah on 2017-04-21.
 */

public class AfterPeriod implements StoppingStrategy {

    private Period period;

    public AfterPeriod(Period period) {
        this.period = period;
    }

    @Override
    public LocalDate getLastDay(LocalDate firstDay) {
        return firstDay.plus(period).minus(Days.ONE);
    }

    @Override
    public String toString(Context context) {
        return context.getString(R.string.to_string_stopping_after_period, toText(period));
    }
}
