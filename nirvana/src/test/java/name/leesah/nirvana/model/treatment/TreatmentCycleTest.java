package name.leesah.nirvana.model.treatment;

import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Created by sah on 2016-12-06.
 */
public class TreatmentCycleTest {

    public static final int MILLIS_PER_YEAR = DateTimeConstants.MILLIS_PER_DAY * 365;

    @Test
    public void equals() throws Exception {
        long todayAsMillis = LocalDate.now().toDateTimeAtStartOfDay().getMillis();
        long firstDayAsMillis = todayAsMillis + new Random().nextInt(MILLIS_PER_YEAR);
        long lastDayAsMillis = firstDayAsMillis + new Random().nextInt(MILLIS_PER_YEAR);

        assertEquals(new TreatmentCycle(new LocalDate(firstDayAsMillis), new LocalDate(lastDayAsMillis)), new TreatmentCycle(new LocalDate(firstDayAsMillis), new LocalDate(lastDayAsMillis)));
    }

    @Test
    public void makeNext() throws Exception {
        int cycleLength = new Random().nextInt(365);

        LocalDate firstDay = LocalDate.now().plusDays(new Random().nextInt());
        LocalDate lastDay = firstDay.plusDays(cycleLength);
        TreatmentCycle cycle = new TreatmentCycle(firstDay, lastDay);

        LocalDate firstDayOfNextCycle = lastDay.plus(Days.ONE);
        TreatmentCycle expected = new TreatmentCycle(firstDayOfNextCycle, firstDayOfNextCycle.plusDays(cycleLength));

        assertEquals(expected, TreatmentCycle.makeNext(cycle));
    }

    @Test
    public void length() throws Exception {
        assertEquals(Days.ONE, new TreatmentCycle(LocalDate.now(), LocalDate.now()).length());
    }
}