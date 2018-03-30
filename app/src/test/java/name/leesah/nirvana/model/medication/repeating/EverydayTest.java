package name.leesah.nirvana.model.medication.repeating;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import name.leesah.nirvana.model.medication.starting.StartingStrategy;
import name.leesah.nirvana.model.treatment.Treatment;

import static name.leesah.nirvana.LanternGenie.randomDay;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by sah on 2017-04-29.
 */
public class EverydayTest {
    @Mock
    private Treatment treatment;
    @Mock
    private StartingStrategy startingStrategy;
    private LocalDate date = randomDay();
    private LocalDate realStartDate = randomDay();

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        when(startingStrategy.getRealStartDate(same(treatment), same(date))).thenReturn(realStartDate);
    }

    @Test
    public void matches() throws Exception {
        when(treatment.contains(same(date))).thenReturn(true);
        assertThat(new Everyday().matches(treatment, startingStrategy, date), is(true));
    }

    @Test
    public void matchesNot() throws Exception {
        when(treatment.contains(same(date))).thenReturn(false);
        assertThat(new Everyday().matches(treatment, startingStrategy, date), is(false));
    }

}