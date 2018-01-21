package name.leesah.nirvana.ui.settings;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.Locale;

import name.leesah.nirvana.BuildConfig;
import name.leesah.nirvana.R;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static java.lang.String.format;
import static java.util.Locale.getDefault;
import static name.leesah.nirvana.DebugTools.clearAllData;
import static name.leesah.nirvana.DebugTools.injectTestData;

/**
 * Created by sah on 2017-04-17.
 */

public class SettingsFragment extends PreferenceFragment {

    private Preference treatment;
    private Preference.OnPreferenceClickListener treatmentListener;
    private int clicksUntilDebugToolsEnabled = 8;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefscr_settings);
        initializeInformationSection();
        if (BuildConfig.DEBUG || isDebugToolsForceEnabled())
            initializeDebugToolsSection();

        treatment = findPreference(getString(R.string.pref_key_treatment));
        treatment.setOnPreferenceClickListener(treatmentListener);

    }

    private void initializeInformationSection() {
        addPreferencesFromResource(R.xml.prefscr_information);
        Preference about = findPreference(getString(R.string.pref_key_about));
        about.setSummary(BuildConfig.VERSION_NAME);
        if (!isDebugToolsForceEnabled())
            about.setOnPreferenceClickListener(preference -> {
                forceEnableDebugTools();
                return true;
            });
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

    public boolean isDebugToolsForceEnabled() {
        return getDefaultSharedPreferences(getContext()).getBoolean(getString(R.string.pref_key_debug_tools_force_enabled), false);
    }

    private void forceEnableDebugTools() {
        clicksUntilDebugToolsEnabled--;
        if (clicksUntilDebugToolsEnabled == 0) {
            initializeDebugToolsSection();
            getDefaultSharedPreferences(getContext()).edit().putBoolean(getString(R.string.pref_key_debug_tools_force_enabled), true).apply();
            makeText(getContext(), "Debug tools enabled.", LENGTH_SHORT).show();
        }
    }

    public void setTreatmentListener(Preference.OnPreferenceClickListener listener) {
        this.treatmentListener = listener;
        if (treatment != null)
            treatment.setOnPreferenceClickListener(listener);
    }

}
