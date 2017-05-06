package name.leesah.nirvana.ui.settings.treatment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import org.joda.time.Period;

import name.leesah.nirvana.R;
import name.leesah.nirvana.data.Therapist;
import name.leesah.nirvana.model.treatment.recurring.NTimes;
import name.leesah.nirvana.ui.preference.DatePreference;
import name.leesah.nirvana.ui.preference.PeriodPreference;

import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static org.joda.time.Period.weeks;

/**
 * Created by sah on 2017-04-17.
 */

public class TreatmentSettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final Period TWO_WEEKS = weeks(2);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefscr_settings_treatment);
        getPreferenceManager().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DatePreference dayZero = (DatePreference) findPreference(getString(R.string.pref_key_treatment_first_day));
        PeriodPreference length = (PeriodPreference) findPreference(getString(R.string.pref_key_treatment_cycle_length));
        RecurringStrategyPreference recurringStrategy = (RecurringStrategyPreference) findPreference(getString(R.string.pref_key_treatment_recurring));

        if (dayZero.getDate() == null)
            dayZero.setDate(today());

        if (length.getPeriod() == null)
            length.setPeriod(TWO_WEEKS);

        if (recurringStrategy.getStrategy() == null)
            recurringStrategy.setStrategy(new NTimes(1));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_key_treatment_enabled)))
            Therapist.reset();
    }
}
