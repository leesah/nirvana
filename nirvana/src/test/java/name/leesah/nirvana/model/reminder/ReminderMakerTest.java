package name.leesah.nirvana.model.reminder;

import android.content.Context;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.robolectric.RuntimeEnvironment.application;

/**
 * Created by sah on 2017-04-29.
 */
@RunWith(RobolectricTestRunner.class)
//TODO: @Config(manifest = Config.NONE, sdk = {24, 25, 26})
public class ReminderMakerTest {
    private Context context;

    @Before
    public void setUp() throws Exception {
        context = application.getApplicationContext();
    }

    @Test
    @Ignore
    public void createReminders() throws Exception {
        // TODO: figure out how to test this
    }

    @Test
    @Ignore
    public void createRemindersForCertainMedication() throws Exception {
    }

}