package name.leesah.nirvana.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.ReadablePeriod;

import java.lang.reflect.Type;

/**
 * Created by sah on 2016-12-04.
 */

class ReadablePeriodTypeAdapter implements JsonSerializer<ReadablePeriod>, JsonDeserializer<ReadablePeriod> {

    @Override
    public JsonElement serialize(final ReadablePeriod src, final Type type, final JsonSerializationContext context) {
        return new JsonPrimitive(src == null ? StringUtils.EMPTY : DateTimeHelper.toText(src));
    }

    @Override
    public ReadablePeriod deserialize(final JsonElement json, final Type type, final JsonDeserializationContext context) throws JsonParseException {
        return json.getAsString().length() == 0 ? null : DateTimeHelper.toPeriod(json.getAsString());
    }
}
