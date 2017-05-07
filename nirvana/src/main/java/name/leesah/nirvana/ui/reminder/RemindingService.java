package name.leesah.nirvana.ui.reminder;

import android.app.IntentService;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import name.leesah.nirvana.data.Nurse;
import name.leesah.nirvana.model.reminder.Reminder;

import static java.util.Collections.singleton;
import static java.util.Locale.US;
import static name.leesah.nirvana.utils.IdentityHelper.uniqueInt;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 */
public class RemindingService extends IntentService {

    private static final String TAG = RemindingService.class.getSimpleName();

    public static final String ACTION_SHOW_REMINDER = "name.leesah.nirvana:action:SHOW_REMINDER";
    static final String ACTION_SNOOZE_REMINDER = "name.leesah.nirvana:action:SNOOZE_REMINDER";
    static final String ACTION_CONFIRM_REMINDER = "name.leesah.nirvana:action:CONFIRM_REMINDER";

    static final String EXTRA_REMINDER_ID = "name.leesah.nirvana.ui.extra.REMINDER";

    static final int SNOOZE_FOR_MINUTES = 15;
    static final String NOTIFICATION_TAG = "name.leesah.nirvana.ui.notification.REMINDER";

    private NotificationSecretary notificationSecretary;
    private AlarmSecretary alarmSecretary;
    private Nurse nurse;

    public RemindingService() {
        super("RemindingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        notificationSecretary = NotificationSecretary.getInstance(this);
        alarmSecretary = AlarmSecretary.getInstance(this);
        nurse = Nurse.getInstance(this);

        if (intent != null) {
            final String action = intent.getAction();
            Log.d(TAG, String.format("Awakened for [%s].", action));

            switch (action) {
                case ACTION_SHOW_REMINDER:
                    handleActionShowReminder(extractReminderId(intent));
                    break;
                case ACTION_CONFIRM_REMINDER:
                    handleActionConfirmReminder(extractReminderId(intent));
                    break;
                case ACTION_SNOOZE_REMINDER:
                    handleActionSnoozeReminder(extractReminderId(intent));
                    break;
            }

            Log.d(TAG, "Going back to sleep.");
        }
    }

    private void handleActionShowReminder(int reminderId) {
        Reminder reminder = nurse.getReminder(reminderId);
        if (reminder == null) {
            Log.w(TAG, "Reminder has expired.");
            return;
        }

        int notificationId = uniqueInt();
        Notification notification = new NotificationBuilder(this, reminder).build();
        notificationSecretary.display(notificationId, notification);
        nurse.setNotified(reminderId, notificationId);

        Log.d(TAG, String.format("Reminder shown: [%s]", reminder));
    }

    private void handleActionSnoozeReminder(int reminderId) {
        final Reminder reminder = nurse.getReminder(reminderId);
        if (reminder == null) {
            Log.w(TAG, "Reminder has expired.");
            return;
        }

        notificationSecretary.dismiss(reminder.getNotificationId());

        Reminder snoozed = reminder.snooze(SNOOZE_FOR_MINUTES);
        alarmSecretary.setAlarm(snoozed);
        nurse.replace(r -> r.getId() == reminderId, singleton(snoozed));

        showToast(String.format(US, "Snoozed for %d minutes.", SNOOZE_FOR_MINUTES));
        Log.d(TAG, String.format("Reminder snoozed [%s].", reminder));
    }

    private void handleActionConfirmReminder(int id) {
        final Reminder reminder = nurse.getReminder(id);
        if (reminder == null) {
            Log.w(TAG, "Reminder has expired.");
            return;
        }

        notificationSecretary.dismiss(reminder.getNotificationId());
        nurse.setDone(reminder.getId());

        Log.d(TAG, String.format("Reminder confirmed: [%s].", reminder));
    }

    private int extractReminderId(Intent intent) {
        if (!intent.hasExtra(EXTRA_REMINDER_ID)) {
            Log.wtf(TAG, "Reminder ID missing from Intent");
            throw new IllegalStateException("Reminder ID missing from Intent");
        }
        return intent.getIntExtra(EXTRA_REMINDER_ID, Integer.MIN_VALUE);
    }

    private void showToast(final String text) {
        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show());
    }

    public static void confirmReminder(Context context, int reminderId) {
        context.startService(
                new Intent(context, RemindingService.class)
                        .setAction(ACTION_CONFIRM_REMINDER)
                        .putExtra(EXTRA_REMINDER_ID, reminderId));
    }

    public static void snoozeReminder(Context context, int reminderId) {
        context.startService(
                new Intent(context, RemindingService.class)
                        .setAction(ACTION_SNOOZE_REMINDER)
                        .putExtra(EXTRA_REMINDER_ID, reminderId));
    }

}
