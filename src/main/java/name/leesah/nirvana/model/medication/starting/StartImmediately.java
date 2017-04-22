package name.leesah.nirvana.model.medication.starting;

import android.content.Context;

import org.joda.time.LocalDate;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.treatment.TreatmentCycle;

/**
 * Created by sah on 2017-04-21.
 */

public class StartImmediately implements StartingStrategy {

    @Override
    public LocalDate getFirstDay(TreatmentCycle cycle) {
        return cycle.getFirstDay();
    }

    @Override
    public String toString(Context context) {
        return context.getString(R.string.to_string_starting_immediately);
    }
}
