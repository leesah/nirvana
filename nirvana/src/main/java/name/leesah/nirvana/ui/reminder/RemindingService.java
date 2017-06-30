package name.leesah.nirvana.ui.reminder;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.joda.time.DateTime;

import name.leesah.nirvana.model.reminder.Reminder;

import static java.util.Collections.singleton;
import static java.util.Locale.US;
import static name.leesah.nirvana.PhoneBook.alarmSecretary;
import static name.leesah.nirvana.PhoneBook.nurse;
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
    public static final String NOTIFICATION_TAG = "name.leesah.nirvana.ui.notification.REMINDER";

    public RemindingService() {
        super(RemindingService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            Log.d(TAG, String.format("Awakened for [%s].", action));

            int reminderId = extractReminderId(intent);
            Reminder reminder = nurse(this).getReminder(reminderId);
            if (reminder == null) {
                Log.w(TAG, "Reminder has expired.");
                return;
            }

            switch (action) {
                case ACTION_SHOW_REMINDER:
                    handleActionShowReminder(reminder);
                    break;
                case ACTION_CONFIRM_REMINDER:
                    handleActionConfirmReminder(reminder);
                    break;
                case ACTION_SNOOZE_REMINDER:
                    handleActionSnoozeReminder(reminder);
                    break;
            }

            Log.d(TAG, "Going back to sleep.");
        }
    }

    private void handleActionShowReminder(Reminder reminder) {
        int notificationId = uniqueInt();
        Notification notification = new NotificationBuilder(this, reminder).build();
        display(notificationId, notification);
        nurse(this).setNotified(reminder.getId(), notificationId);

        Log.d(TAG, String.format("Reminder shown: [%s]", reminder));
    }

    private void handleActionSnoozeReminder(Reminder reminder) {
        DateTime snoozeTo = DateTime.now().plusMinutes(SNOOZE_FOR_MINUTES);
        dismiss(reminder.getNotificationId());

        Reminder snoozed = reminder.snooze(snoozeTo);
        alarmSecretary(this).setAlarm(snoozed);
        nurse(this).replace(r -> r.getId() == reminder.getId(), singleton(snoozed));

        showToast(String.format(US, "Snoozed until %s.", snoozeTo));
        Log.d(TAG, String.format("Reminder snoozed [%s].", reminder));
    }

    private void handleActionConfirmReminder(Reminder reminder) {
        dismiss(reminder.getNotificationId());
        nurse(this).setDone(reminder.getId());

        Log.d(TAG, String.format("Reminder confirmed: [%s].", reminder));
    }

    private int extractReminderId(Intent intent) {
        if (!intent.hasExtra(EXTRA_REMINDER_ID)) {
            Log.wtf(TAG, "Reminder ID missing from Intent");
            throw new IllegalStateException("Reminder ID missing from Intent");
        }
        return intent.getIntExtra(EXTRA_REMINDER_ID, Integer.MIN_VALUE);
    }

    public void display(int notificationId, Notification notification) {
        getSystemService(NotificationManager.class).notify(NOTIFICATION_TAG, notificationId, notification);
    }

    public void dismiss(int notificationId) {
        getSystemService(NotificationManager.class).cancel(NOTIFICATION_TAG, notificationId);
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
