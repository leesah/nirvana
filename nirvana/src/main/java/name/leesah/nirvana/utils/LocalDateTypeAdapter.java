package name.leesah.nirvana.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

import java.lang.reflect.Type;

import static name.leesah.nirvana.utils.DateTimeHelper.toDate;
import static name.leesah.nirvana.utils.DateTimeHelper.toText;

/**
 * Created by sah on 2016-12-04.
 */

class LocalDateTypeAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

    @Override
    public JsonElement serialize(final LocalDate src, final Type type, final JsonSerializationContext context) {
        return new JsonPrimitive(src == null ? StringUtils.EMPTY : toText(src));
    }

    @Override
    public LocalDate deserialize(final JsonElement json, final Type type, final JsonDeserializationContext context) throws JsonParseException {
        return json.getAsString().length() == 0 ? null : toDate(json.getAsString());
    }
}
