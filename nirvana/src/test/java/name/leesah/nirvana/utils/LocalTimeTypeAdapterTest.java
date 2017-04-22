package name.leesah.nirvana.utils;

import com.google.gson.JsonPrimitive;

import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by sah on 2016-12-08.
 */
public class LocalTimeTypeAdapterTest {

    private LocalTime now;
    private JsonPrimitive nowAsJsonElement;
    private LocalTimeTypeAdapter adapter;

    @Before
    public void setUp() throws Exception {
        now = LocalTime.now();
        nowAsJsonElement = new JsonPrimitive(now.getMillisOfDay());
        adapter = new LocalTimeTypeAdapter();
    }

    @Test
    public void serialize() throws Exception {
        assertEquals(nowAsJsonElement, adapter.serialize(now, null, null));
    }

    @Test
    public void deserialize() throws Exception {
        assertEquals(now, adapter.deserialize(nowAsJsonElement, null, null));
    }

}