package name.leesah.nirvana.model.medication.stopping;

import org.joda.time.LocalDate;
import org.junit.Test;

import name.leesah.nirvana.model.treatment.Treatment;

import static name.leesah.nirvana.LanternGenie.randomDaySilVousPlait;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by sah on 2017-04-29.
 */
public class NeverTest {

    @Test
    public void hasNotStopped() throws Exception {
        Treatment treatment = mock(Treatment.class);
        LocalDate date = randomDaySilVousPlait();
        when(treatment.contains(same(date))).thenReturn(true);
        assertThat(new Never().hasStopped(treatment, date), is(false));
    }

    @Test
    public void hasStopped() throws Exception {
        Treatment treatment = mock(Treatment.class);
        LocalDate date = randomDaySilVousPlait();
        when(treatment.contains(same(date))).thenReturn(false);
        assertThat(new Never().hasStopped(treatment, date), is(true));
    }

}