package name.leesah.nirvana.ui.reminder;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowNotificationManager;

import name.leesah.nirvana.model.reminder.Reminder;
import name.leesah.nirvana.persistence.Nurse;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static name.leesah.nirvana.LanternGenie.everythingVanishes;
import static name.leesah.nirvana.LanternGenie.hire;
import static name.leesah.nirvana.LanternGenie.randomReminder;
import static name.leesah.nirvana.PhoneBook.nurse;
import static name.leesah.nirvana.ui.reminder.BellRinger.ACTION_CONFIRM_REMINDER;
import static name.leesah.nirvana.ui.reminder.BellRinger.ACTION_SHOW_REMINDER;
import static name.leesah.nirvana.ui.reminder.BellRinger.EXTRA_REMINDER_ID;
import static name.leesah.nirvana.ui.reminder.BellRinger.NOTIFICATION_TAG;
import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.RuntimeEnvironment.application;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by sah on 2017-04-07.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = {24, 25, 26})
public class BellRingerTest {

    private Context context;
    private Nurse nurse;
    private Reminder reminder;

    @Before
    public void setUp() throws Exception {
        context = application.getApplicationContext();

        nurse = spy(new Nurse(context));
        hire(nurse);
        reminder = randomReminder(context, true, today());
    }

    @After
    public void tearDown() throws Exception {
        everythingVanishes(context);
    }

    @Test
    public void showReminders() throws Exception {
        show(reminder);

        ArgumentCaptor<Integer> notificationIdCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(nurse).getReminder(reminder.getId());
        verify(nurse).setNotified(eq(reminder.getId()), notificationIdCaptor.capture());

        ShadowNotificationManager notificationManager = shadowOf(context.getSystemService(NotificationManager.class));
        assertThat(notificationManager.getNotification(NOTIFICATION_TAG, notificationIdCaptor.getValue()), is(notNullValue()));
    }

    @Test
    public void confirmReminders() throws Exception {
        show(reminder);
        int notificationId = nurse(context).getReminder(reminder.getId()).getNotificationId();

        confirm(reminder);

        verify(nurse).setDone(reminder.getId());
        assertThat(shadowOf(context.getSystemService(NotificationManager.class)).getNotification(NOTIFICATION_TAG, notificationId), is(nullValue()));
    }

    private void show(Reminder reminder) {
        Intent intent = new Intent(context, BellRinger.class)
                .setAction(ACTION_SHOW_REMINDER)
                .putExtra(EXTRA_REMINDER_ID, reminder.getId());
        new BellRingerWrapper().startWithIntent(intent);
    }

    private void confirm(Reminder reminder) {
        Intent intent = new Intent(context, Midnighter.class)
                .setAction(ACTION_CONFIRM_REMINDER)
                .putExtra(EXTRA_REMINDER_ID, reminder.getId());
        new BellRingerWrapper().startWithIntent(intent);
    }

    private class BellRingerWrapper extends BellRinger {
        private void startWithIntent(Intent intent) {
            onReceive(context, intent);
        }
    }
}