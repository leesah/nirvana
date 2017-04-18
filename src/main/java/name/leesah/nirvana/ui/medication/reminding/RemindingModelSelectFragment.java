package name.leesah.nirvana.ui.medication.reminding;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.reminding.RemindingModel;
import name.leesah.nirvana.ui.tweaks.ListAndDetailsPreferenceFragment;
import name.leesah.nirvana.ui.medication.MedicationEditActivity;

/**
 * Created by sah on 2017-04-15.
 */

public class RemindingModelSelectFragment extends ListAndDetailsPreferenceFragment<RemindingModelEditFragment> {

    private CertainHoursEditFragment certainHours = new CertainHoursEditFragment();
    private EveryNHoursEditFragment everyNHours = new EveryNHoursEditFragment();
    private MedicationEditActivity.ValidityReportListener validityReportListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefscr_reminding_model);

        ListPreference models = (ListPreference) findPreference(getString(R.string.pref_key_medication_reminding_model));
        models.setOnPreferenceChangeListener((p, v) -> switchDetailsFragment(p, v.toString()));
    }

   private boolean switchDetailsFragment(Preference preference, String choice) {
        preference.setSummary(choice);

        if (choice.equals(getString(R.string.medication_reminding_certain_hours))) {
            replaceFragment(certainHours);
            return true;
        } else if (choice.equals(getString(R.string.medication_reminding_every_n_hours))) {
            replaceFragment(everyNHours);
            return true;
        }
        return false;
    }

    public RemindingModel readModel() {
        return getCurrentFragment().readModel();
    }

    public void setValidityReportListener(MedicationEditActivity.ValidityReportListener listener) {
            certainHours.setValidityReportListener(listener);
            everyNHours.setValidityReportListener(listener);
    }

}
