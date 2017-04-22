package name.leesah.nirvana.model.medication.stopping;

import android.content.Context;

import org.joda.time.LocalDate;

import name.leesah.nirvana.model.treatment.TreatmentCycle;

/**
 * Created by sah on 2017-04-21.
 */

public interface StoppingStrategy {

    LocalDate getLastDay(LocalDate firstDay);

    String toString(Context context);

}
