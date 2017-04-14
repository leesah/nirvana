package name.leesah.nirvana.model.reminder;

import android.os.Parcelable;

import org.joda.time.LocalTime;
import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.Comparator;

import static java.lang.String.format;
import static name.leesah.nirvana.utils.DateTimeHelper.toText;

/**
 * Created by sah on 2016-12-11.
 */
@Parcel
public class TimedDosage{
    private final LocalTime timeOfDay;
    private final int amount;

    @ParcelConstructor
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

    @Override
    public String toString() {
        return format("%2d %s at %s.", amount, "unit(s)", toText(timeOfDay));
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
}
