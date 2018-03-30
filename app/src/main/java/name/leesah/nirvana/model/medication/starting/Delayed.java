package name.leesah.nirvana.model.medication.starting;

import android.content.Context;

import org.joda.time.LocalDate;
import org.joda.time.Period;

import java.util.Objects;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.treatment.Treatment;

import static org.joda.time.format.PeriodFormat.wordBased;

/**
 * Created by sah on 2017-04-21.
 */

public class Delayed implements StartingStrategy {

    private final Period period;

    public Delayed(Period period) {
        this.period = period;
    }

    @Override
    public LocalDate getRealStartDate(Treatment treatment, LocalDate date) {
        return treatment.getStartDateOf(date).plus(period);
    }

    @Override
    public String toString(Context context) {
        return context.getString(R.string.to_string_starting_after_period, wordBased().print(period));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Delayed delayed = (Delayed) o;
        return Objects.equals(period, delayed.period);
    }

    @Override
    public int hashCode() {
        return Objects.hash(period);
    }

    public Period getPeriod() {
        return period;
    }
}
