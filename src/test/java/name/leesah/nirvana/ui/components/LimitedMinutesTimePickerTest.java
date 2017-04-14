package name.leesah.nirvana.ui.components;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by sah on 2017-04-16.
 */
public class LimitedMinutesTimePickerTest {
    @Mock private Context context;
    private LimitedMinutesTimePicker picker;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        picker = new LimitedMinutesTimePicker(context, null);
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