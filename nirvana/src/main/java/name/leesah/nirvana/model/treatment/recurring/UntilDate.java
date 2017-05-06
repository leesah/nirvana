package name.leesah.nirvana.model.treatment.recurring;

import android.content.Context;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;
import org.joda.time.Period;

import name.leesah.nirvana.R;

import static java.lang.String.format;
import static name.leesah.nirvana.utils.DateTimeHelper.dateFallsInDuration;
import static name.leesah.nirvana.utils.DateTimeHelper.toText;
import static org.joda.time.format.DateTimeFormat.fullDate;

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
        else if (dateFallsInDuration(date, dayZero, length))
            return true;
        else
            return hasNext(dayZero.plus(length), length, date);
    }

    @Override
    public String toString(Context context) {
        return context.getString(R.string.to_string_recurring_until_date, fullDate().print(until));
    }

    @Override
    public String toString() {
        return format("Repeating until %s", toText(until));
    }

}
