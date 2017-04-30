package name.leesah.nirvana.ui.preference;

import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import static android.text.TextUtils.isEmpty;

/**
 * Created by sah on 2017-04-28.
 */

abstract class StringValuedDialogPreferenceDelegate {

    private final DialogPreference preference;

    private String value;

    StringValuedDialogPreferenceDelegate(DialogPreference preference) {
        this.preference = preference;
    }

    protected Object onGetDefaultValue(TypedArray a, int index) {
        return (a.getString(index));
    }

    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        value = restoreValue ? getPersistedString() : defaultValue.toString();
        if (value != null) {
            persistString(value);
            preference.setSummary(value);
        }
    }

    protected void persistString(String value) {
        if (shouldPersist()) {
            if (TextUtils.equals(value, getPersistedString())) {
                return;
            }

            SharedPreferences.Editor editor = getSharedPreferences().edit();
            editor.putString(getKey(), value);
            editor.apply();
        }
    }

    private String getPersistedString() {
        if (!shouldPersist()) {
            return null;
        }

        return getSharedPreferences().getString(getKey(), null);
    }

    private SharedPreferences getSharedPreferences() {
        return getPreferenceManager().getSharedPreferences();
    }

    private boolean shouldPersist() {
        return preference.getPreferenceManager() != null
                && preference.isPersistent()
                && !isEmpty(getKey());
    }

    private PreferenceManager getPreferenceManager() {
        return preference.getPreferenceManager();
    }

    private String getKey() {
        return preference.getKey();
    }

    protected abstract String translateSummary(CharSequence original);

    void updateValue(String newValue) {
        if (callChangeListener(newValue)) {
            value = newValue;
            preference.setSummary(value);
            persistString(newValue);
        }
    }

    protected String getValue() {
        return value;
    }

    private boolean callChangeListener(String newValue) {
        Preference.OnPreferenceChangeListener listener = preference.getOnPreferenceChangeListener();
        return listener == null || listener.onPreferenceChange(preference, newValue);
    }
}
