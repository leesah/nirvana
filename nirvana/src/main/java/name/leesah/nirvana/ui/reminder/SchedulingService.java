package name.leesah.nirvana.ui.reminder;

import android.app.AlarmManager;
import android.app.IntentService;
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
import static android.app.PendingIntent.getService;
import static android.content.Intent.ACTION_BOOT_COMPLETED;
import static java.lang.String.format;
import static java.util.Locale.US;
import static name.leesah.nirvana.PhoneBook.alarmSecretary;
import static name.leesah.nirvana.PhoneBook.nurse;
import static name.leesah.nirvana.PhoneBook.reminderMaker;
import static name.leesah.nirvana.persistence.Nurse.isSeenByNurse;
import static name.leesah.nirvana.persistence.Nurse.isUpcoming;
import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static org.joda.time.DateTime.now;
import static org.joda.time.format.DateTimeFormat.longDateTime;

public class SchedulingService extends IntentService {

    private static final String TAG = SchedulingService.class.getSimpleName();

    static final String ACTION_SET_REMINDERS = "name.leesah.nirvana:action:SET_REMINDERS";
    static final int REQUEST_CODE = 1000;
    public static final Minutes GAP = Minutes.THREE;

    public SchedulingService() {
        super(SchedulingService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            Log.d(TAG, format("Awakened for [%s].", action));

            switch (action) {
                case ACTION_SET_REMINDERS:
                    midnightTasks(this);
                    break;
            }

            Log.d(TAG, "Going back to sleep.");
        }
    }

    private static void midnightTasks(Context context) {
        setMidnightAlarm(context);
        scheduleForTheRestOfToday(context);
    }

    static void setMidnightAlarm(Context context) {

        DateTime trigger = today().plus(Days.ONE).toDateTimeAtStartOfDay().plus(GAP);
        context.getSystemService(AlarmManager.class).set(
                RTC_WAKEUP,
                trigger.getMillis(),
                getService(context, REQUEST_CODE, buildMidnightIntent(context), FLAG_UPDATE_CURRENT));

        Bundle params = new Bundle();
        params.putString("trigger", longDateTime().print(trigger));
        FirebaseAnalytics.getInstance(context).logEvent("midnight_alarm_set", params);
        Log.d(TAG, format("SchedulingService registered to run at [%s].", longDateTime().print(trigger)));
    }

    public static void scheduleForTheRestOfToday(Context context) {
        reminderMaker(context).createReminders(today()).stream()
                .filter(isSeenByNurse(context).negate().and(isUpcoming(now())))
                .forEach(reminder -> {
                    alarmSecretary(context).setAlarm(reminder);
                    nurse(context).add(reminder);
                    Log.d(TAG, format(US, "Scheduled new reminder: %s", reminder));
                });
    }

    private static Intent buildMidnightIntent(Context context) {
        return new Intent(context, SchedulingService.class).setAction(ACTION_SET_REMINDERS);
    }

    public static class BootCompletedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_BOOT_COMPLETED))
                FirebaseAnalytics.getInstance(context).logEvent("boot_completed", new Bundle());
                midnightTasks(context);
        }
    }
}
