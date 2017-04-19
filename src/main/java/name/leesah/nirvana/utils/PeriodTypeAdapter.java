package name.leesah.nirvana.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.Period;
import org.joda.time.ReadablePeriod;

import java.lang.reflect.Type;

import static name.leesah.nirvana.utils.DateTimeHelper.toPeriod;
import static name.leesah.nirvana.utils.DateTimeHelper.toText;

/**
 * Created by sah on 2016-12-04.
 */

class PeriodTypeAdapter implements JsonSerializer<Period>, JsonDeserializer<Period> {

    @Override
    public JsonElement serialize(final Period src, final Type type, final JsonSerializationContext context) {
        return new JsonPrimitive(src == null ? StringUtils.EMPTY : toText(src));
    }

    @Override
    public Period deserialize(final JsonElement json, final Type type, final JsonDeserializationContext context) throws JsonParseException {
        return json.getAsString().length() == 0 ? null : toPeriod(json.getAsString());
    }
}
