package name.leesah.nirvana.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.joda.time.LocalTime;

import java.lang.reflect.Type;


/**
 * Created by sah on 2016-12-08.
 */

class LocalTimeTypeAdapter implements JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {

    @Override
    public JsonElement serialize(LocalTime src, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(src == null ? 0 : src.getMillisOfDay());
    }

    @Override
    public LocalTime deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        long millis = json.getAsInt();
        return millis == 0 ? null : LocalTime.fromMillisOfDay(millis);
    }
}
