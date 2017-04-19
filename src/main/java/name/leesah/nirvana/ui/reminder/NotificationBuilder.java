package name.leesah.nirvana.ui.reminder;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Icon;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.reminder.Reminder;
import name.leesah.nirvana.data.Pharmacist;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static name.leesah.nirvana.ui.reminder.RemindingService.ACTION_CONFIRM_REMINDER;
import static name.leesah.nirvana.ui.reminder.RemindingService.ACTION_SNOOZE_REMINDER;
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
        Medication medication = Pharmacist.getInstance(context).getMedication(reminder.getMedicationId());

        final Resources res = context.getResources();

        final String title = res.getString(R.string.reminder_notification_title_template, medication.getName());
        final String text = res.getString(R.string.reminder_notification_placeholder_text_template, reminder.getDosageAmount(), reminder.getTime().toString("HH:mm"));

        setDefaults(Notification.DEFAULT_ALL);
        setSmallIcon(getNotificationSmallIcon());
        setLargeIcon(getNotificationLargeIcon());
        setContentTitle(title);
        setContentText(text);
        setContentIntent(getShowDetailsIntent());
        setPriority(Notification.PRIORITY_DEFAULT);
        setTicker(title);
        setWhen(reminder.getTime().toDateTimeToday().getMillis());
        setAutoCancel(true);
        setOngoing(true);
        addAction(getSnoozeAction(context));
        addAction(getConfirmAction(context));
//        setStyle(new Notification.BigTextStyle()
//                .bigText(text)
//                .setBigContentTitle(title)
//                .setSummaryText("Dummy summary text"));

    }

    private Notification.Action getConfirmAction(Context context) {
        return new Notification.Action.Builder(getConfirmIcon(), context.getString(R.string.action_confirm_reminder), getConfirmIntent()).build();
    }

    private Notification.Action getSnoozeAction(Context context) {
        return new Notification.Action.Builder(getSnoozeIcon(), context.getString(R.string.action_snooze_reminder), getSnoozeIntent()).build();
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

    private PendingIntent getSnoozeIntent() {
        Intent intent = new Intent(context, RemindingService.class)
                .setAction(ACTION_SNOOZE_REMINDER)
                .putExtra(RemindingService.EXTRA_REMINDER_ID, reminderId);
        return PendingIntent.getService(context, uniqueInt(), intent, FLAG_UPDATE_CURRENT);
    }

    private Icon getNotificationSmallIcon() {
        return Icon.createWithResource(context, R.drawable.ic_check_white);
    }

    private Icon getNotificationLargeIcon() {
        return Icon.createWithResource(context, R.drawable.ic_schedule_black);
    }

    private Icon getConfirmIcon() {
        return Icon.createWithResource(context, R.drawable.ic_check_black);
    }

    private Icon getSnoozeIcon() {
        return Icon.createWithResource(context, R.drawable.ic_snooze_black);
    }

}
