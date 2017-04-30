package name.leesah.nirvana.ui.medication.reminding;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.support.annotation.Nullable;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.reminding.AtCertainHours;
import name.leesah.nirvana.model.medication.reminding.EveryNHours;
import name.leesah.nirvana.model.medication.reminding.RemindingStrategy;
import name.leesah.nirvana.ui.medication.MedicationActivity;
import name.leesah.nirvana.ui.widget.ListAndDetailsPreferenceFragment;

/**
 * Created by sah on 2017-04-15.
 */

public class RemindingStrategySelectFragment extends ListAndDetailsPreferenceFragment<RemindingModelEditFragment> {

    public static final String KEY_MODEL = "name.leesah.nirvana:key:MODEL";
    private CertainHoursEditFragment certainHours = new CertainHoursEditFragment();
    private EveryNHoursEditFragment everyNHours = new EveryNHoursEditFragment();
    private MedicationActivity.ValidityReportListener validityReportListener;
    private RemindingStrategy editingExisting;
    private ListPreference models;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefscr_medication_reminding);

        models = (ListPreference) findPreference(getString(R.string.pref_key_medication_reminding));
        models.setOnPreferenceChangeListener((p, v) -> switchDetailsFragment(p, v.toString()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_MODEL)) {
            models.setValue(savedInstanceState.getString(KEY_MODEL));
        } else if (editingExisting != null) {
            if (editingExisting instanceof AtCertainHours) {
                models.setValue(getString(R.string.medication_reminding_certain_hours));
                certainHours.setEditingExisting((AtCertainHours) editingExisting);
            }
            if (editingExisting instanceof EveryNHours) {
                models.setValue(getString(R.string.medication_reminding_every_n_hours));
                everyNHours.setEditingExisting((EveryNHours) editingExisting);
            }
            editingExisting = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_MODEL, models.getValue());
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

    public RemindingStrategy readModel() {
        return getCurrentFragment().readModel();
    }

    public void setValidityReportListener(MedicationActivity.ValidityReportListener listener) {
        certainHours.setValidityReportListener(listener);
        everyNHours.setValidityReportListener(listener);
    }

    public void setEditingExisting(RemindingStrategy editExisting) {
        this.editingExisting = editExisting;
    }
}
