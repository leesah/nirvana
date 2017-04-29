package name.leesah.nirvana.model.medication.stopping;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import name.leesah.nirvana.model.treatment.Treatment;

import static name.leesah.nirvana.DateTimeRelatedTestHelper.YEAR;
import static name.leesah.nirvana.DateTimeRelatedTestHelper.randomDay;
import static name.leesah.nirvana.DateTimeRelatedTestHelper.randomDayBefore;
import static name.leesah.nirvana.DateTimeRelatedTestHelper.randomPeriod;
import static org.hamcrest.CoreMatchers.is;
import static org.joda.time.Days.ONE;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by sah on 2017-04-27.
 */
public class InPeriodTest {

    @Mock
    private Treatment treatment;
    private Period lastsFor;
    private LocalDate lastDay;
    private InPeriod strategy;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        LocalDate dayZero = randomDay();
        Period length = randomPeriod();
        when(treatment.contains(notNull())).thenReturn(true);
        when(treatment.getStartDateOf(notNull())).thenReturn(dayZero);

        lastsFor = randomPeriod(length);
        strategy = new InPeriod(lastsFor);

        lastDay = dayZero.plus(lastsFor).minus(ONE);
    }

    @Test
    public void hasNotStopped() throws Exception {
        // Long before the actual last day
        assertStopped(false, randomDayBefore(lastDay));
        // The day before the actual last day
        assertStopped(false, lastDay.minus(ONE));
        // The actual last day
        assertStopped(false, lastDay);
    }

    @Test
    public void hasStopped() throws Exception {
        // The second day of the actual last day
        assertStopped(true, lastDay.plus(ONE));
        // Long after the actual last day
        assertStopped(true, lastDay.plus(lastsFor).plus(randomPeriod(YEAR)));
    }

    private void assertStopped(boolean stopped, LocalDate date) {
        assertThat(strategy.hasStopped(treatment, date), is(stopped));
    }

}