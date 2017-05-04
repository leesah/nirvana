package name.leesah.nirvana.utils;

import org.junit.Test;

import name.leesah.nirvana.model.PeriodUnit;
import name.leesah.nirvana.model.Weekday;

import static name.leesah.nirvana.utils.AdaptedGsonFactory.getGson;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by sah on 2017-05-04.
 */
public class EnumTypeAdapterFactoryTest {

    @Test
    public void periodUnit() throws Exception {
        String json = getGson().toJson(PeriodUnit.DAY);
        assertThat(getGson().fromJson(json, PeriodUnit.class), equalTo(PeriodUnit.DAY));
    }

    @Test
    public void weekday() throws Exception {
        String json = getGson().toJson(Weekday.FRIDAY);
        assertThat(getGson().fromJson(json, Weekday.class), equalTo(Weekday.FRIDAY));
    }

}