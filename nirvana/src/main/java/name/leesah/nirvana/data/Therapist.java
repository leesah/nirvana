package name.leesah.nirvana.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import org.joda.time.LocalDate;
import org.joda.time.Period;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.treatment.EverlastingTreatment;
import name.leesah.nirvana.model.treatment.RecurringTreatment;
import name.leesah.nirvana.model.treatment.Treatment;
import name.leesah.nirvana.model.treatment.recurring.NTimes;
import name.leesah.nirvana.model.treatment.recurring.RecurringStrategy;
import name.leesah.nirvana.model.treatment.recurring.UntilDate;

import static android.text.TextUtils.isEmpty;
import static java.lang.String.format;
import static java.util.Locale.US;
import static name.leesah.nirvana.utils.DateTimeHelper.toDate;
import static name.leesah.nirvana.utils.DateTimeHelper.toPeriod;
import static name.leesah.nirvana.utils.DateTimeHelper.toText;

/**
 * Created by sah on 2016-12-11.
 */

public class Therapist extends DataHolder {

    private static final String TAG = Therapist.class.getSimpleName();
    public static final LocalDate DEFAULT_DAY_ZERO = new LocalDate(0);

    private static Therapist instance;
    private Treatment treatment;
    private boolean cycleSupportEnabled;
    private boolean cacheUpToDate = false;

    private Therapist(Context context) {
        super(context);
    }

    public static Therapist getInstance(Context context) {
        if (instance == null)
            instance = new Therapist(context);
        return instance;
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
                resources.getString(R.string.pref_key_treatment_enabled), false);
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
        return toDate(preferences.getString(resources.getString(R.string.pref_key_treatment_first_day), defaultValue));
    }

    private Period loadLength() {
        String key = resources.getString(R.string.pref_key_treatment_cycle_length);
        String text = preferences.getString(key, null);
        if (isEmpty(text))
            throw new IllegalStateException("Length is not set.");
        return toPeriod(text);
    }

    private RecurringStrategy loadRecurring() {
        String text = preferences.getString(resources.getString(R.string.pref_key_treatment_recurring), "");

        if (text.equals(resources.getString(R.string.treatment_recurring_n_times)))
            return loadRepeatingNTimes();
        else if (text.equals(resources.getString(R.string.treatment_recurring_until_date)))
            return loadRepeatingUntilDate();
        else {
            throw new IllegalStateException(format("Unexpected treatment recurring strategy: [%s].", text));
        }
    }


    private NTimes loadRepeatingNTimes() {
        int n = preferences.getInt(resources.getString(R.string.pref_key_treatment_recurring_n_times), -1);
        if (n < 0) {
            String msg = format(US, "Unexpected n for [%s]: [%d].", NTimes.class.getSimpleName(), n);
            Log.wtf(TAG, msg);
            throw new IllegalStateException(msg);
        }
        return new NTimes(n);
    }

    private UntilDate loadRepeatingUntilDate() {
        String key = resources.getString(R.string.pref_key_treatment_recurring_until_date);
        String text = preferences.getString(key, null);
        if (isEmpty(text)) {
            String msg = format("Unexpected date for [%s]: [%s].", UntilDate.class.getSimpleName(), text);
            Log.wtf(TAG, msg);
            throw new IllegalStateException(msg);
        }

        return new UntilDate(toDate(text));
    }

    public static void reset() {
        Therapist.instance = null;
    }

}
