package name.leesah.nirvana.model.treatment;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.ReadablePeriod;

import name.leesah.nirvana.model.treatment.repeating.TreatmentCycleRecurringStrategy;

/**
 * Created by sah on 2016-12-07.
 */
public class TreatmentBuilder {
    private LocalDate firstDay;
    private ReadablePeriod cycleLength;
    private TreatmentCycleRecurringStrategy treatmentCycleRecurringStrategy;

    public Treatment build() {
        return new Treatment(firstDay, cycleLength, treatmentCycleRecurringStrategy);
    }

    public TreatmentBuilder setFirstDay(LocalDate firstDay) {
        this.firstDay = firstDay;
        return this;
    }

    public TreatmentBuilder setTreatmentCycleRecurringStrategy(TreatmentCycleRecurringStrategy treatmentCycleRecurringStrategy) {
        this.treatmentCycleRecurringStrategy = treatmentCycleRecurringStrategy;
        return this;
    }

    public TreatmentBuilder setCycleLength(ReadablePeriod cycleLength) {
        this.cycleLength = cycleLength;
        return this;
    }

    public Treatment buildEverlastingTreatment() {
        return new Treatment(new LocalDate(0), Days.ONE, (c, date) -> new TreatmentCycle(date, date));
    }
}
