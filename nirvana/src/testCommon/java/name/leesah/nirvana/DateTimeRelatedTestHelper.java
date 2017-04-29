package name.leesah.nirvana;

import android.support.annotation.NonNull;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Period;

import java.util.Random;

import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static org.joda.time.Period.days;

public class DateTimeRelatedTestHelper {
    public static final Period YEAR = days(365);
    public static final Random random = new Random();

    public static LocalDate randomDay() {
        return randomDayAfter(randomDayBefore(today()));
    }

    @NonNull
    public static LocalDate randomDayBefore(LocalDate date) {
        return date.minus(randomPeriod());
    }

    public static LocalDate randomDayAfter(LocalDate date) {
        return date.plus(randomPeriod());
    }

    @NonNull
    public static Period randomPeriod() {
        return randomPeriod(YEAR);
    }

    @NonNull
    public static Period randomPeriod(Period maximum) {
        return days(random.nextInt(maximum.getDays())).plus(Days.ONE);
    }

}