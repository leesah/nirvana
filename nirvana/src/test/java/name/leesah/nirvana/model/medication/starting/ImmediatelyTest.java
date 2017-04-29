package name.leesah.nirvana.model.medication.starting;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import name.leesah.nirvana.model.treatment.Treatment;

import static name.leesah.nirvana.DateTimeRelatedTestHelper.randomDay;
import static name.leesah.nirvana.DateTimeRelatedTestHelper.randomDayBefore;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by sah on 2017-04-29.
 */
public class ImmediatelyTest {
    @Mock
    private Treatment treatment;
    private LocalDate date;
    private LocalDate startDate;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        date = randomDay();
        startDate = randomDayBefore(date);
    }

    @Test
    public void getRealStartDate() throws Exception {
        when(treatment.getStartDateOf(same(date))).thenReturn(startDate);
        assertThat(new Immediately().getRealStartDate(treatment, date), equalTo(startDate));
    }

}