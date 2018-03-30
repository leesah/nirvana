package name.leesah.nirvana.ui.widget;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static name.leesah.nirvana.ui.widget.LimitedMinutesTimePicker.roundUpMinute;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
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

    @Test(expected = IllegalArgumentException.class)
    public void minusOneIsNotAccepted() throws Exception {
        picker.setMinute(-1);
    }

    @Test
    public void zeroIsStoredAsZero() throws Exception {
        picker.setMinute(0);
        assertThat(picker.getMinute(), is(0));
    }

    @Test
    public void oneIsStoredAsFifteen() throws Exception {
        picker.setMinute(1);
        assertThat(picker.getMinute(), is(15));
    }

    @Test
    public void twentyNineIsStoredAsThirty() throws Exception {
        picker.setMinute(29);
        assertThat(picker.getMinute(), is(30));
    }

    @Test
    public void thirtyIsStoredAsThirty() throws Exception {
        picker.setMinute(30);
        assertThat(picker.getMinute(), is(30));
    }

    @Test
    public void fortySixIsStoredAsZero() throws Exception {
        picker.setMinute(46);
        assertThat(picker.getMinute(), is(0));
    }

    @Test
    public void minuteIsRoundedUp() throws Exception {
        assertThat(roundUpMinute(0), is(0));
        assertThat(roundUpMinute(1), is(15));
        assertThat(roundUpMinute(14), is(15));
        assertThat(roundUpMinute(15), is(15));
        assertThat(roundUpMinute(16), is(30));
        assertThat(roundUpMinute(29), is(30));
        assertThat(roundUpMinute(30), is(30));
        assertThat(roundUpMinute(31), is(45));
        assertThat(roundUpMinute(44), is(45));
        assertThat(roundUpMinute(45), is(45));
        assertThat(roundUpMinute(46), is(0));
        assertThat(roundUpMinute(59), is(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void minusOneIsNotRounded() throws Exception {
        roundUpMinute(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void sixtyIsNotRounded() throws Exception {
        roundUpMinute(60);
    }

}