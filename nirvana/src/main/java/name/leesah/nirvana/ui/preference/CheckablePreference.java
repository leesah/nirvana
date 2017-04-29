package name.leesah.nirvana.ui.preference;

import android.preference.Preference;

/**
 * Created by sah on 2017-04-28.
 */

public interface CheckablePreference {
    boolean isChecked();

    void setChecked(boolean checked);

    String getSwitchTextOn();

    void setSwitchTextOn(String switchTextOn);

    String getSwitchTextOff();

    void setSwitchTextOff(String switchTextOff);

    String getSummaryOff();

    void setSummaryOff(String summaryOff);

    String getValueOff();

    void setValueOff(String valueOff);

    void setOnPreferenceChangeListener(Preference.OnPreferenceChangeListener listener);

    Object getValue();
}
