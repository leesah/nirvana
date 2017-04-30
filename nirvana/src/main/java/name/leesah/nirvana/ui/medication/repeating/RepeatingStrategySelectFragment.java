package name.leesah.nirvana.ui.medication.repeating;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.repeating.EveryNDays;
import name.leesah.nirvana.model.medication.repeating.Everyday;
import name.leesah.nirvana.model.medication.repeating.RepeatingStrategy;
import name.leesah.nirvana.ui.medication.MedicationActivity;
import name.leesah.nirvana.ui.widget.ListAndDetailsPreferenceFragment;

/**
 * Created by sah on 2017-04-15.
 */

public class RepeatingStrategySelectFragment extends ListAndDetailsPreferenceFragment<RepeatingModelEditFragment> {

    public static final String KEY_MODEL = "name.leesah.nirvana:key:MODEL";
    private EverydayEditFragment everyday = new EverydayEditFragment();
    private DaysOfWeekEditFragment daysOfWeek = new DaysOfWeekEditFragment();
    private EveryNDaysEditFragment everyNDays = new EveryNDaysEditFragment();
    private FloatingActionButton saveButton;
    private RepeatingStrategy editingExisting;
    private ListPreference models;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefscr_medication_repeating);

        models = (ListPreference) findPreference(getString(R.string.pref_key_medication_repeating));
        models.setOnPreferenceChangeListener((p, v) -> switchDetailsFragment(p, v.toString()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_MODEL)) {
            models.setValue(savedInstanceState.getString(KEY_MODEL));
        } else if (editingExisting != null) {
            if (editingExisting instanceof Everyday) {
                models.setValue(getString(R.string.medication_repeating_everyday));
                everyday.setEditingExisting((Everyday) editingExisting);
            }
            if (editingExisting instanceof EveryNDays) {
                models.setValue(getString(R.string.medication_repeating_every_n_days));
                everyNDays.setEditingExisting((EveryNDays) editingExisting);
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

        if (choice.equals(getString(R.string.medication_repeating_everyday))) {
            replaceFragment(everyday);
            return true;
        } else if (choice.equals(getString(R.string.medication_repeating_days_of_week))) {
            replaceFragment(daysOfWeek);
            return true;
        } else if (choice.equals(getString(R.string.medication_repeating_every_n_days))) {
            replaceFragment(everyNDays);
            return true;
        }
        return false;
    }

    public RepeatingStrategy readModel() {
        return getCurrentFragment().readModel();
    }

    public void setValidityReportListener(MedicationActivity.ValidityReportListener listener) {
        everyday.setValidityReportListener(listener);
        daysOfWeek.setValidityReportListener(listener);
        everyNDays.setValidityReportListener(listener);
    }

    public void setEditingExisting(RepeatingStrategy editingExisting) {
        this.editingExisting = editingExisting;
    }
}
