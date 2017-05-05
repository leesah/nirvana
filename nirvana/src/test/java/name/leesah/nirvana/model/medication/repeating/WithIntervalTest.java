package name.leesah.nirvana.model.medication.repeating;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Random;

import name.leesah.nirvana.model.medication.starting.StartingStrategy;
import name.leesah.nirvana.model.treatment.Treatment;

import static name.leesah.nirvana.LanternGenie.randomDaySilVousPlait;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by sah on 2017-04-29.
 */
public class WithIntervalTest {

    private static final int N = 10;

    @Mock(name = "A nice treatment")
    private Treatment treatment;
    @Mock(name = "A good strategy")
    private StartingStrategy startingStrategy;
    private LocalDate realStartDate;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        realStartDate = randomDaySilVousPlait();
        when(treatment.contains(notNull())).thenReturn(true);
    }

    @Test
    public void matches() throws Exception {
        LocalDate date = realStartDate.plusDays((new Random().nextInt(10) + 1) * 10);
        when(startingStrategy.getRealStartDate(same(treatment), same(date))).thenReturn(realStartDate);
        assertThat(new WithInterval(N).matches(treatment, startingStrategy, date), is(true));
    }

    @Test
    public void matchesNot() throws Exception {
        LocalDate date = realStartDate.plusDays(N + 1);
        when(startingStrategy.getRealStartDate(same(treatment), same(date))).thenReturn(realStartDate);
        assertThat(new WithInterval(N).matches(treatment, startingStrategy, date), is(false));
    }

}