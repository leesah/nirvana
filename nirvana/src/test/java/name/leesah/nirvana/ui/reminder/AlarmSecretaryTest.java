package name.leesah.nirvana.ui.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Set;

import name.leesah.nirvana.BuildConfig;
import name.leesah.nirvana.LanternGenie;
import name.leesah.nirvana.model.reminder.Reminder;

import static android.app.AlarmManager.RTC_WAKEUP;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.app.PendingIntent.getService;
import static name.leesah.nirvana.LanternGenie.everythingVanishesSilVousPlait;
import static name.leesah.nirvana.LanternGenie.oneRandomReminderOnAnyDaySilVousPlait;
import static name.leesah.nirvana.LanternGenie.severalRandomRemindersOnAnyDaysSilVousPlait;
import static name.leesah.nirvana.ui.reminder.RemindingService.ACTION_SHOW_REMINDER;
import static name.leesah.nirvana.utils.IdentityHelper.uniqueInt;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.RuntimeEnvironment.application;

/**
 * Created by sah on 2017-04-20.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = {24, 25})
public class AlarmSecretaryTest {

    @Spy
    private Context context = application.getApplicationContext();
    @Mock
    private AlarmManager alarmManager;
    @Captor
    ArgumentCaptor<PendingIntent> pendingIntent;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        when(context.getSystemService(eq(AlarmManager.class))).thenReturn(alarmManager);
    }

    @After
    public void tearDown() throws Exception {
        everythingVanishesSilVousPlait(application.getApplicationContext());
    }

    @Test
    public void setAlarm() throws Exception {
        Reminder reminder = oneRandomReminderOnAnyDaySilVousPlait(context, true);

        AlarmSecretary.getInstance(context).setAlarm(reminder);

        verify(alarmManager).set(
                eq(RTC_WAKEUP),
                eq(reminder.getDate().toDateTime(reminder.getTime()).getMillis()),
                pendingIntent.capture());
        verifyNoMoreInteractions(alarmManager);

        /* TODO: compare the PendingIntents
        Intent intent = new Intent(context, RemindingService.class)
                .setAction(ACTION_SHOW_REMINDER);

        assertThat(
                pendingIntent.getValue(),
                equalTo(getService(context, uniqueInt(), intent, FLAG_UPDATE_CURRENT)));
        */
    }

}