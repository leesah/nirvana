package name.leesah.nirvana.model.treatment.recurring;

import android.support.annotation.IntRange;

import org.joda.time.LocalDate;
import org.joda.time.Period;

import name.leesah.nirvana.utils.DateTimeHelper;

import static java.lang.String.format;
import static java.util.Locale.US;


/**
 * Created by sah on 2016-12-03.
 */

public class NTimes implements RecurringStrategy {

    private int n;

    public NTimes(@IntRange(from = 1) int n) {
        this.n = n;
    }

    @Override
    public boolean hasNext(LocalDate dayZero, Period length, LocalDate date) {
        return hasNext(0, dayZero, length, date);
    }

    private boolean hasNext(int i, LocalDate dayZero, Period length, LocalDate date) {
        if (i == n - 1)
            return false;
        else if (DateTimeHelper.dateFallsInDuration(date, dayZero, length))
            return true;
        else
            return hasNext(i + 1, dayZero.plus(length), length, date);
    }

    @Override
    public String toString() {
        return format(US, "Repeating for %d times", n);
    }

    public int getN() {
        return n;
    }
}
