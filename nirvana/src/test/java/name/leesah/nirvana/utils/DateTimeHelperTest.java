package name.leesah.nirvana.utils;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;

import name.leesah.nirvana.model.PeriodUnit;

import static org.junit.Assert.assertEquals;

/**
 * Created by sah on 2016-12-14.
 */
public class DateTimeHelperTest {

    private final LocalDate date = new LocalDate().withYear(2016).withMonthOfYear(6).withDayOfMonth(27);
    private final String dateAstring = "2016-06-27";
    private Period period = Days.days(1024).toPeriod();
    private String periodAsString = "P1024D";


    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void dateToString() throws Exception {
        assertEquals(dateAstring, DateTimeHelper.toText(date));
    }

    @Test
    public void stringToDate() throws Exception {
        assertEquals(date, DateTimeHelper.toDate(dateAstring));
    }

    @Test
    public void periodToString() throws Exception {
        assertEquals(periodAsString, DateTimeHelper.toText(period));
    }

    @Test
    public void stringToPeriod() throws Exception {
        assertEquals(period, DateTimeHelper.toPeriod(periodAsString));
    }

    @Test
    public void today() throws Exception {
        assertEquals(LocalDate.now(), DateTimeHelper.today());
    }

    @Test
    public void todayAsString() throws Exception {
        assertEquals(DateTimeHelper.toText(LocalDate.now()), DateTimeHelper.todayAsString());
    }

    @Test
    public void numberAndUnitToPeriod() throws Exception {
        assertEquals(period, DateTimeHelper.toPeriod(1024, PeriodUnit.DAY));
    }

    @Test
    public void numberAndUnitToPeriodAsString() throws Exception {
        assertEquals(periodAsString, DateTimeHelper.toPeriodAsString(1024, PeriodUnit.DAY));
    }

}