package name.leesah.nirvana.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import org.joda.time.LocalDate;
import org.joda.time.Period;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.treatment.Treatment;
import name.leesah.nirvana.model.treatment.TreatmentBuilder;
import name.leesah.nirvana.model.treatment.TreatmentCycle;
import name.leesah.nirvana.model.treatment.repeating.NotRepeating;
import name.leesah.nirvana.model.treatment.repeating.RepeatingNTimes;
import name.leesah.nirvana.model.treatment.repeating.RepeatingUntilDate;
import name.leesah.nirvana.model.treatment.repeating.TreatmentCycleRepeatingModel;
import name.leesah.nirvana.utils.DateTimeHelper;

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
            treatmentCache = loadTreatmentFromSharedPreferences(preferences, resources);
        } else {
            treatmentCache = buildDummyTreatment();
        }

        Log.d(TAG, String.format("Loaded treatment: %s", treatmentCache));
    }

    private Treatment loadTreatmentFromSharedPreferences(SharedPreferences sharedPreferences, Resources resources) {
        return new TreatmentBuilder()
                .setFirstDay(loadLocalDate(sharedPreferences, resources))
                .setCycleLength(loadCycleLength(sharedPreferences, resources))
                .setTreatmentCycleRepeatingModel(loadRepeatingModel(sharedPreferences, resources))
                .build();
    }

    private LocalDate loadLocalDate(SharedPreferences sharedPreferences, Resources resources) {
        String key = resources.getString(R.string.pref_key_treatment_first_day);
        String text = sharedPreferences.getString(key, null);
        if (TextUtils.isEmpty(text))
            throw new IllegalStateException(String.format("Value not found for key [%s].", key));
        return DateTimeHelper.toDate(text);
    }

    private Period loadCycleLength(SharedPreferences sharedPreferences, Resources resources) {
        String key = resources.getString(R.string.pref_key_treatment_cycle_length);
        String text = sharedPreferences.getString(key, null);
        if (TextUtils.isEmpty(text))
            throw new IllegalStateException(String.format("Value not found for key [%s].", key));
        return DateTimeHelper.toPeriod(text);
    }

    private TreatmentCycleRepeatingModel loadRepeatingModel(SharedPreferences sharedPreferences, Resources resources) {
        String key = resources.getString(R.string.pref_key_treatment_repeating_model);
        String text = sharedPreferences.getString(key, null);
        if (TextUtils.isEmpty(text))
            throw new IllegalStateException(String.format("Value not found for key [%s].", key));

        int resId = resources.getIdentifier(text, "xml", resources.getString(R.string.package_name));
        switch (resId) {
            default:
            case R.xml.pref_treatment_repeating_none:
                return new NotRepeating();
            case R.xml.pref_treatment_repeating_n_times:
                return loadRepeatingNTimes(sharedPreferences, resources);
            case R.xml.pref_treatment_repeating_until_date:
                return loadRepeatingUntilDate(sharedPreferences, resources);
        }
    }

    private TreatmentCycleRepeatingModel loadRepeatingNTimes(SharedPreferences sharedPreferences, Resources resources) {
        String key = resources.getString(R.string.pref_key_treatment_repeating_details_times);
        int n = sharedPreferences.getInt(key, -1);
        if (n < 0)
            throw new IllegalStateException(String.format("Value not found for key [%s].", key));

        return new RepeatingNTimes(n);
    }

    private TreatmentCycleRepeatingModel loadRepeatingUntilDate(SharedPreferences sharedPreferences, Resources resources) {
        String key = resources.getString(R.string.pref_key_treatment_repeating_details_until_date);
        String text = sharedPreferences.getString(key, null);
        if (TextUtils.isEmpty(text))
            throw new IllegalStateException(String.format("Value not found for key [%s].", key));
        LocalDate lastDay = DateTimeHelper.toDate(text);

        return new RepeatingUntilDate(lastDay);
    }

    private Treatment buildDummyTreatment() {
        return new TreatmentBuilder()
                .buildEverlastingTreatment();
    }

    public static void setInstance(Therapist instance) {
        Therapist.instance = instance;
    }

}
