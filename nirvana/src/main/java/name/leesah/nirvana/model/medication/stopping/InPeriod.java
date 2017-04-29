package name.leesah.nirvana.model.medication.stopping;

import android.content.Context;

import org.joda.time.LocalDate;
import org.joda.time.Period;

import java.util.Objects;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.treatment.Treatment;

import static name.leesah.nirvana.utils.DateTimeHelper.toText;

/**
 * Created by sah on 2017-04-21.
 */

public class InPeriod implements StoppingStrategy {

    private Period period;

    public InPeriod(Period period) {
        this.period = period;
    }

    @Override
    public boolean hasStopped(Treatment treatment, LocalDate date) {
        return !treatment.getStartDateOf(date).plus(period).isAfter(date);
    }

    @Override
    public String toString(Context context) {
        return context.getString(R.string.to_string_stopping_after_period, toText(period));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InPeriod that = (InPeriod) o;
        return Objects.equals(period, that.period);
    }

    @Override
    public int hashCode() {
        return Objects.hash(period);
    }

    public Period getPeriod() {
        return period;
    }
}
