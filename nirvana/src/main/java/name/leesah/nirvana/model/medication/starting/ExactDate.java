package name.leesah.nirvana.model.medication.starting;

import android.content.Context;

import org.joda.time.LocalDate;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExactDate exactDate = (ExactDate) o;
        return Objects.equals(startDate, exactDate.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate);
    }

    @Override
    public String toString(Context context) {
        return null;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
}
