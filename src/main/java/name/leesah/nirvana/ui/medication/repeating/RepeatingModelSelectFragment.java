package name.leesah.nirvana.ui.medication.repeating;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.reminding.RemindingModel;
import name.leesah.nirvana.model.medication.repeating.EveryNDays;
import name.leesah.nirvana.model.medication.repeating.RepeatingModel;
import name.leesah.nirvana.ui.medication.MedicationEditActivity;
import name.leesah.nirvana.ui.medication.reminding.RemindingModelEditFragment;

/**
 * Created by sah on 2017-04-15.
 */

public class RepeatingModelSelectFragment extends PreferenceFragment{


    private EverydayEditFragment everyday = new EverydayEditFragment();
    private DaysOfWeekEditFragment daysOfWeek = new DaysOfWeekEditFragment();
    private EveryNDaysEditFragment everyNDays = new EveryNDaysEditFragment();
    private FloatingActionButton saveButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_repeating_model);

        ListPreference models = (ListPreference) findPreference(getString(R.string.pref_key_medication_repeating_model));
        models.setOnPreferenceChangeListener((p, v) -> switchDetailsFragment(p, v.toString()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_model_with_details, container, false);
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

    public RepeatingModel readModel() {
        return getCurrentFragment().readModel();
    }

    private void replaceFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().replace(R.id.details_container, fragment).commit();
    }

    private RepeatingModelEditFragment getCurrentFragment() {
        return (RepeatingModelEditFragment) getFragmentManager().findFragmentById(R.id.details_container);
    }

    public void setValidityReportListener(MedicationEditActivity.ValidityReportListener listener) {
        everyday.setValidityReportListener(listener);
        daysOfWeek.setValidityReportListener(listener);
        everyNDays.setValidityReportListener(listener);
    }
}
