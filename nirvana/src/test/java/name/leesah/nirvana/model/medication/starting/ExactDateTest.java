package name.leesah.nirvana.model.medication.starting;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import name.leesah.nirvana.model.treatment.Treatment;

import static name.leesah.nirvana.LanternGenie.randomDay;
import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by sah on 2017-04-29.
 */
public class ExactDateTest {
    @Mock
    private Treatment treatment;
    private LocalDate date;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        when(treatment.getStartDateOf(date)).thenReturn(randomDay());
    }

    @Test
    public void getRealStartDate() throws Exception {
        LocalDate realStartDate = randomDay();
        assertThat(new ExactDate(realStartDate).getRealStartDate(treatment, date), equalTo(realStartDate));
    }

    @Test
    public void equals() throws Exception {
        assertThat(new ExactDate(today()), equalTo(new ExactDate(today())));
    }
}