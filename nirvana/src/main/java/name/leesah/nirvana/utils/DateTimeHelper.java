package name.leesah.nirvana.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Months;
import org.joda.time.Period;
import org.joda.time.ReadablePeriod;
import org.joda.time.Weeks;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;

import name.leesah.nirvana.model.PeriodUnit;

import static java.lang.String.format;

/**
 * Created by sah on 2016-12-14.
 */

public class DateTimeHelper {

    private static final String TAG = DateTimeHelper.class.getSimpleName();

    private static final PeriodFormatter PERIOD_FORMATTER = ISOPeriodFormat.standard();
    private static final DateTimeFormatter DATE_FORMATTER = ISODateTimeFormat.date();
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormat.forPattern("HH:mm");

    @Nullable
    public static Period toPeriod(@Nullable CharSequence text) {
        return text == null ? null : PERIOD_FORMATTER.parsePeriod(text.toString());
    }

    @NonNull
    public static Period toPeriod(int number, PeriodUnit unit) {
        switch (unit) {
            case DAY:
                return Days.days(number).toPeriod();
            case WEEK:
                return Weeks.weeks(number).toPeriod();
            case MONTH:
                return Months.months(number).toPeriod();
            default:
                String msg = format("Unrecognizable unit: [%s].", unit.name());
                Log.wtf(TAG, msg);
                throw new IllegalArgumentException(msg);
        }
    }

    @NonNull
    public static String toPeriodAsString(int number, PeriodUnit unit) {
        return toText(toPeriod(number, unit));
    }

    @Nullable
    public static String toText(@Nullable Period period) {
        return period == null ? null : PERIOD_FORMATTER.print(period);
    }

    @Nullable
    public static LocalDate toDate(@Nullable CharSequence text) {
        return text == null ? null : DATE_FORMATTER.parseLocalDate(text.toString());
    }

    @NonNull
    public static LocalDate today() {
        return LocalDate.now();
    }

    @NonNull
    public static String todayAsString() {
        return toText(today());
    }

    @Nullable
    public static String toText(@Nullable LocalDate date) {
        return date == null ? null : date.toString(DATE_FORMATTER);
    }

    @Nullable
    public static String toText(@Nullable LocalTime time) {
        return time == null ? null : time.toString(TIME_FORMATTER);
    }

    public static boolean dateFallsInDuration(@NonNull LocalDate dayX, @NonNull LocalDate day0, @NonNull Period length) {
        return !day0.isAfter(dayX) && day0.plus(length).isAfter(dayX);
    }
}
