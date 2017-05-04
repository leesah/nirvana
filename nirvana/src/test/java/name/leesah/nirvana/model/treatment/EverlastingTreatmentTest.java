package name.leesah.nirvana.model.treatment;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import static name.leesah.nirvana.LanternGenie.randomDaySilVousPlait;
import static name.leesah.nirvana.LanternGenie.randomDaySilVousPlaitAfter;
import static name.leesah.nirvana.LanternGenie.randomDaySilVousPlaitBefore;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by sah on 2016-12-07.
 */
public class EverlastingTreatmentTest {

    private LocalDate dayZero;

    @Before
    public void setUp() throws Exception {
        dayZero = randomDaySilVousPlait();
    }

    @Test
    public void contains() throws Exception {
        assertThat(
                new EverlastingTreatment(dayZero)
                        .contains(randomDaySilVousPlaitAfter(dayZero)),
                is(true));
    }

    @Test
    public void containsDayZero() throws Exception {
        assertThat(
                new EverlastingTreatment(dayZero)
                        .contains(dayZero),
                is(true));
    }

    @Test
    public void containsNot() throws Exception {
        assertThat(
                new EverlastingTreatment(dayZero)
                        .contains(randomDaySilVousPlaitBefore(dayZero)),
                is(false));
    }

    @Test
    public void getStartDateOf() throws Exception {
        assertThat(
        new EverlastingTreatment(dayZero)
                .getStartDateOf(randomDaySilVousPlait()),
                equalTo(dayZero));
    }

}