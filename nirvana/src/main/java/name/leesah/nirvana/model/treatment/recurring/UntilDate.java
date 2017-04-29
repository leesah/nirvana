package name.leesah.nirvana.model.treatment.recurring;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;
import org.joda.time.Period;

import name.leesah.nirvana.utils.DateTimeHelper;

import static java.lang.String.format;
import static name.leesah.nirvana.utils.DateTimeHelper.toText;

/**
 * Created by sah on 2016-12-03.
 */

public class UntilDate implements RecurringStrategy {

    private LocalDate until;

    public UntilDate(@NonNull LocalDate until) {
        this.until = until;
    }

    @Override
    public boolean hasNext(LocalDate dayZero, Period length, LocalDate date) {
        if (!date.isBefore(until))
            return false;
        else if (DateTimeHelper.dateFallsInDuration(date, dayZero, length))
            return true;
        else
            return hasNext(dayZero.plus(length), length, date);
    }

    @Override
    public String toString() {
        return format("Repeating until %s", toText(until));
    }

}
