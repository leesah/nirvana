package name.leesah.nirvana.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;

import name.leesah.nirvana.model.medication.reminding.RemindingStrategy;
import name.leesah.nirvana.model.medication.repeating.RepeatingStrategy;
import name.leesah.nirvana.model.medication.starting.StartingStrategy;
import name.leesah.nirvana.model.medication.stopping.StoppingStrategy;
import name.leesah.nirvana.model.treatment.recurring.RecurringStrategy;

/**
 * Created by sah on 2016-12-07.
 */

public class AdaptedGsonFactory {

    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(RecurringStrategy.class, new MetaKeyBasedGenericTypeAdapter<RecurringStrategy>())
                .registerTypeAdapter(RemindingStrategy.class, new MetaKeyBasedGenericTypeAdapter<RemindingStrategy>())
                .registerTypeAdapter(RepeatingStrategy.class, new MetaKeyBasedGenericTypeAdapter<RepeatingStrategy>())
                .registerTypeAdapter(StartingStrategy.class, new MetaKeyBasedGenericTypeAdapter<StartingStrategy>())
                .registerTypeAdapter(StoppingStrategy.class, new MetaKeyBasedGenericTypeAdapter<StoppingStrategy>())
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeTypeAdapter())
                .registerTypeAdapter(Period.class, new PeriodTypeAdapter())
                .registerTypeAdapterFactory(new LowercaseEnumTypeAdapterFactory())
                .create();
    }
}
