package name.leesah.nirvana.model.medication.stopping;

import android.content.Context;
import android.support.annotation.Keep;

import org.joda.time.LocalDate;

import name.leesah.nirvana.model.treatment.Treatment;

/**
 * Created by sah on 2017-04-21.
 */
@Keep
public interface StoppingStrategy {

    boolean hasStopped(Treatment treatment, LocalDate date);

    String toString(Context context);

}
