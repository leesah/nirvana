package name.leesah.nirvana.ui.reminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

public class NotificationSecretary {

    private NotificationManager notificationManager;

    public NotificationSecretary(Context context) {
        notificationManager = (NotificationManager) context.getSystemService(RemindingService.NOTIFICATION_SERVICE);
    }

    public void display(int notificationId, Notification notification) {
        notificationManager.notify(RemindingService.NOTIFICATION_TAG, notificationId, notification);
    }

    public void dismiss(int notificationId) {
        notificationManager.cancel(RemindingService.NOTIFICATION_TAG, notificationId);
    }

}