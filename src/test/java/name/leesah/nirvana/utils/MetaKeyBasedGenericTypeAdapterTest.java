package name.leesah.nirvana.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Random;

import name.leesah.nirvana.model.treatment.repeating.NotRepeating;
import name.leesah.nirvana.model.treatment.repeating.NTimes;
import name.leesah.nirvana.model.treatment.repeating.UntilDate;
import name.leesah.nirvana.model.treatment.repeating.TreatmentCycleRepeatingModel;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;
import static name.leesah.nirvana.utils.MetaKeyBasedGenericTypeAdapter.CLASS_META_KEY;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

/**
 * Created by sah on 2016-12-05.
 */
public class MetaKeyBasedGenericTypeAdapterTest {

    private MetaKeyBasedGenericTypeAdapter<TreatmentCycleRepeatingModel> adapter;

    private JsonSerializationContext jsonSerializationContext;
    private JsonDeserializationContext jsonDeerializationContext;
    private JsonElement jsonElement;
    private JsonObject jsonObject;

    @Before
    public void setUp() throws Exception {
        adapter = new MetaKeyBasedGenericTypeAdapter<>();

        jsonSerializationContext = Mockito.mock(JsonSerializationContext.class);
        jsonDeerializationContext = Mockito.mock(JsonDeserializationContext.class);
        jsonElement = Mockito.mock(JsonElement.class);
        jsonObject = new JsonObject();

        when(jsonElement.getAsJsonObject()).thenReturn(jsonObject);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void serializeNotRepeatingModel() throws Exception {
        doTestSerialize(new NotRepeating());
    }

    @Test
    public void serializeRepeatingNTimesModel() throws Exception {
        doTestSerialize(new NTimes(new Random().nextInt()));
    }

    @Test
    public void serializeRepeatingUntilDateModel() throws Exception {
        doTestSerialize(new UntilDate(LocalDate.now()));
    }

    private void doTestSerialize(TreatmentCycleRepeatingModel model) {
        when(jsonSerializationContext.serialize(same(model), eq(model.getClass()))).thenReturn(jsonElement);

        JsonElement actual = adapter.serialize(model, TreatmentCycleRepeatingModel.class, jsonSerializationContext);

        assertSame(jsonElement, actual);
        assertEquals(model.getClass().getCanonicalName(), jsonObject.get(CLASS_META_KEY).getAsString());
    }

    @Test
    public void deserializeNotRepeating() throws Exception {
        doTestDeserialize(new NotRepeating());
    }

    @Test
    public void deserializeRepeatingNTimes() throws Exception {
        doTestDeserialize(new NTimes(new Random().nextInt()));
    }

    @Test
    public void deserializeRepeatingUntilDate() throws Exception {
        doTestDeserialize(new UntilDate(LocalDate.now()));
    }

    private void doTestDeserialize(TreatmentCycleRepeatingModel model) {
        jsonObject.addProperty(CLASS_META_KEY, model.getClass().getCanonicalName());
        when(jsonDeerializationContext.deserialize(jsonElement, model.getClass())).thenReturn(model);
        TreatmentCycleRepeatingModel actual = adapter.deserialize(jsonElement, NTimes.class, jsonDeerializationContext);

        assertSame(model, actual);
    }

}