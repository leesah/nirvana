package name.leesah.nirvana.model.treatment;

import android.text.TextUtils;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.ReadablePeriod;

import name.leesah.nirvana.model.treatment.repeating.TreatmentCycleRepeatingModel;

import static java.lang.String.format;

/**
 * Created by sah on 2016-12-03.
 */

public class Treatment {

    private LocalDate firstDay;
    private final ReadablePeriod cycleLength;
    private TreatmentCycleRepeatingModel repeatingModel;

    Treatment(LocalDate firstDay, ReadablePeriod cycleLength, TreatmentCycleRepeatingModel repeatingModel) {
        this.firstDay = firstDay;
        this.cycleLength = cycleLength;
        this.repeatingModel = repeatingModel;
    }

    public TreatmentCycleRepeatingModel getRepeatingModel() {
        return repeatingModel;
    }

    public TreatmentCycle getFirstCycle() {
        return new TreatmentCycle(firstDay, firstDay.plus(cycleLength).minus(Days.ONE));
    }

    @Override
    public String toString() {
        return format("Treatment [%s], [%s]", getFirstCycle().toString(), StringUtils.lowerCase(repeatingModel.toString()));
    }
}
