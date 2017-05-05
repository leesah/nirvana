package name.leesah.nirvana.model.medication.starting;

import android.content.Context;

import org.joda.time.LocalDate;

import java.util.Objects;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.treatment.Treatment;

import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static org.joda.time.format.DateTimeFormat.fullDate;

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
        return startDate.equals(today()) ?
                context.getString(R.string.today) :
                startDate.equals(today().plusDays(1)) ?
                        context.getString(R.string.tomorrow) :
                        fullDate().print(startDate);
    }

    public LocalDate getStartDate() {
        return startDate;
    }
}
