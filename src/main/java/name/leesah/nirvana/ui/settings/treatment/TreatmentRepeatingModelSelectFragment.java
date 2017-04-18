package name.leesah.nirvana.ui.settings.treatment;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import name.leesah.nirvana.R;
import name.leesah.nirvana.ui.settings.treatment.NTimesDetailsFragment;
import name.leesah.nirvana.ui.settings.treatment.NotRepeatingDetailsFragment;
import name.leesah.nirvana.ui.settings.treatment.TreatmentRepeatingModelEditFragment;
import name.leesah.nirvana.ui.settings.treatment.UntilDateDetailsFragment;
import name.leesah.nirvana.ui.tweaks.ListAndDetailsPreferenceFragment;

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

        models = (ListPreference) findPreference(getString(R.string.pref_key_treatment_repeating_model));
        models.setOnPreferenceChangeListener((p, v) -> switchDetailsFragment(p, v.toString()));
    }

    private boolean switchDetailsFragment(Preference preference, String choice) {
        preference.setSummary(choice);

        if (choice.equals(getString(R.string.treatment_repeating_none))) {
            replaceFragment(notRepeating);
            return true;
        } else if (choice.equals(getString(R.string.treatment_repeating_n_times))) {
            replaceFragment(nTimes);
            return true;
        } else if (choice.equals(getString(R.string.treatment_repeating_until_date))) {
            replaceFragment(untilDate);
            return true;
        }
        return false;
    }


}
