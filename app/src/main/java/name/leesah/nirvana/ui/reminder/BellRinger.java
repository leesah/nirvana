package name.leesah.nirvana.ui.reminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;

import name.leesah.nirvana.model.reminder.Reminder;

import static name.leesah.nirvana.PhoneBook.nurse;
import static name.leesah.nirvana.utils.IdentityHelper.uniqueInt;

public class BellRinger extends BroadcastReceiver {

    public static final String EXTRA_REMINDER_ID = "name.leesah.nirvana.ui.extra.REMINDER";
    public static final String ACTION_SHOW_REMINDER = "name.leesah.nirvana:action:SHOW_REMINDER";
    public static final String ACTION_CONFIRM_REMINDER = "name.leesah.nirvana:action:CONFIRM_REMINDER";
    public static final String NOTIFICATION_TAG = "name.leesah.nirvana.ui.notification.REMINDER";

    private static final String TAG = BellRinger.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            Bundle params = new Bundle();
            params.putString("intent_action", action);
            FirebaseAnalytics.getInstance(context).logEvent("reminding_service_run", params);
            Log.d(TAG, String.format("Awakened for [%s].", action));

            int reminderId = extractReminderId(intent);
            Reminder reminder = nurse(context).getReminder(reminderId);
            if (reminder == null) {
                params = new Bundle();
                params.putInt("reminder_id", reminderId);
                FirebaseAnalytics.getInstance(context).logEvent("expired_reminder_skipped", params);
                Log.w(TAG, "Reminder has expired.");
                return;
            }

            switch (action) {
                case ACTION_SHOW_REMINDER:
                    handleActionShowReminder(context, reminder);
                    break;
                case ACTION_CONFIRM_REMINDER:
                    handleActionConfirmReminder(context, reminder);
                    break;
            }

            Log.d(TAG, "Going back to sleep.");
        }
    }

    private void handleActionShowReminder(Context context, Reminder reminder) {
        int notificationId = uniqueInt();
        Notification notification = new NotificationBuilder(context, reminder).build();
        display(context, notificationId, notification);
        nurse(context).setNotified(reminder.getId(), notificationId);

        Bundle params = new Bundle();
        params.putInt("reminder_id", reminder.getId());
        params.putInt("notification_id", notificationId);
        FirebaseAnalytics.getInstance(context).logEvent("reminder_shown", params);
        Log.d(TAG, String.format("Reminder shown: [%s]", reminder));
    }

    private void handleActionConfirmReminder(Context context, Reminder reminder) {
        dismiss(context, reminder.getNotificationId());
        nurse(context).setDone(reminder.getId());

        Bundle params = new Bundle();
        params.putInt("reminder_id", reminder.getId());
        params.putInt("notification_id", reminder.getNotificationId());
        FirebaseAnalytics.getInstance(context).logEvent("reminder_confirmed", params);
        Log.d(TAG, String.format("Reminder confirmed: [%s].", reminder));
    }

    private int extractReminderId(Intent intent) {
        if (!intent.hasExtra(EXTRA_REMINDER_ID)) {
            Log.wtf(TAG, "Reminder ID missing from Intent");
            throw new IllegalStateException("Reminder ID missing from Intent");
        }
        return intent.getIntExtra(EXTRA_REMINDER_ID, Integer.MIN_VALUE);
    }

    private void display(Context context, int notificationId, Notification notification) {
        context.getSystemService(NotificationManager.class).notify(NOTIFICATION_TAG, notificationId, notification);
    }

    private void dismiss(Context context, int notificationId) {
        context.getSystemService(NotificationManager.class).cancel(NOTIFICATION_TAG, notificationId);
    }

    public static void confirmReminder(Context context, int reminderId) {
        context.sendBroadcast(
                new Intent(context, BellRinger.class)
                        .setAction(ACTION_CONFIRM_REMINDER)
                        .putExtra(EXTRA_REMINDER_ID, reminderId));
    }

}
