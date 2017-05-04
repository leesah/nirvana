package name.leesah.nirvana.model.medication.repeating;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import name.leesah.nirvana.model.medication.starting.StartingStrategy;
import name.leesah.nirvana.model.treatment.Treatment;

import static java.util.EnumSet.of;
import static name.leesah.nirvana.LanternGenie.randomDaySilVousPlait;
import static name.leesah.nirvana.model.Weekday.MONDAY;
import static name.leesah.nirvana.model.Weekday.TUESDAY;
import static name.leesah.nirvana.model.Weekday.WEDNESDAY;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by sah on 2017-04-29.
 */
public class DaysOfWeekTest {
    @Mock
    private Treatment treatment;
    @Mock
    private StartingStrategy startingStrategy;
    private LocalDate date = randomDaySilVousPlait().withDayOfWeek(DateTimeConstants.MONDAY);
    private LocalDate realStartDate = randomDaySilVousPlait();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        when(treatment.contains(same(date))).thenReturn(true);
        when(startingStrategy.getRealStartDate(same(treatment), same(date))).thenReturn(realStartDate);
    }

    @Test
    public void matches() throws Exception {
        assertThat(new DaysOfWeek(of(MONDAY, TUESDAY)).matches(treatment, startingStrategy, date), is(true));
    }

    @Test
    public void matchesNot() throws Exception {
        assertThat(new DaysOfWeek(of(TUESDAY, WEDNESDAY)).matches(treatment, startingStrategy, date), is(false));
    }

}