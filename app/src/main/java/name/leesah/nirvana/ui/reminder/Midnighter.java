package name.leesah.nirvana.ui.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Minutes;

import static android.app.AlarmManager.RTC_WAKEUP;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.app.PendingIntent.getBroadcast;
import static android.content.Intent.ACTION_BOOT_COMPLETED;
import static java.lang.String.format;
import static name.leesah.nirvana.PhoneBook.nurse;
import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static org.joda.time.format.DateTimeFormat.longDateTime;

public class Midnighter extends BroadcastReceiver {

    private static final String TAG = Midnighter.class.getSimpleName();

    static final String ACTION_SET_REMINDERS = "name.leesah.nirvana:action:SET_REMINDERS";
    static final int REQUEST_CODE = 1000;
    public static final Minutes GAP = Minutes.TWO;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            Log.d(TAG, format("Awakened for [%s].", action));

            switch (action) {
                case ACTION_SET_REMINDERS:
                case ACTION_BOOT_COMPLETED:
                    setMidnightAlarm(context);
                    nurse(context).scheduleForTheRestOfToday();
                    break;
            }

            Log.d(TAG, "Going back to sleep.");
        }
    }

    public static void setMidnightAlarm(Context context) {
        DateTime trigger = today().plus(Days.ONE).toDateTimeAtStartOfDay().plus(GAP);
        context.getSystemService(AlarmManager.class).set(RTC_WAKEUP, trigger.getMillis(), midnightIntent(context));

        Bundle params = new Bundle();
        params.putString("trigger", longDateTime().print(trigger));
        FirebaseAnalytics.getInstance(context).logEvent("midnight_alarm_set", params);
        Log.d(TAG, format("Midnighter registered to run at [%s].", longDateTime().print(trigger)));
    }

    private static PendingIntent midnightIntent(Context context) {
        Intent intent = new Intent(context, Midnighter.class).setAction(ACTION_SET_REMINDERS);
        return getBroadcast(context, REQUEST_CODE, intent, FLAG_UPDATE_CURRENT);
    }

}
