package name.leesah.nirvana.ui.medication.reminding;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.support.annotation.Nullable;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.reminding.AtCertainHours;
import name.leesah.nirvana.model.medication.reminding.EveryNHours;
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
    private RemindingModel editExisting;
    private ListPreference models;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefscr_reminding_model);

        models = (ListPreference) findPreference(getString(R.string.pref_key_medication_reminding_model));
        models.setOnPreferenceChangeListener((p, v) -> switchDetailsFragment(p, v.toString()));

        if (editExisting != null) {
            if (editExisting instanceof AtCertainHours) {
                models.setValue(getString(R.string.medication_reminding_certain_hours));
                certainHours.setEditingExisting((AtCertainHours) editExisting);
            }
            if (editExisting instanceof EveryNHours) {
                models.setValue(getString(R.string.medication_reminding_every_n_hours));
                everyNHours.setEditingExsiting((EveryNHours)editExisting);
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //TODO
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("name.leesah.nirvana:key:MODEL", models.getValue());
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

    public void setEditingExisting(RemindingModel editExisting) {
        this.editExisting = editExisting;
    }
}
