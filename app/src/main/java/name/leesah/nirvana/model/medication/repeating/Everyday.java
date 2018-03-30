package name.leesah.nirvana.model.medication.repeating;

import android.content.Context;

import org.joda.time.LocalDate;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.starting.StartingStrategy;
import name.leesah.nirvana.model.treatment.Treatment;

/**
 * Created by sah on 2016-12-07.
 */
public class Everyday implements RepeatingStrategy {

    @Override
    public boolean matches(Treatment treatment, StartingStrategy startingStrategy, LocalDate date) {
        return treatment.contains(date);
    }

    @Override
    public String toString(Context context) {
        return context.getString(R.string.everyday);
    }

}
