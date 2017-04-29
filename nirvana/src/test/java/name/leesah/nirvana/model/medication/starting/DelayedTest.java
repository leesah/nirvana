package name.leesah.nirvana.model.medication.starting;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import name.leesah.nirvana.model.treatment.Treatment;

import static name.leesah.nirvana.DateTimeRelatedTestHelper.random;
import static name.leesah.nirvana.DateTimeRelatedTestHelper.randomDay;
import static name.leesah.nirvana.DateTimeRelatedTestHelper.randomDayBefore;
import static name.leesah.nirvana.DateTimeRelatedTestHelper.randomPeriod;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by sah on 2017-04-27.
 */
public class DelayedTest {

    @Mock
    private Treatment treatment;
    private Period delayedFor = randomPeriod();
    private LocalDate date = randomDay();
    private LocalDate startDate = randomDayBefore(date);

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        when(treatment.getStartDateOf(date)).thenReturn(startDate);
    }

    @Test
    public void getRealStartDate() throws Exception {
        assertThat(new Delayed(delayedFor).getRealStartDate(treatment, date), equalTo(startDate.plus(delayedFor)));
    }
}