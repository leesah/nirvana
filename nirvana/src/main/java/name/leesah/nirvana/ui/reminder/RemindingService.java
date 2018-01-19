package name.leesah.nirvana.ui.reminder;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;

import name.leesah.nirvana.model.reminder.Reminder;

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
    static final String ACTION_CONFIRM_REMINDER = "name.leesah.nirvana:action:CONFIRM_REMINDER";

    static final String EXTRA_REMINDER_ID = "name.leesah.nirvana.ui.extra.REMINDER";

    public static final String NOTIFICATION_TAG = "name.leesah.nirvana.ui.notification.REMINDER";

    public RemindingService() {
        super(RemindingService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            Bundle params = new Bundle();
            params.putString("intent_action", action);
            FirebaseAnalytics.getInstance(this).logEvent("reminding_service_run", params);
            Log.d(TAG, String.format("Awakened for [%s].", action));

            int reminderId = extractReminderId(intent);
            Reminder reminder = nurse(this).getReminder(reminderId);
            if (reminder == null) {
                params = new Bundle();
                params.putString("reminder", reminder.toString());
                FirebaseAnalytics.getInstance(this).logEvent("expired_reminder_skipped", params);
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
            }

            Log.d(TAG, "Going back to sleep.");
        }
    }

    private void handleActionShowReminder(Reminder reminder) {
        int notificationId = uniqueInt();
        Notification notification = new NotificationBuilder(this, reminder).build();
        display(notificationId, notification);
        nurse(this).setNotified(reminder.getId(), notificationId);

        Bundle params = new Bundle();
        params.putString("reminder", reminder.toString());
        params.putInt("notification_id", notificationId);
        params.putString("notification", notification.toString());
        FirebaseAnalytics.getInstance(this).logEvent("reminder_shown", params);
        Log.d(TAG, String.format("Reminder shown: [%s]", reminder));
    }

    private void handleActionConfirmReminder(Reminder reminder) {
        dismiss(reminder.getNotificationId());
        nurse(this).setDone(reminder.getId());

        Bundle params = new Bundle();
        params.putString("reminder", reminder.toString());
        params.putInt("notification_id", reminder.getNotificationId());
        FirebaseAnalytics.getInstance(this).logEvent("reminder_confirmed", params);
        Log.d(TAG, String.format("Reminder confirmed: [%s].", reminder));
    }

    private int extractReminderId(Intent intent) {
        if (!intent.hasExtra(EXTRA_REMINDER_ID)) {
            Log.wtf(TAG, "Reminder ID missing from Intent");
            throw new IllegalStateException("Reminder ID missing from Intent");
        }
        return intent.getIntExtra(EXTRA_REMINDER_ID, Integer.MIN_VALUE);
    }

    private void display(int notificationId, Notification notification) {
        getSystemService(NotificationManager.class).notify(NOTIFICATION_TAG, notificationId, notification);
    }

    private void dismiss(int notificationId) {
        getSystemService(NotificationManager.class).cancel(NOTIFICATION_TAG, notificationId);
    }

    public static void confirmReminder(Context context, int reminderId) {
        context.startService(
                new Intent(context, RemindingService.class)
                        .setAction(ACTION_CONFIRM_REMINDER)
                        .putExtra(EXTRA_REMINDER_ID, reminderId));
    }

}
