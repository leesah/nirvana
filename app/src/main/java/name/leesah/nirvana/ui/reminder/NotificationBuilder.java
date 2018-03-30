package name.leesah.nirvana.ui.reminder;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Icon;

import name.leesah.nirvana.model.medication.DosageForm;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.reminder.Reminder;

import static android.app.Notification.PRIORITY_DEFAULT;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.graphics.drawable.Icon.createWithResource;
import static android.os.Build.VERSION.SDK_INT;
import static name.leesah.nirvana.PhoneBook.pharmacist;
import static name.leesah.nirvana.R.drawable.ic_capsule;
import static name.leesah.nirvana.R.drawable.ic_drop;
import static name.leesah.nirvana.R.drawable.ic_injection;
import static name.leesah.nirvana.R.drawable.ic_reminder_status_active;
import static name.leesah.nirvana.R.drawable.ic_reminder_status_done;
import static name.leesah.nirvana.R.drawable.ic_tablet;
import static name.leesah.nirvana.R.plurals.notification_text;
import static name.leesah.nirvana.R.string.done;
import static name.leesah.nirvana.R.string.notification_channel_id_reminder;
import static name.leesah.nirvana.R.string.notification_title;
import static name.leesah.nirvana.ui.reminder.BellRinger.ACTION_CONFIRM_REMINDER;
import static name.leesah.nirvana.ui.reminder.BellRinger.EXTRA_REMINDER_ID;
import static name.leesah.nirvana.utils.DateTimeHelper.toText;
import static name.leesah.nirvana.utils.IdentityHelper.uniqueInt;

/**
 * Created by sah on 2016-12-09.
 */

class NotificationBuilder extends Notification.Builder {

    private final Context context;

    NotificationBuilder(Context context, Reminder reminder) {
        super(context);
        this.context = context;

        Medication medication = pharmacist(context).getMedication(reminder.getMedicationId());

        final Resources res = context.getResources();
        final String title = res.getString(notification_title, medication.getName());
        final String text = res.getQuantityString(notification_text, reminder.getDosageAmount(), reminder.getDosageAmount(), toText(reminder.getTime()));

        setDefaults(Notification.DEFAULT_ALL);
        if (SDK_INT >= 26)
            setChannelId(this.context.getString(notification_channel_id_reminder));
        setSmallIcon(getNotificationIcon(medication.getForm()));
        setContentTitle(title);
        setContentText(text);
        setContentIntent(getShowDetailsIntent(reminder));
        setPriority(PRIORITY_DEFAULT);
        setTicker(title);
        setWhen(reminder.getTime().toDateTimeToday().getMillis());
        setAutoCancel(false);
        setOngoing(true);
        addAction(getConfirmAction(context, reminder));
    }

    private Notification.Action getConfirmAction(Context context, Reminder reminder) {
        Intent intent = new Intent(this.context, BellRinger.class)
                .setAction(ACTION_CONFIRM_REMINDER)
                .setData(reminder.getUri())
                .putExtra(EXTRA_REMINDER_ID, reminder.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, uniqueInt(), intent, FLAG_UPDATE_CURRENT);
        return new Notification.Action.Builder(getConfirmIcon(), context.getString(done), pendingIntent).build();
    }

    private PendingIntent getShowDetailsIntent(Reminder reminder) {
        Intent intent = new Intent(context, ReminderDetailsActivity.class)
                .setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK)
                .setData(reminder.getUri())
                .putExtra(EXTRA_REMINDER_ID, reminder.getId());
        return PendingIntent.getActivity(context, uniqueInt(), intent, FLAG_UPDATE_CURRENT);
    }

    private Icon getNotificationIcon(DosageForm form) {
        switch (form) {
            case TABLET:
                return createWithResource(context, ic_tablet);
            case CAPSULE:
                return createWithResource(context, ic_capsule);
            case INJECTION:
                return createWithResource(context, ic_injection);
            case DROP:
                return createWithResource(context, ic_drop);
            default:
                return createWithResource(context, ic_reminder_status_active);
        }
    }

    private Icon getConfirmIcon() {
        return createWithResource(context, ic_reminder_status_done);
    }

}
