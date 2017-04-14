package name.leesah.nirvana.model.treatment.repeating;

import org.joda.time.LocalDate;

import java.io.Serializable;

import name.leesah.nirvana.model.treatment.TreatmentCycle;

/**
 * Created by sah on 2016-12-06.
 */
public interface TreatmentCycleRepeatingModel extends Serializable {
    TreatmentCycle getCycleForDate(TreatmentCycle firstCycle, LocalDate date);
}
