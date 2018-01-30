package name.leesah.nirvana.ui.preference;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import name.leesah.nirvana.ui.widget.LimitedMinutesTimePicker;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.RuntimeEnvironment.application;

/**
 * Created by sah on 2017-04-16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = {24, 25, 26})
public class LimitedMinutesTimePickerTest {
    private LimitedMinutesTimePicker picker;

    @Before
    public void setUp() throws Exception {
        picker = new LimitedMinutesTimePicker(application.getApplicationContext(), null);
    }

    @Test
    public void roundUpMinute() throws Exception {
        verifyRoundUpMinute(0, 0);
        verifyRoundUpMinute(1, 15);
        verifyRoundUpMinute(14, 15);
        verifyRoundUpMinute(15, 15);
        verifyRoundUpMinute(16, 30);
        verifyRoundUpMinute(29, 30);
        verifyRoundUpMinute(30, 30);
        verifyRoundUpMinute(31, 45);
        verifyRoundUpMinute(44, 45);
        verifyRoundUpMinute(45, 45);
        verifyRoundUpMinute(46, 0);
        verifyRoundUpMinute(59, 0);
    }

    private void verifyRoundUpMinute(int given, int expected) {
        int i = picker.roundUpMinute(given);
        assertThat(LimitedMinutesTimePicker.DISPLAYED_MINUTES[i], is(expected));
    }
}