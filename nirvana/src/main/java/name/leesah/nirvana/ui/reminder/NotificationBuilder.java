package name.leesah.nirvana.ui.reminder;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Icon;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.DosageForm;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.reminder.Reminder;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static name.leesah.nirvana.PhoneBook.pharmacist;
import static name.leesah.nirvana.ui.reminder.RemindingService.ACTION_CONFIRM_REMINDER;
import static name.leesah.nirvana.utils.DateTimeHelper.toText;
import static name.leesah.nirvana.utils.IdentityHelper.uniqueInt;

/**
 * Created by sah on 2016-12-09.
 */

class NotificationBuilder extends Notification.Builder {

    private final Context context;
    private final int reminderId;

    NotificationBuilder(Context context, Reminder reminder) {
        super(context);

        this.context = context;
        this.reminderId = reminder.getId();
        Medication medication = pharmacist(context).getMedication(reminder.getMedicationId());

        final Resources res = context.getResources();

        final String title = res.getString(R.string.notification_title, medication.getName());
        final String text = res.getQuantityString(R.plurals.notification_text, reminder.getDosageAmount(), reminder.getDosageAmount(), toText(reminder.getTime()));

        setDefaults(Notification.DEFAULT_ALL);
        setSmallIcon(getNotificationIcon(medication.getForm()));
        setContentTitle(title);
        setContentText(text);
        setContentIntent(getShowDetailsIntent());
        setPriority(Notification.PRIORITY_DEFAULT);
        setTicker(title);
        setWhen(reminder.getTime().toDateTimeToday().getMillis());
        setAutoCancel(false);
        setOngoing(true);
        addAction(getConfirmAction(context));
    }

    private Notification.Action getConfirmAction(Context context) {
        return new Notification.Action.Builder(getConfirmIcon(), context.getString(R.string.done), getConfirmIntent()).build();
    }

    private PendingIntent getShowDetailsIntent() {
        Intent intent = new Intent(context, ReminderDetailsActivity.class)
                .setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK)
                .putExtra(ReminderDetailsActivity.EXTRA_REMINDER_ID, reminderId);
        return PendingIntent.getActivity(context, uniqueInt(), intent, FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getConfirmIntent() {
        Intent intent = new Intent(context, RemindingService.class)
                .setAction(ACTION_CONFIRM_REMINDER)
                .putExtra(RemindingService.EXTRA_REMINDER_ID, reminderId);
        return PendingIntent.getService(context, uniqueInt(), intent, FLAG_UPDATE_CURRENT);
    }

    private Icon getNotificationIcon(DosageForm form) {
        switch (form) {
            case TABLET:
                return Icon.createWithResource(context, R.drawable.ic_tablet);
            case CAPSULE:
                return Icon.createWithResource(context, R.drawable.ic_capsule);
            default:
                return Icon.createWithResource(context, R.drawable.ic_reminder_status_active);
        }
    }

    private Icon getConfirmIcon() {
        return Icon.createWithResource(context, R.drawable.ic_reminder_status_done);
    }

}
