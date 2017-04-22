package name.leesah.nirvana.utils;

import com.google.gson.JsonPrimitive;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import static name.leesah.nirvana.utils.DateTimeHelper.toText;
import static org.junit.Assert.assertEquals;

/**
 * Created by sah on 2016-12-08.
 */
public class LocalDateTypeAdapterTest {

    private LocalDate now;
    private JsonPrimitive nowAsJsonElement;
    private LocalDateTypeAdapter adapter;

    @Before
    public void setUp() throws Exception {
        now = LocalDate.now();
        nowAsJsonElement = new JsonPrimitive(toText(now));
        adapter = new LocalDateTypeAdapter();
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