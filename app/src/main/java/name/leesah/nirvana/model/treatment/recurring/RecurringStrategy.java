package name.leesah.nirvana.model.treatment.recurring;

import android.content.Context;

import org.joda.time.LocalDate;
import org.joda.time.Period;

/**
 * Created by sah on 2016-12-06.
 */
public interface RecurringStrategy {

    boolean hasNext(LocalDate dayZero, Period length, LocalDate date);

    String toString(Context context);
}
