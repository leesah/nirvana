package name.leesah.nirvana.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.ReadablePeriod;

import name.leesah.nirvana.model.medication.reminding.RemindingModel;
import name.leesah.nirvana.model.medication.repeating.RepeatingModel;
import name.leesah.nirvana.model.treatment.repeating.TreatmentCycleRepeatingModel;

/**
 * Created by sah on 2016-12-07.
 */

public class AdaptedGsonFactory {

    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(TreatmentCycleRepeatingModel.class, new MetaKeyBasedGenericTypeAdapter<TreatmentCycleRepeatingModel>())
                .registerTypeAdapter(RepeatingModel.class, new MetaKeyBasedGenericTypeAdapter<RepeatingModel>())
                .registerTypeAdapter(RemindingModel.class, new MetaKeyBasedGenericTypeAdapter<RemindingModel>())
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeTypeAdapter())
                .registerTypeAdapter(ReadablePeriod.class, new ReadablePeriodTypeAdapter())
                .registerTypeAdapterFactory(new LowercaseEnumTypeAdapterFactory())
                .create();
    }
}
