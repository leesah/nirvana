package name.leesah.nirvana.utils;

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

/**
 * Created by sah on 2016-12-14.
 */

public class DateTimeHelper {

    public static final PeriodFormatter PERIOD_FORMATTER = ISOPeriodFormat.standard();
    private static final DateTimeFormatter DATE_FORMATTER = ISODateTimeFormat.date();
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormat.forPattern("HH:mm");

    public static Period toPeriod(CharSequence text) {
        return PERIOD_FORMATTER.parsePeriod(text.toString());
    }

    public static Period toPeriod(int number, PeriodUnit unit) {
        switch (unit) {
            case DAY:
                return Days.days(number).toPeriod();
            case WEEK:
                return Weeks.weeks(number).toPeriod();
            case MONTH:
                return Months.months(number).toPeriod();
            default:
                return null;
        }
    }

    public static String toPeriodAsString(int number, PeriodUnit unit) {
        return toText(toPeriod(number, unit));
    }

    public static String toText(Period period) {
        return PERIOD_FORMATTER.print(period);
    }

    public static LocalDate toDate(CharSequence text) {
        return DATE_FORMATTER.parseLocalDate(text.toString());
    }

    public static LocalDate today() {
        return LocalDate.now();
    }

    public static String todayAsString() {
        return toText(today());
    }

    public static String toText(LocalDate date) {
        return date.toString(DATE_FORMATTER);
    }

    public static String toText(LocalTime time) {
        return time.toString(TIME_FORMATTER);
    }

}
