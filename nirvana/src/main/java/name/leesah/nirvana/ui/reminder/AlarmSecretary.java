package name.leesah.nirvana.ui.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.joda.time.DateTime;

import name.leesah.nirvana.model.reminder.Reminder;

import static android.app.AlarmManager.RTC_WAKEUP;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static name.leesah.nirvana.ui.reminder.RemindingService.ACTION_SHOW_REMINDER;
import static name.leesah.nirvana.ui.reminder.RemindingService.EXTRA_REMINDER_ID;
import static name.leesah.nirvana.utils.IdentityHelper.uniqueInt;

public class AlarmSecretary {
    private static final String TAG = AlarmSecretary.class.getSimpleName();

    private AlarmManager alarmManager;

    private final Context context;

    public AlarmSecretary(Context context) {
        this.context = context;
        alarmManager = context.getSystemService(AlarmManager.class);
    }

    public void setAlarm(Reminder reminder) {
        Log.d(TAG, String.format("Setting alarm for reminder: [%s].", reminder));

        DateTime trigger = reminder.getDate().toDateTime(reminder.getTime());
        alarmManager.set(RTC_WAKEUP, trigger.getMillis(), buildIntent(reminder, context));

        Log.i(TAG, String.format("Reminder has been set for medication: [%d].", reminder.getMedicationId()));
    }

    private PendingIntent buildIntent(Reminder reminder, Context context) {
        Intent intent = new Intent(context, RemindingService.class)
                .setAction(ACTION_SHOW_REMINDER)
                .putExtra(EXTRA_REMINDER_ID, reminder.getId());
        return PendingIntent.getService(context, uniqueInt(), intent, FLAG_UPDATE_CURRENT);
    }
}