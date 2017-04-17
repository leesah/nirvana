package name.leesah.nirvana.model.medication.repeating;

import android.util.Log;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

import name.leesah.nirvana.model.DayOfWeek;
import name.leesah.nirvana.model.treatment.TreatmentCycle;

import static java.lang.String.format;
import static java.util.stream.Collectors.toSet;

/**
 * Created by sah on 2016-12-07.
 */
public class DaysOfWeek implements RepeatingModel {

    private static final String TAG = DaysOfWeek.class.getSimpleName();

    private final EnumSet<DayOfWeek> daysOfWeek;
    private final Set<Integer> daysOfWeekAsIntegers;

    public DaysOfWeek(EnumSet<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
        daysOfWeekAsIntegers = daysOfWeek.stream()
                .map(this::getJodaTimeConstant)
                .collect(toSet());
    }

    @Override
    public boolean matchesDate(TreatmentCycle currentCycle, LocalDate date) {
        return daysOfWeekAsIntegers.contains(date.getDayOfWeek());
    }

    private int getJodaTimeConstant(DayOfWeek day) {
        switch (day) {
            case MONDAY:
                return DateTimeConstants.MONDAY;
            case TUESDAY:
                return DateTimeConstants.TUESDAY;
            case WEDNESDAY:
                return DateTimeConstants.WEDNESDAY;
            case THURSDAY:
                return DateTimeConstants.THURSDAY;
            case FRIDAY:
                return DateTimeConstants.FRIDAY;
            case SATURDAY:
                return DateTimeConstants.SATURDAY;
            case SUNDAY:
                return DateTimeConstants.SUNDAY;
            default:
                String msg = format("Unexpected day of week: [%s].", day);
                Log.wtf(TAG, msg);
                throw new IllegalStateException(msg);
        }
    }


}
