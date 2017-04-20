package name.leesah.nirvana.ui.settings.treatment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import org.joda.time.Period;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.treatment.repeating.TreatmentCycleRepeatingModel;
import name.leesah.nirvana.ui.tweaks.DatePreference;
import name.leesah.nirvana.ui.tweaks.PeriodPreference;

import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static org.joda.time.Period.weeks;

/**
 * Created by sah on 2017-04-17.
 */

public class TreatmentSettingsFragment extends PreferenceFragment {

    public static final Period TWO_WEEKS = weeks(2);
    private DatePreference firstDayPreference;
    private PeriodPreference lengthPreference;
    private Preference repeatingModelPreference;
    private Preference.OnPreferenceClickListener repeatingModelListener;
    private TreatmentCycleRepeatingModel repeatingModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefscr_settings_treatment);

        firstDayPreference = (DatePreference) findPreference(getString(R.string.pref_key_treatment_first_day));
        lengthPreference = (PeriodPreference) findPreference(getString(R.string.pref_key_treatment_cycle_length));
        repeatingModelPreference = findPreference(getString(R.string.pref_key_treatment_repeating_model));

        if (firstDayPreference.getDate() == null)
            firstDayPreference.setDate(today());

        if (lengthPreference.getPeriod() == null)
            lengthPreference.setPeriod(TWO_WEEKS);

        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        if (!sharedPreferences.contains(getString(R.string.pref_key_treatment_repeating_model)))
            sharedPreferences.edit().putString(getString(R.string.pref_key_treatment_repeating_model), getString(R.string.treatment_repeating_none)).apply();
        repeatingModelPreference.setOnPreferenceClickListener(repeatingModelListener);
    }

    public void setRepeatingModelListener(Preference.OnPreferenceClickListener listener) {
        this.repeatingModelListener = listener;
        if (repeatingModelPreference != null)
            repeatingModelPreference.setOnPreferenceClickListener(listener);
    }
}
