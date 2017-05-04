package name.leesah.nirvana.model.treatment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

/**
 * Created by sah on 2017-04-29.
 */

public class EverlastingTreatment implements Treatment {


    private final LocalDate dayZero;

    public EverlastingTreatment(@NonNull LocalDate dayZero) {
        this.dayZero = dayZero;
    }

    @Override
    public boolean contains(@NonNull LocalDate date) {
        return !date.isBefore(dayZero);
    }

    @Nullable
    @Override
    public LocalDate getStartDateOf(@NonNull LocalDate date) {
        return dayZero;
    }

    public LocalDate getDayZero() {
        return dayZero;
    }
}
