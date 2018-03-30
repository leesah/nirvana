package name.leesah.nirvana.model.treatment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.LocalDate;
import org.joda.time.Period;

import name.leesah.nirvana.model.treatment.recurring.RecurringStrategy;

import static java.lang.String.format;
import static name.leesah.nirvana.utils.DateTimeHelper.dateFallsInDuration;
import static name.leesah.nirvana.utils.DateTimeHelper.toText;
import static org.joda.time.Days.days;

/**
 * Created by sah on 2016-12-03.
 */

public class RecurringTreatment implements Treatment {

    private final LocalDate dayZero;
    private final Period length;
    private final RecurringStrategy recurringStrategy;

    public RecurringTreatment(LocalDate dayZero, Period length, RecurringStrategy recurringStrategy) {
        this.dayZero = dayZero;
        this.length = length;
        this.recurringStrategy = recurringStrategy;
    }

    @Override
    public boolean contains(@NonNull LocalDate date) {
        return getStartDateOf(date) != null;
    }

    @Override
    @Nullable
    public LocalDate getStartDateOf(@NonNull LocalDate date) {
        return getStartDate(date, dayZero);
    }

    @Nullable
    private LocalDate getStartDate(@NonNull LocalDate date, LocalDate firstDayOfCycle) {
        if (dateFallsInDuration(date, firstDayOfCycle, length))
            return firstDayOfCycle;
        else if (recurringStrategy.hasNext(dayZero, length, firstDayOfCycle))
            return getStartDate(date, firstDayOfCycle.plus(length));
        else
            return null;
    }

    @Override
    public String toString() {
        return format("[Cycle %s + %s (%s), %s]", toText(dayZero), toText(length), toText(dayZero.plus(length).minus(days(1))), recurringStrategy.toString());
    }


    public LocalDate getDayZero() {
        return dayZero;
    }

    public Period getLength() {
        return length;
    }

    public RecurringStrategy getRecurringStrategy() {
        return recurringStrategy;
    }
}
