package name.leesah.nirvana.model.medication.reminding;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.Set;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.reminder.Reminder;
import name.leesah.nirvana.model.reminder.TimedDosage;

import static java.lang.String.format;
import static name.leesah.nirvana.utils.DateTimeHelper.toText;

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
    public String toString(Context context) {
        String doses = context.getResources().getQuantityString(R.plurals.to_string_every_n_hours_doses, firstDose.getAmount(), firstDose.getAmount());
        String everyNHours = context.getResources().getQuantityString(R.plurals.to_string_every_n_hours_every_n_hours, everyN, everyN);
        return context.getString(R.string.to_string_every_n_hours, doses, everyNHours, toText(firstDose.getTimeOfDay()));
    }
}
