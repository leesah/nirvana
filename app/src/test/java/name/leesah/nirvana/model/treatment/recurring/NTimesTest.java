package name.leesah.nirvana.model.treatment.recurring;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;

import name.leesah.nirvana.LanternGenie;

import static name.leesah.nirvana.LanternGenie.randomDay;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by sah on 2017-04-29.
 */
public class NTimesTest {

    private LocalDate dayZero;
    private Period length;
    private int n;

    @Before
    public void setUp() throws Exception {
        dayZero = randomDay();
        length = LanternGenie.randomPeriod();
        n = LanternGenie.randomAmount();
    }

    @Test
    public void hasNotNextWhenTotallyOneTime() throws Exception {
        assertThat(new NTimes(1).hasNext(dayZero, length, dayZero), is(false));
    }

    @Test
    public void hasNotNext() throws Exception {
        LocalDate date = dayZero.plus(length).plus(length);
        assertThat(new NTimes(3).hasNext(dayZero, length, date), is(false));
    }

    @Test
    public void hasNext() throws Exception {
        LocalDate date = dayZero.plus(length).plus(length);
        assertThat(new NTimes(4).hasNext(dayZero, length, date), is(true));
    }
}