package name.leesah.nirvana.model.medication.starting;

import android.content.Context;

import org.joda.time.LocalDate;

import name.leesah.nirvana.model.treatment.Treatment;

/**
 * Created by sah on 2017-04-28.
 */

public class ExactDate implements StartingStrategy {

    private final LocalDate startDate;

    public ExactDate(LocalDate date) {
        this.startDate = date;
    }

    @Override
    public LocalDate getRealStartDate(Treatment treatment, LocalDate date) {
        return startDate;
    }

    @Override
    public String toString(Context context) {
        return null;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
}
