package name.leesah.nirvana.model;

import org.joda.time.Days;
import org.joda.time.Months;
import org.joda.time.ReadablePeriod;
import org.joda.time.Weeks;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by sah on 2016-12-07.
 */
public enum PeriodUnit {
    DAY(Days.class, 0),
    WEEK(Weeks.class, 1),
    MONTH(Months.class, 2);

    private final Class<? extends ReadablePeriod> clazz;
    private int positionInSpinner;


    PeriodUnit(Class<? extends ReadablePeriod> clazz, int position) {
        this.clazz = clazz;
        this.positionInSpinner = position;

    }

    public int getPositionInSpinner() {
        return positionInSpinner;
    }

    public ReadablePeriod toPeriod(int n) {
        try {
            return (ReadablePeriod) clazz.getMethod(clazz.getSimpleName().toLowerCase(), int.class).invoke(null, n);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }
}
