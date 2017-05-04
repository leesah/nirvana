package name.leesah.nirvana.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
                .registerTypeAdapterFactory(new MetaKeyBasedGenericTypeAdapterFactory(
                        RecurringStrategy.class,
                        RemindingStrategy.class, RepeatingStrategy.class,
                        StartingStrategy.class, StoppingStrategy.class))
                .registerTypeAdapterFactory(new DateTimeTypeAdapterFactory())
                .registerTypeAdapterFactory(new EnumTypeAdapterFactory())
                .create();
    }
}
