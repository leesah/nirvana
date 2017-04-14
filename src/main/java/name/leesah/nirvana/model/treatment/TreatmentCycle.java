package name.leesah.nirvana.model.treatment;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import static org.joda.time.Days.daysBetween;

/**
 * Created by sah on 2016-12-03.
 */
public final class TreatmentCycle implements Cloneable {
    private final LocalDate firstDay;
    private final LocalDate lastDay;

    public TreatmentCycle(LocalDate firstDay, LocalDate lastDay) {
        this.firstDay = new LocalDate(firstDay);
        this.lastDay = new LocalDate(lastDay);
    }

    public TreatmentCycle(TreatmentCycle cycle) {
        this(new LocalDate(cycle.firstDay), new LocalDate(cycle.lastDay));
    }

    public LocalDate getFirstDay() {
        return firstDay;
    }

    public LocalDate getLastDay() {
        return lastDay;
    }

    public boolean contains(LocalDate date) {
        return !(firstDay.isAfter(date) || lastDay.isBefore(date));
    }

    public Days length(){
        return daysBetween(firstDay, lastDay).plus(Days.ONE);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TreatmentCycle)) return false;
        TreatmentCycle that = (TreatmentCycle) obj;
        return this.firstDay.equals(that.firstDay) && this.lastDay.equals(that.lastDay);
    }

    @Override
    public String toString() {
        return "[" + firstDay.toString() + ", " + lastDay.toString() + "] (" + daysBetween(firstDay, lastDay).getDays() + " days)";
    }

    public static TreatmentCycle makeNext(TreatmentCycle cycle) {
        LocalDate firstDay = cycle.lastDay.plus(Days.ONE);
        return new TreatmentCycle(firstDay, firstDay.plus(daysBetween(cycle.firstDay, cycle.lastDay)));
    }
}
