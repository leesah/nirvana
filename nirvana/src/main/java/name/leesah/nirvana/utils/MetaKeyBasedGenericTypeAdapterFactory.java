package name.leesah.nirvana.utils;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import static java.lang.Class.forName;
import static java.util.Arrays.stream;

/**
 * Created by sah on 2017-04-19.
 */

public class MetaKeyBasedGenericTypeAdapterFactory implements TypeAdapterFactory {

    static final String CLASS_META_KEY = "name.leesah.name:key:CLASS";

    private final Class<?>[] supported;

    public MetaKeyBasedGenericTypeAdapterFactory(Class<?>... supported) {
        this.supported = supported;
    }

    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

        if (stream(supported).noneMatch(c -> c.isAssignableFrom(type.getRawType())))
            return null;

        return new TypeAdapter<T>() {

            @Override
            public void write(JsonWriter out, T value) throws IOException {
                if (value == null) out.nullValue();
                else {
                    Class<T> actualClass = (Class<T>) value.getClass();
                    JsonElement json = gson.getDelegateAdapter(
                            MetaKeyBasedGenericTypeAdapterFactory.this, TypeToken.get(actualClass)
                    ).toJsonTree(value);
                    json.getAsJsonObject().addProperty(CLASS_META_KEY, actualClass.getCanonicalName());

                    gson.getAdapter(JsonElement.class).write(out, json);
                }
            }

            @Override
            public T read(JsonReader in) throws IOException {
                try {
                    JsonElement json = gson.fromJson(in, JsonElement.class);

                    String className = json.getAsJsonObject().remove(CLASS_META_KEY).getAsString();
                    @SuppressWarnings("unchecked")
                    TypeToken<T> actualType = TypeToken.get((Class<T>) forName(className));

                    return gson.getDelegateAdapter(
                            MetaKeyBasedGenericTypeAdapterFactory.this, actualType
                    ).fromJsonTree(json);
                } catch (ClassNotFoundException | ClassCastException e) {
                    throw new JsonParseException(e);
                }
            }
        };
    }
}
