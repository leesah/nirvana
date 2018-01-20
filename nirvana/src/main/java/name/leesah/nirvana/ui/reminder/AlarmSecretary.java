package name.leesah.nirvana.ui.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.joda.time.DateTime;

import name.leesah.nirvana.model.reminder.Reminder;

import static android.app.AlarmManager.RTC_WAKEUP;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.app.PendingIntent.getBroadcast;
import static java.lang.String.format;
import static java.util.Locale.getDefault;
import static name.leesah.nirvana.ui.reminder.BellRinger.ACTION_SHOW_REMINDER;
import static name.leesah.nirvana.ui.reminder.BellRinger.EXTRA_REMINDER_ID;
import static name.leesah.nirvana.utils.IdentityHelper.uniqueInt;
import static org.joda.time.format.DateTimeFormat.longDateTime;

public class AlarmSecretary {
    private static final String TAG = AlarmSecretary.class.getSimpleName();
    private static final String REMINDER_RUI_TEMPLATE = "https://nirvana.leesah.name/reminder/%d";

    private AlarmManager alarmManager;

    private final Context context;

    public AlarmSecretary(Context context) {
        this.context = context;
        this.alarmManager = context.getSystemService(AlarmManager.class);
    }

    public void setAlarm(Reminder reminder) {
        Log.d(TAG, format("Setting alarm for reminder: [%s].", reminder));

        DateTime trigger = reminder.getDate().toDateTime(reminder.getTime());
        alarmManager.set(RTC_WAKEUP, trigger.getMillis(), buildIntent(reminder, context));

        Bundle params = new Bundle();
        params.putInt("reminder_id", reminder.getId());
        params.putString("trigger", longDateTime().print(trigger));
        FirebaseAnalytics.getInstance(context).logEvent("reminder_alarm_set", params);
        Log.i(TAG, format("Reminder has been set for medication: [%d].", reminder.getMedicationId()));
    }

    private PendingIntent buildIntent(Reminder reminder, Context context) {
        Intent intent = new Intent(context, BellRinger.class)
                .setData(uriOf(reminder))
                .setAction(ACTION_SHOW_REMINDER)
                .putExtra(EXTRA_REMINDER_ID, reminder.getId());
        return getBroadcast(context, uniqueInt(), intent, FLAG_UPDATE_CURRENT);
    }

    private Uri uriOf(Reminder reminder) {
        return Uri.parse(format(getDefault(), REMINDER_RUI_TEMPLATE, reminder.getId()));
    }
}