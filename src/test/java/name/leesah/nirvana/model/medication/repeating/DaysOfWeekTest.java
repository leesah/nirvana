package name.leesah.nirvana.model.medication.repeating;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import java.util.EnumSet;

import name.leesah.nirvana.model.DayOfWeek;
import name.leesah.nirvana.model.treatment.TreatmentCycle;

import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static org.joda.time.DateTimeConstants.SATURDAY;
import static org.joda.time.DateTimeConstants.TUESDAY;
import static org.joda.time.Days.ONE;
import static org.joda.time.Days.SEVEN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by sah on 2016-12-08.
 */
public class DaysOfWeekTest {

    private RepeatingModel model;
    private TreatmentCycle following7Days;
    private LocalDate today;

    @Before
    public void setUp() throws Exception {
        model = new DaysOfWeek(EnumSet.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY));
        today = today();
        following7Days = new TreatmentCycle(today.plus(ONE), today.plus(SEVEN));
    }

    @Test
    public void matchesDateSuccess() throws Exception {
        assertTrue(model.matchesDate(following7Days, getNext(TUESDAY)));
    }

    @Test
    public void matchesDateFailure() throws Exception {
        assertFalse(model.matchesDate(following7Days, getNext(SATURDAY)));
    }

    private LocalDate getNext(int day) {
        int dayOfWeekToday = today.getDayOfWeek();
        int offset = day <= dayOfWeekToday ? day + 7 - dayOfWeekToday : day - dayOfWeekToday;

        LocalDate next = today.plusDays(offset);
        assertEquals(day, next.getDayOfWeek());
        return next;
    }

}