package name.leesah.nirvana.model.medication.repeating;

import org.joda.time.LocalDate;

import java.util.EnumSet;
import java.util.stream.Collectors;

import name.leesah.nirvana.model.DayOfWeek;
import name.leesah.nirvana.model.treatment.TreatmentCycle;

/**
 * Created by sah on 2016-12-07.
 */
public class DaysOfWeek implements RepeatingModel {

    private final EnumSet<DayOfWeek> daysOfWeek;

    public DaysOfWeek(EnumSet<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    @Override
    public boolean matchesDate(TreatmentCycle currentCycle, LocalDate date) {
        return daysOfWeek.stream()
                .map(DayOfWeek::getDay)
                .collect(Collectors.toList())
                .contains(date.getDayOfWeek());
    }

}
