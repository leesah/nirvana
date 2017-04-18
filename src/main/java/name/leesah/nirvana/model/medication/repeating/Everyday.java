package name.leesah.nirvana.model.medication.repeating;

import android.content.Context;

import org.joda.time.LocalDate;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.treatment.TreatmentCycle;

/**
 * Created by sah on 2016-12-07.
 */
public class Everyday implements RepeatingModel {

    @Override
    public boolean matchesDate(TreatmentCycle currentCycle, LocalDate today) {
        return true;
    }

    @Override
    public String toString(Context context) {
        return context.getString(R.string.to_string_everyday);
    }

}
