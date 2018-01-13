package name.leesah.nirvana.ui.settings;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import name.leesah.nirvana.BuildConfig;
import name.leesah.nirvana.R;

import static name.leesah.nirvana.DebugTools.clearAllData;
import static name.leesah.nirvana.DebugTools.injectTestData;

/**
 * Created by sah on 2017-04-17.
 */

public class SettingsFragment extends PreferenceFragment {

    private Preference treatment;
    private Preference.OnPreferenceClickListener treatmentListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefscr_settings);
        if (BuildConfig.DEBUG)
            initializeDebugToolsSection();
        initializeInformationSection();

        treatment = findPreference(getString(R.string.pref_key_treatment));

        treatment.setOnPreferenceClickListener(treatmentListener);

    }

    private void initializeDebugToolsSection() {
        addPreferencesFromResource(R.xml.prefscr_debug_tools);
        findPreference("debug: inject").setOnPreferenceClickListener(preference -> {
            injectTestData(getContext());
            return true;
        });
        findPreference("debug: clear").setOnPreferenceClickListener(preference -> {
            clearAllData(getContext());
            return true;
        });
    }

    private void initializeInformationSection() {
        addPreferencesFromResource(R.xml.prefscr_information);
        findPreference(getString(R.string.pref_key_about)).setSummary(BuildConfig.VERSION_NAME);
    }

    public void setTreatmentListener(Preference.OnPreferenceClickListener listener) {
        this.treatmentListener = listener;
        if (treatment != null)
            treatment.setOnPreferenceClickListener(listener);
    }

}
