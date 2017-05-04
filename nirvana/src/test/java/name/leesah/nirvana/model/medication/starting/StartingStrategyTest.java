package name.leesah.nirvana.model.medication.starting;

import android.content.Context;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import name.leesah.nirvana.model.treatment.Treatment;

import static name.leesah.nirvana.LanternGenie.randomDaySilVousPlait;
import static name.leesah.nirvana.LanternGenie.randomDaySilVousPlaitAfter;
import static name.leesah.nirvana.LanternGenie.randomDaySilVousPlaitBefore;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Created by sah on 2017-04-29.
 */
public class StartingStrategyTest {

    private LocalDate realStartDate;
    private StartingStrategy startingStrategy;

    @Before
    public void setUp() throws Exception {
        realStartDate = randomDaySilVousPlait();
        startingStrategy = new StartingStrategy() {
            @Override
            public LocalDate getRealStartDate(Treatment treatment, LocalDate date) {
                return realStartDate;
            }

            @Override
            public String toString(Context context) {
                return null;
            }
        };
    }

    @Test
    public void hasStartedLongAgo() throws Exception {
        assertThat(startingStrategy.hasStarted(mock(Treatment.class), randomDaySilVousPlaitAfter(realStartDate)), is(true));
    }

    @Test
    public void hasStartedJustTheDay() throws Exception {
        assertThat(startingStrategy.hasStarted(mock(Treatment.class), new LocalDate(realStartDate)), is(true));
    }

    @Test
    public void hasNotStarted() throws Exception {
        assertThat(startingStrategy.hasStarted(mock(Treatment.class), randomDaySilVousPlaitBefore(realStartDate)), is(false));
    }

}