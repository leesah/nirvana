package name.leesah.nirvana.model.medication.repeating;

import android.content.Context;
import android.support.annotation.Keep;

import org.joda.time.LocalDate;

import name.leesah.nirvana.model.medication.starting.StartingStrategy;
import name.leesah.nirvana.model.treatment.Treatment;

/**
 * Created by sah on 2016-12-07.
 */
@Keep
public interface RepeatingStrategy {

    boolean matches(Treatment treatment, StartingStrategy startingStrategy, LocalDate date);

    String toString(Context context);

}
