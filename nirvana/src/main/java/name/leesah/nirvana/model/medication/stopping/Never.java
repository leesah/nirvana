package name.leesah.nirvana.model.medication.stopping;

import android.content.Context;

import org.joda.time.LocalDate;

import name.leesah.nirvana.R;
import name.leesah.nirvana.data.Therapist;
import name.leesah.nirvana.model.treatment.Treatment;

/**
 * Created by sah on 2017-04-27.
 */

public class Never implements StoppingStrategy {

    @Override
    public boolean hasStopped(Treatment treatment, LocalDate date) {
        return !treatment.contains(date);
    }

    @Override
    public String toString(Context context) {
        return Therapist.getInstance(context).isCycleSupportEnabled() ?
                context.getString(R.string.stopping_until_cycle_ends) :
                context.getString(R.string.stopping_never);
    }
}
