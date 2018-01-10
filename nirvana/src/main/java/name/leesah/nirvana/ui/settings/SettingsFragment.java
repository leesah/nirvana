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
    private Preference notification;
    private Preference.OnPreferenceClickListener treatmentListener;
    private Preference.OnPreferenceClickListener notificationListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefscr_settings);
        if (BuildConfig.DEBUG)
            initializeDebugToolsSection();
        initializeInformationSection();

        treatment = findPreference(getString(R.string.pref_key_treatment));
        notification = findPreference(getString(R.string.pref_key_notification));

        treatment.setOnPreferenceClickListener(treatmentListener);
        notification.setOnPreferenceClickListener(notificationListener);

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

    public void setNotificationListener(Preference.OnPreferenceClickListener listener) {
        this.notificationListener = listener;
        if (notification != null)
            notification.setOnPreferenceClickListener(listener);
    }
}
