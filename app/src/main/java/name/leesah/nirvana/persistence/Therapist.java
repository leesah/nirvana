package name.leesah.nirvana.persistence;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import org.joda.time.LocalDate;
import org.joda.time.Period;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.treatment.EverlastingTreatment;
import name.leesah.nirvana.model.treatment.RecurringTreatment;
import name.leesah.nirvana.model.treatment.Treatment;
import name.leesah.nirvana.model.treatment.recurring.RecurringStrategy;

import static android.text.TextUtils.isEmpty;
import static java.lang.String.format;
import static name.leesah.nirvana.R.string.pref_key_treatment_enabled;
import static name.leesah.nirvana.R.string.pref_key_treatment_first_day;
import static name.leesah.nirvana.R.string.pref_key_treatment_recurring;
import static name.leesah.nirvana.utils.AdaptedGsonFactory.getGson;
import static name.leesah.nirvana.utils.DateTimeHelper.toDate;
import static name.leesah.nirvana.utils.DateTimeHelper.toPeriod;
import static name.leesah.nirvana.utils.DateTimeHelper.toText;

/**
 * Created by sah on 2016-12-11.
 */

public class Therapist extends DataHolder {

    private static final String TAG = Therapist.class.getSimpleName();
    public static final LocalDate DEFAULT_DAY_ZERO = new LocalDate(0);

    private Treatment treatment;
    private boolean cycleSupportEnabled;
    private boolean cacheUpToDate = false;

    public Therapist(Context context) {
        super(context);
    }

    public boolean isCycleSupportEnabled() {
        loadCache();
        return cycleSupportEnabled;
    }

    @NonNull
    public Treatment getTreatment() {
        loadCache();
        return treatment;
    }

    public void invalidate() {
        synchronized (this) {
            cacheUpToDate = false;
        }
    }

    private void loadCache() {
        synchronized (this) {
            if (cacheUpToDate)
                return;
            loadFromSharedPreferences();
            cacheUpToDate = true;
            Log.d(TAG, format("Loaded treatment: %s", treatment));
        }
    }

    private void loadFromSharedPreferences() {
        cycleSupportEnabled = preferences.getBoolean(
                resources.getString(pref_key_treatment_enabled), false);
        treatment = cycleSupportEnabled ?
                new RecurringTreatment(loadDayZero(), loadLength(), loadRecurring()) :
                new EverlastingTreatment(loadDayZero(toText(DEFAULT_DAY_ZERO)));
    }

    private LocalDate loadDayZero() {
        LocalDate dayZero = loadDayZero(null);
        if (dayZero == null)
            throw new IllegalStateException("Day zero is not set.");
        return dayZero;
    }

    private LocalDate loadDayZero(String defaultValue) {
        return toDate(preferences.getString(resources.getString(pref_key_treatment_first_day), defaultValue));
    }

    private Period loadLength() {
        String key = resources.getString(R.string.pref_key_treatment_cycle_length);
        String text = preferences.getString(key, null);
        if (isEmpty(text))
            throw new IllegalStateException("Length is not set.");
        return toPeriod(text);
    }

    private RecurringStrategy loadRecurring() {
        String text = preferences.getString(resources.getString(pref_key_treatment_recurring), null);
        if (isEmpty(text))
            throw new IllegalStateException("Recurring strategy is not set.");
        return getGson().fromJson(text, RecurringStrategy.class);
    }

}
