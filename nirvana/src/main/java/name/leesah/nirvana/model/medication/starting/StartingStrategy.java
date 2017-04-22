package name.leesah.nirvana.model.medication.starting;

import android.content.Context;

import org.joda.time.LocalDate;

import name.leesah.nirvana.model.treatment.TreatmentCycle;

/**
 * Created by sah on 2017-04-21.
 */

public interface StartingStrategy {

    LocalDate getFirstDay(TreatmentCycle cycle);

    String toString(Context context);
}
