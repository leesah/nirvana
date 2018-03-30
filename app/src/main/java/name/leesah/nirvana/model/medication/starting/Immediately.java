package name.leesah.nirvana.model.medication.starting;

import android.content.Context;

import org.joda.time.LocalDate;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.treatment.Treatment;

/**
 * Created by sah on 2017-04-21.
 */

public class Immediately implements StartingStrategy {

    @Override
    public LocalDate getRealStartDate(Treatment treatment, LocalDate date) {
        return treatment.getStartDateOf(date);
    }

    @Override
    public String toString(Context context) {
        return context.getString(R.string.to_string_starting_immediately);
    }
}
