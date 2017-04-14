package name.leesah.nirvana.ui.settings;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.MenuItem;

import org.joda.time.LocalDate;
import org.joda.time.Period;

import name.leesah.nirvana.R;
import name.leesah.nirvana.ui.components.DatePreference;
import name.leesah.nirvana.ui.components.PeriodPreference;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * This fragment shows general preferences only. It is used when the
 * activity is showing setValueIfNotPersisted two-pane settings UI.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TreatmentPreferenceFragment extends PreferenceFragment {

    public static final String TAG = TreatmentPreferenceFragment.class.getSimpleName();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_treatment);
        setHasOptionsMenu(true);

        initializePreferences();
    }

    private void initializePreferences() {
        initializeFirstDay();
        initializeLength();
        initializeRepeatingModel();
    }

    private void initializeFirstDay() {
        String key = getString(R.string.pref_key_treatment_first_day);
        if (!getDefaultSharedPreferences(getContext()).contains(key)) {
            Log.d(TAG, "[First day] not yet persisted, setting to today.");
            ((DatePreference) findPreference(key)).setValue(LocalDate.now());
        }
    }

    private void initializeLength() {
        String key = getString(R.string.pref_key_treatment_cycle_length);
        if (!getDefaultSharedPreferences(getContext()).contains(key)) {
            Log.d(TAG, "[Cycle length] not yet persisted, setting to one month.");
            ((PeriodPreference) findPreference(key)).setPeriod(Period.months(1));
        }
    }

    private void initializeRepeatingModel() {
        String key = getString(R.string.pref_key_treatment_repeating_model);
        ListPreference listPreference = (ListPreference) findPreference(key);
        listPreference.setOnPreferenceChangeListener((preference, value) -> switchRepeatingModel(value.toString()));

        if (!getDefaultSharedPreferences(getContext()).contains(key)) {
            String entryName = getResources().getResourceEntryName(R.xml.pref_treatment_repeating_none);
            Log.d(TAG, String.format("[Repeating model] not yet persisted, setting to %s.", entryName));
            listPreference.setValue(entryName);
        }
    }

    private boolean switchRepeatingModel(final String resourceName) {
        int resourceId = getResources().getIdentifier(resourceName, "xml", getString(R.string.package_name));
        if (resourceId == 0)
            return false;

        Preference existing = findPreference(getString(R.string.pref_key_treatment_repeating_model_details));
        if (existing != null)
            getPreferenceScreen().removePreference(existing);

        addPreferencesFromResource(resourceId);
        return true;
    }

}
