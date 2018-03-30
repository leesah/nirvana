package name.leesah.nirvana.model.treatment;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import name.leesah.nirvana.LanternGenie;
import name.leesah.nirvana.model.treatment.recurring.RecurringStrategy;

import static name.leesah.nirvana.LanternGenie.randomDay;
import static name.leesah.nirvana.LanternGenie.randomDayAfter;
import static name.leesah.nirvana.LanternGenie.randomDayBefore;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by sah on 2016-12-07.
 */
@Ignore
public class RecurringTreatmentTest {

    private LocalDate dayZero;
    private Period length;
    @Mock
    private RecurringStrategy recurring;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        dayZero = randomDay();
        length = LanternGenie.randomPeriod();
    }

    @Test
    public void contains() throws Exception {
        assertThat(
                new RecurringTreatment(dayZero, length, recurring)
                        .contains(randomDayAfter(dayZero)),
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
                        .contains(randomDayBefore(dayZero)),
                is(false));
    }

    @Test
    public void getStartDateOf() throws Exception {
        assertThat(
                new EverlastingTreatment(dayZero)
                        .getStartDateOf(randomDay()),
                equalTo(dayZero));
    }

}