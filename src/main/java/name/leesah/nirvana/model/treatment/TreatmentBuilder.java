package name.leesah.nirvana.model.treatment;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.ReadablePeriod;

import name.leesah.nirvana.model.treatment.repeating.TreatmentCycleRepeatingModel;

/**
 * Created by sah on 2016-12-07.
 */
public class TreatmentBuilder {
    private LocalDate firstDay;
    private ReadablePeriod cycleLength;
    private TreatmentCycleRepeatingModel treatmentCycleRepeatingModel;

    public Treatment build() {
        return new Treatment(firstDay, cycleLength, treatmentCycleRepeatingModel);
    }

    public TreatmentBuilder setFirstDay(LocalDate firstDay) {
        this.firstDay = firstDay;
        return this;
    }

    public TreatmentBuilder setTreatmentCycleRepeatingModel(TreatmentCycleRepeatingModel treatmentCycleRepeatingModel) {
        this.treatmentCycleRepeatingModel = treatmentCycleRepeatingModel;
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
