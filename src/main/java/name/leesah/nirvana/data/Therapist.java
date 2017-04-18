package name.leesah.nirvana.data;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.joda.time.LocalDate;
import org.joda.time.Period;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.treatment.Treatment;
import name.leesah.nirvana.model.treatment.TreatmentBuilder;
import name.leesah.nirvana.model.treatment.TreatmentCycle;
import name.leesah.nirvana.model.treatment.repeating.NotRepeating;
import name.leesah.nirvana.model.treatment.repeating.NTimes;
import name.leesah.nirvana.model.treatment.repeating.UntilDate;
import name.leesah.nirvana.model.treatment.repeating.TreatmentCycleRepeatingModel;
import name.leesah.nirvana.utils.DateTimeHelper;

import static java.lang.String.format;
import static name.leesah.nirvana.utils.DateTimeHelper.toDate;

/**
 * Created by sah on 2016-12-11.
 */

public class Therapist extends DataHolder {

    private static final String TAG = Therapist.class.getSimpleName();

    private static Therapist instance;
    private Treatment treatmentCache;

    private Therapist(Context context) {
        super(context);
    }

    public static Therapist getInstance(Context context) {
        if (instance == null)
            instance = new Therapist(context);
        return instance;
    }

    public Treatment getTreatment() {
        if (treatmentCache == null)
            loadTreatmentCache();
        return treatmentCache;
    }

    public TreatmentCycle getTreatmentCycle(LocalDate today) {
        Treatment treatment = getTreatment();
        return treatment
                .getRepeatingModel()
                .getCycleForDate(treatment.getFirstCycle(), today);
    }

    private void loadTreatmentCache() {
        boolean treatmentSupportEnabled = preferences.getBoolean(resources.getString(R.string.pref_key_treatment_enabled), false);
        if (treatmentSupportEnabled) {
            treatmentCache = loadTreatmentFromSharedPreferences();
        } else {
            treatmentCache = buildDummyTreatment();
        }

        Log.d(TAG, format("Loaded treatment: %s", treatmentCache));
    }

    private Treatment loadTreatmentFromSharedPreferences() {
        return new TreatmentBuilder()
                .setFirstDay(loadLocalDate())
                .setCycleLength(loadCycleLength())
                .setTreatmentCycleRepeatingModel(loadRepeatingModel())
                .build();
    }

    private LocalDate loadLocalDate() {
        String key = resources.getString(R.string.pref_key_treatment_first_day);
        String text = preferences.getString(key, null);
        if (TextUtils.isEmpty(text))
            throw new IllegalStateException(format("Value not found for key [%s].", key));
        return toDate(text);
    }

    private Period loadCycleLength() {
        String key = resources.getString(R.string.pref_key_treatment_cycle_length);
        String text = preferences.getString(key, null);
        if (TextUtils.isEmpty(text))
            throw new IllegalStateException(format("Value not found for key [%s].", key));
        return DateTimeHelper.toPeriod(text);
    }

    private TreatmentCycleRepeatingModel loadRepeatingModel() {
        String text = preferences.getString(resources.getString(R.string.pref_key_treatment_repeating_model), "");

        if (text.equals(resources.getString(R.string.treatment_repeating_none)))
            return new NotRepeating();
        else if (text.equals(resources.getString(R.string.treatment_repeating_n_times)))
            return loadRepeatingNTimes();
        else if (text.equals(resources.getString(R.string.treatment_repeating_until_date)))
            return loadRepeatingUntilDate();
        else {
            Log.wtf(TAG, format("Unexpected treatment repeating model: [%s]. Using [%s] as default.",
                    text, NotRepeating.class.getSimpleName()));
            return new NotRepeating();
        }
    }


    private TreatmentCycleRepeatingModel loadRepeatingNTimes() {
        int n = preferences.getInt(resources.getString(R.string.pref_key_treatment_repeating_model_times), -1);
        if (n < 0) {
            Log.wtf(TAG, format("Unexpected n for [%s]: [%d]. Using [%s] as default.",
                    NTimes.class.getSimpleName(), n, NotRepeating.class.getSimpleName()));
            return new NotRepeating();
        }

        return new NTimes(n);
    }

    private TreatmentCycleRepeatingModel loadRepeatingUntilDate() {
        String key = resources.getString(R.string.pref_key_treatment_repeating_model_until_date);
        String text = preferences.getString(key, null);
        if (TextUtils.isEmpty(text)) {
            Log.wtf(TAG, format("Unexpected date for [%s]: [%s]. Using [%s] as default.",
                    NTimes.class.getSimpleName(), text, NotRepeating.class.getSimpleName()));
            return new NotRepeating();
        }

        return new UntilDate(toDate(text));
    }

    private Treatment buildDummyTreatment() {
        return new TreatmentBuilder()
                .buildEverlastingTreatment();
    }

    public static void setInstance(Therapist instance) {
        Therapist.instance = instance;
    }

}
