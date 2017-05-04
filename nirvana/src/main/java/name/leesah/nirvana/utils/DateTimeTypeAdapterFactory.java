package name.leesah.nirvana.utils;


import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;

import java.io.IOException;

import static java.lang.Class.forName;
import static java.util.Arrays.stream;
import static name.leesah.nirvana.utils.DateTimeHelper.*;

/**
 * Created by sah on 2017-04-19.
 */

public class DateTimeTypeAdapterFactory implements TypeAdapterFactory {

    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (type.getRawType() == LocalTime.class)
            return (TypeAdapter<T>) new LocalTimeAdapter();
        else if (type.getRawType() == LocalDate.class)
            return (TypeAdapter<T>) new LocalDateAdapter();
        else if (type.getRawType() == Period.class)
            return (TypeAdapter<T>) new PeriodAdapter();
        else
            return null;
    }

    private class LocalTimeAdapter extends TypeAdapter<LocalTime> {
        @Override
        public void write(JsonWriter out, LocalTime value) throws IOException {
            if (value == null) out.nullValue();
            else out.value(value.getMillisOfDay());
        }

        @Override
        public LocalTime read(JsonReader in) throws IOException {
            return LocalTime.fromMillisOfDay(in.nextInt());
        }
    }

    private class LocalDateAdapter extends TypeAdapter<LocalDate> {
        @Override
        public void write(JsonWriter out, LocalDate value) throws IOException {
            if (value == null) out.nullValue();
            else out.value(toText(value));
        }

        @Override
        public LocalDate read(JsonReader in) throws IOException {
            return toDate(in.nextString());
        }
    }

    private class PeriodAdapter extends TypeAdapter<Period> {
        @Override
        public void write(JsonWriter out, Period value) throws IOException {
            if (value == null) out.nullValue();
            else out.value(toText(value));
        }

        @Override
        public Period read(JsonReader in) throws IOException {
            return toPeriod(in.nextString());
        }
    }

}
