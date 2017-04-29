package name.leesah.nirvana.model.medication.starting;

import android.content.Context;

import org.joda.time.LocalDate;

import name.leesah.nirvana.model.treatment.Treatment;

/**
 * Created by sah on 2017-04-21.
 */

public interface StartingStrategy {

    default boolean hasStarted(Treatment treatment, LocalDate date) {
        return !getRealStartDate(treatment, date).isAfter(date);
    }

    LocalDate getRealStartDate(Treatment treatment, LocalDate date);

    String toString(Context context);

}
