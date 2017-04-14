package name.leesah.nirvana.model.medication.repeating;

import org.joda.time.LocalDate;

import name.leesah.nirvana.model.treatment.TreatmentCycle;

/**
 * Created by sah on 2016-12-07.
 */
public class Everyday implements RepeatingModel {

    @Override
    public boolean matchesDate(TreatmentCycle currentCycle, LocalDate today) {
        return true;
    }

}
