package name.leesah.nirvana.ui.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.ArrayMap;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.robolectric.RobolectricTestRunner;

import java.util.Set;

import name.leesah.nirvana.model.reminder.Reminder;
import name.leesah.nirvana.model.reminder.ReminderMaker;
import name.leesah.nirvana.persistence.Nurse;

import static android.app.AlarmManager.RTC_WAKEUP;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.app.PendingIntent.getService;
import static com.google.common.collect.Sets.union;
import static name.leesah.nirvana.LanternGenie.everythingVanishes;
import static name.leesah.nirvana.LanternGenie.hire;
import static name.leesah.nirvana.LanternGenie.randomReminder;
import static name.leesah.nirvana.LanternGenie.randomReminders;
import static name.leesah.nirvana.LanternGenie.randomRemindersAnHourAgo;
import static name.leesah.nirvana.LanternGenie.randomRemindersAnHourLater;
import static name.leesah.nirvana.ui.reminder.Midnighter.ACTION_SET_REMINDERS;
import static name.leesah.nirvana.ui.reminder.Midnighter.REQUEST_CODE;
import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.isIn;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;
import static org.robolectric.RuntimeEnvironment.application;

/**
 * Created by sah on 2017-04-20.
 */
@RunWith(RobolectricTestRunner.class)
//TODO: @Config(manifest = Config.NONE, sdk = {24, 25, 26})
public class MidnighterTest {

    @Spy
    private Context context = application.getApplicationContext();
    @Captor
    private ArgumentCaptor<PendingIntent> intent;
    @Mock
    private AlarmManager alarmManager;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
        everythingVanishes(context);
    }

    @Test
    public void setMidnightAlarm() throws Exception {
        when(context.getSystemService(same(AlarmManager.class))).thenReturn(alarmManager);

        Midnighter.setMidnightAlarm(context);

        verify(alarmManager).set(eq(RTC_WAKEUP), anyLong(), intent.capture());
        assertThat(intent.getValue(), equalTo(getService(context, REQUEST_CODE,
                new Intent(context, Midnighter.class)
                        .setAction(ACTION_SET_REMINDERS), FLAG_UPDATE_CURRENT)));
        verifyNoMoreInteractions(alarmManager);

    }

}