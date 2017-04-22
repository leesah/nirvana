package name.leesah.nirvana.model.treatment.recurring;

import org.joda.time.LocalDate;

import java.io.Serializable;

import name.leesah.nirvana.model.treatment.TreatmentCycle;

/**
 * Created by sah on 2016-12-06.
 */
public interface RecurringStrategy extends Serializable {
    TreatmentCycle getCycleForDate(TreatmentCycle firstCycle, LocalDate date);
}
