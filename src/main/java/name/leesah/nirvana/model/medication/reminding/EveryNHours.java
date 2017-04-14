package name.leesah.nirvana.model.medication.reminding;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.Set;

import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.reminder.Reminder;
import name.leesah.nirvana.model.reminder.TimedDosage;

import static java.lang.String.format;

/**
 * Created by sah on 2017-04-16.
 */

public class EveryNHours implements RemindingModel {

    private final TimedDosage firstDose;
    private final int everyN;

    public EveryNHours(TimedDosage firstDose, int everyN) {
        this.firstDose = firstDose;
        this.everyN = everyN;
    }

    @NonNull
    @Override
    public Set<Reminder> getRemindersThroughDay(Medication medication) {
        return Collections.emptySet();
    }

    @Override
    public String toString() {
        return format("%d unit(s) every %d hours, starting from %s", firstDose.getAmount(), everyN, firstDose.getTimeOfDay());
    }
}
