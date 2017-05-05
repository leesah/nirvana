package name.leesah.nirvana.model.reminder;

import android.content.Context;

import org.joda.time.LocalTime;

import java.util.Comparator;

import name.leesah.nirvana.R;

import static java.lang.String.format;
import static java.util.Locale.US;
import static name.leesah.nirvana.utils.DateTimeHelper.toText;

/**
 * Created by sah on 2016-12-11.
 */
public class TimedDosage {
    private final LocalTime timeOfDay;
    private final int amount;

    public TimedDosage(LocalTime timeOfDay, int amount) {
        this.timeOfDay = timeOfDay;
        this.amount = amount;
    }

    public LocalTime getTimeOfDay() {
        return timeOfDay;
    }

    public int getAmount() {
        return amount;
    }

    public String toString(Context context) {
        return context.getResources().getQuantityString(R.plurals.to_string_timed_dosage, amount, amount, toText(timeOfDay));
    }

    public static final Comparator<TimedDosage> comparator = Comparator.comparingInt(td -> td.timeOfDay.getMillisOfDay());

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimedDosage that = (TimedDosage) o;
        return amount == that.amount && timeOfDay.equals(that.timeOfDay);
    }

    @Override
    public int hashCode() {
        int result = timeOfDay.hashCode();
        result = 31 * result + amount;
        return result;
    }

    @Override
    public String toString() {
        return format(US, "%d unit(s) at %s", amount, toText(timeOfDay));
    }
}
