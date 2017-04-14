package name.leesah.nirvana.ui.reminder;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.joda.time.LocalDate;

import java.util.EnumSet;

import name.leesah.nirvana.model.reminder.Reminder;
import name.leesah.nirvana.data.Nurse;
import name.leesah.nirvana.utils.IdentityHelper;

import static java.util.Collections.singleton;
import static java.util.stream.Collectors.joining;
import static name.leesah.nirvana.model.reminder.Reminder.State.NOTIFIED;
import static name.leesah.nirvana.model.reminder.Reminder.State.PLANNED;
import static name.leesah.nirvana.model.reminder.Reminder.State.SNOOZED;
import static name.leesah.nirvana.utils.DateTimeHelper.toText;
import static name.leesah.nirvana.utils.DateTimeHelper.today;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 */
public class RemindingService extends IntentService {

    private static final String TAG = RemindingService.class.getSimpleName();

    static final String ACTION_SHOW_REMINDER = "name.leesah.nirvana.ui.action.SHOW_REMINDER";
    static final String ACTION_SNOOZE_REMINDER = "name.leesah.nirvana.ui.action.SNOOZE_REMINDER";
    static final String ACTION_CONFIRM_REMINDER = "name.leesah.nirvana.ui.action.CONFIRM_REMINDER";

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
        Reminder reminder = getValidatedReminder(reminderId, today(), EnumSet.of(PLANNED, SNOOZED));

        int notificationId = IdentityHelper.uniqueInt();
        Notification notification = new NotificationBuilder(this, reminder).build();
        notificationSecretary.display(notificationId, notification);
        nurse.setNotified(reminderId, notificationId);

        Log.d(TAG, String.format("Reminder shown: [%s]", reminder));
    }

    private void handleActionSnoozeReminder(int reminderId) {
        final Reminder reminder = getValidatedReminder(reminderId, today(), EnumSet.of(NOTIFIED));

        notificationSecretary.dismiss(reminder.getNotificationId());

        Reminder snoozed = reminder.snooze(SNOOZE_FOR_MINUTES);
        alarmSecretary.setAlarm(snoozed);
        nurse.replace(r -> r.getId() == reminderId, singleton(snoozed));

        showToast(String.format("Snoozed for %d minutes.", SNOOZE_FOR_MINUTES));
        Log.d(TAG, String.format("Reminder snoozed [%s].", reminder));
    }

    private void handleActionConfirmReminder(int id) {
        final Reminder reminder = getValidatedReminder(id, today(), EnumSet.of(NOTIFIED));

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

    private Reminder getValidatedReminder(int id, LocalDate expectedDate, EnumSet<Reminder.State> expectedStates) {
        final Reminder reminder = nurse.getReminder(id);

        if (reminder == null) {
            String message = String.format("Reminder ID [%d] doesn't exist.", id);
            Log.wtf(TAG, message);
            throw new IllegalStateException(message);
        }

        if (!expectedDate.equals(reminder.getDate())
                || !expectedStates.contains(reminder.getState())) {
            String message = String.format("Reminder [%d] is expected to be [%s] on [%s], but actually [%s] on [%s].", id
                    , expectedStates.stream().map(Enum::name).collect(joining(", ")), toText(expectedDate)
                    , reminder.getState().name(), toText(reminder.getDate()));
            Log.wtf(TAG, message);
            throw new IllegalStateException(message);
        }

        return reminder;
    }

    private void showToast(final String text) {
        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show());
    }

}
