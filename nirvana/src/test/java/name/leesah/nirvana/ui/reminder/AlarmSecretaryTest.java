package name.leesah.nirvana.ui.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

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

import name.leesah.nirvana.BuildConfig;
import name.leesah.nirvana.model.reminder.Reminder;

import static android.app.AlarmManager.RTC_WAKEUP;
import static name.leesah.nirvana.LanternGenie.everythingVanishes;
import static name.leesah.nirvana.LanternGenie.randomReminder;
import static name.leesah.nirvana.PhoneBook.alarmSecretary;
import static org.mockito.ArgumentMatchers.eq;
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
    private ArgumentCaptor<PendingIntent> pendingIntent;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        when(context.getSystemService(eq(AlarmManager.class))).thenReturn(alarmManager);
    }

    @After
    public void tearDown() throws Exception {
        everythingVanishes(application.getApplicationContext());
    }

    @Test
    public void setAlarm() throws Exception {
        Reminder reminder = randomReminder(context, true);

        alarmSecretary(context).setAlarm(reminder);

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