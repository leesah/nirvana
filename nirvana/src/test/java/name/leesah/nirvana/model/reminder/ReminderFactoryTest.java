package name.leesah.nirvana.model.reminder;

import android.content.Context;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import name.leesah.nirvana.BuildConfig;
import name.leesah.nirvana.model.medication.Medication;

import static name.leesah.nirvana.LanternGenie.randomPositiveInt;
import static name.leesah.nirvana.LanternGenie.severalRandomMedicationsSilVousPlait;
import static org.junit.Assert.*;
import static org.robolectric.RuntimeEnvironment.application;

/**
 * Created by sah on 2017-04-29.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = {24, 25})
public class ReminderFactoryTest {
    private Context context;

    @Before
    public void setUp() throws Exception {
        context = application.getApplicationContext();
    }

    @Test @Ignore
    public void createReminders() throws Exception {
        // TODO: figure out how to test this
    }

    @Test @Ignore
    public void createRemindersForCertainMedication() throws Exception {
    }

}