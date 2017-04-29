package name.leesah.nirvana.ui.settings.treatment;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.support.annotation.Nullable;

import name.leesah.nirvana.R;
import name.leesah.nirvana.ui.widget.ListAndDetailsPreferenceFragment;

/**
 * Created by sah on 2017-04-17.
 */

public class TreatmentRepeatingModelSelectFragment extends ListAndDetailsPreferenceFragment<TreatmentRepeatingModelEditFragment> {
    private ListPreference models;
    private NotRepeatingDetailsFragment notRepeating = new NotRepeatingDetailsFragment();
    private NTimesDetailsFragment nTimes = new NTimesDetailsFragment();
    private UntilDateDetailsFragment untilDate = new UntilDateDetailsFragment();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefscr_settings_treatment_repeating_model);

        models = (ListPreference) findPreference(getString(R.string.pref_key_treatment_recurring));
        models.setOnPreferenceChangeListener((p, v) -> switchDetailsFragment(p, v.toString()));
    }

    private boolean switchDetailsFragment(Preference preference, String choice) {
        preference.setSummary(choice);

        if (choice.equals(getString(R.string.treatment_recurring_none))) {
            replaceFragment(notRepeating);
            return true;
        } else if (choice.equals(getString(R.string.treatment_recurring_n_times))) {
            replaceFragment(nTimes);
            return true;
        } else if (choice.equals(getString(R.string.treatment_recurring_until_date))) {
            replaceFragment(untilDate);
            return true;
        }
        return false;
    }


}
