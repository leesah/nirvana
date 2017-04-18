package name.leesah.nirvana.model.medication.repeating;

import android.content.Context;
import android.util.Log;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.DayOfWeek;
import name.leesah.nirvana.model.treatment.TreatmentCycle;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.join;

/**
 * Created by sah on 2016-12-07.
 */
public class DaysOfWeek implements RepeatingModel {

    private static final String TAG = DaysOfWeek.class.getSimpleName();

    private final EnumSet<DayOfWeek> daysOfWeek;

    public DaysOfWeek(EnumSet<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    @Override
    public boolean matchesDate(TreatmentCycle currentCycle, LocalDate date) {
        return asIntegers().contains(date.getDayOfWeek());
    }

    private Set<Integer> asIntegers() {
        return daysOfWeek.stream()
                .map(this::getJodaTimeConstant)
                .collect(toSet());
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

    @Override
    public String toString(Context context) {
        String days = join(asStrings(), context.getString(R.string.comma_equivalent));
        return context.getString(R.string.medication_repeating_var_days_of_week, days);
    }

    private List<String> asStrings() {
        return daysOfWeek.stream()
                .map(Enum::name)
                .collect(toList());
    }

}
