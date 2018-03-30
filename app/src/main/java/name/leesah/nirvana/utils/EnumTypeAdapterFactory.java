package name.leesah.nirvana.utils;


import com.google.common.collect.MoreCollectors;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import static java.util.Arrays.stream;
import static org.apache.commons.lang3.StringUtils.lowerCase;

/**
 * Created by sah on 2017-04-19.
 */

public class EnumTypeAdapterFactory implements TypeAdapterFactory {

    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

        Class<T> rawType = (Class<T>) type.getRawType();
        if (!rawType.isEnum()) return null;

        return new TypeAdapter<T>() {

            @Override
            public void write(JsonWriter out, T value) throws IOException {
                if (value == null) out.nullValue();
                else out.value(lowerCase(value.toString()));
            }

            @Override
            public T read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                } else {
                    String s = in.nextString();
                    return stream(rawType.getEnumConstants())
                            .filter(e -> lowerCase(e.toString()).equals(s))
                            .collect(MoreCollectors.onlyElement());
                }
            }
        };
    }

}
