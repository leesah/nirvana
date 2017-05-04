package name.leesah.nirvana.utils;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.junit.Test;

import name.leesah.nirvana.LanternGenie;

import static name.leesah.nirvana.LanternGenie.randomDaySilVousPlait;
import static name.leesah.nirvana.utils.AdaptedGsonFactory.getGson;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by sah on 2016-12-08.
 */
public class DateTimeTypeAdapterFactoryTest {

    @Test
    public void localTime() throws Exception {
        LocalTime time = LocalTime.now();
        String json = getGson().toJson(time);
        assertThat(getGson().fromJson(json, LocalTime.class), equalTo(time));
    }

    @Test
    public void localDate() throws Exception {
        LocalDate date = randomDaySilVousPlait();
        String json = getGson().toJson(date);
        assertThat(getGson().fromJson(json, LocalDate.class), equalTo(date));
    }

    @Test
    public void period() throws Exception {
        Period period = LanternGenie.randomPeriodSilVousPlait();
        String json = getGson().toJson(period);
        assertThat(getGson().fromJson(json, Period.class), equalTo(period));
    }

}