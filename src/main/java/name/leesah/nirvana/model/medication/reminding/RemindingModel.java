package name.leesah.nirvana.model.medication.reminding;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Set;

import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.reminder.Reminder;

/**
 * Created by sah on 2016-12-11.
 */
public interface RemindingModel {

    @NonNull
    Set<Reminder> getRemindersThroughDay(Medication medication);

    String toString(Context context);

}
