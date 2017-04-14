package name.leesah.nirvana.ui.reminder;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.joda.time.LocalDate;

import java.util.Set;

import name.leesah.nirvana.model.reminder.Reminder;
import name.leesah.nirvana.model.reminder.ReminderFactory;
import name.leesah.nirvana.data.Nurse;

import static android.app.AlarmManager.RTC_WAKEUP;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static name.leesah.nirvana.utils.DateTimeHelper.toDate;
import static name.leesah.nirvana.utils.DateTimeHelper.toText;
import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static name.leesah.nirvana.utils.DateTimeHelper.todayAsString;
import static org.joda.time.Days.ONE;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class SchedulingService extends IntentService {

    private static final String TAG = SchedulingService.class.getSimpleName();

    private static final String ACTION_SET_REMINDERS = "name.leesah.nirvana.ui.action.SET_REMINDERS";
    private static final int REQUEST_CODE_SET_REMINDERS = 1000;
    private static final String PREF_KEY_MIDNIGHT_ALARM_LATEST_RUN = "SCHEDULING SERVICE LATEST RUN";

    public SchedulingService() {
        super("SchedulingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            Log.d(TAG, String.format("Awakened for [%s].", action));

            switch (action) {
                case ACTION_SET_REMINDERS:
                    handleMidnightAlarm(this);
                    break;
            }

            Log.d(TAG, "Going back to sleep.");
        }
    }

    private static void handleMidnightAlarm(Context context) {
        LocalDate today = today();
        SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(context);

        String latestRun = sharedPreference.getString(PREF_KEY_MIDNIGHT_ALARM_LATEST_RUN, null);
        if (latestRun != null && toDate(latestRun).equals(today)) {
            Log.d(TAG, "SchedulingService was run today.");
            return;
        }

        setReminderAlarms(context, today);
        setMidnightAlarm(context, today);

        sharedPreference.edit().putString(PREF_KEY_MIDNIGHT_ALARM_LATEST_RUN, todayAsString()).apply();
    }

    private static void setReminderAlarms(Context context, LocalDate date) {
        Set<Reminder> reminders = new ReminderFactory(context).createReminders(date);
        reminders.forEach(reminder -> AlarmSecretary.getInstance(context).setAlarm(reminder));
        Nurse.getInstance(context).add(reminders);

        Log.i(TAG, String.format("%d reminders(s) set.", reminders.size()));
        reminders.forEach(reminder -> Log.d(TAG, reminder.toString()));
    }

    private static void setMidnightAlarm(Context context, LocalDate date) {
        LocalDate tomorrow = date.plus(ONE);
        Log.i(TAG, String.format("Registering SchedulingService for a run on [%s].", toText(tomorrow)));

        long millis = tomorrow.toDateTimeAtStartOfDay().getMillis();
        PendingIntent pendingIntent = buildPendingIntentForMidnightAlarm(context);
        ((AlarmManager) context.getSystemService(ALARM_SERVICE)).set(RTC_WAKEUP, millis, pendingIntent);
        Log.d(TAG, String.format("SchedulingService registered to run at [%d] millis.", millis));
    }

    private static PendingIntent buildPendingIntentForMidnightAlarm(Context context) {
        Intent intent = new Intent(context, SchedulingService.class).setAction(ACTION_SET_REMINDERS);
        return PendingIntent.getService(context, REQUEST_CODE_SET_REMINDERS, intent, FLAG_UPDATE_CURRENT);
    }

    public static void inCaseNotRunToday(Context context) {
        handleMidnightAlarm(context);
    }

    public static class BootCompletedReceiver extends BroadcastReceiver {

        private static final String TAG = BootCompletedReceiver.class.getSimpleName();

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, String.format("Received broadcast: [%s].", intent.getAction()));

            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                inCaseNotRunToday(context);
            }
        }
    }
}
