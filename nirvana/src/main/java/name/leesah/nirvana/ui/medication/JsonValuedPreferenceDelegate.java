package name.leesah.nirvana.ui.medication;

import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Objects;

import static android.text.TextUtils.isEmpty;
import static name.leesah.nirvana.utils.AdaptedGsonFactory.getGson;

/**
 * Created by sah on 2017-04-28.
 */

public abstract class JsonValuedPreferenceDelegate<ValueType> {

    protected final Preference preference;
    private final TypeToken<ValueType> type;

    private ValueType value;

    protected JsonValuedPreferenceDelegate(Preference preference, TypeToken<ValueType> type) {
        this.preference = preference;
        this.type = type;
    }

    public void setValue(@Nullable ValueType newValue) {
        if (callChangeListener(newValue)) {
            value = newValue;
            preference.setSummary(buildSummary(value));
            persistValue(newValue);
        }
    }

    @Nullable
    public ValueType getValue() {
        return value;
    }

    protected abstract String buildSummary(ValueType value);

    public Object onGetDefaultValue(TypedArray a, int index) {
        return (a.getString(index));
    }

    public void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        value = restoreValue ? getPersistedValue() : fromJson(defaultValue.toString());
        if (value != null) {
            persistValue(value);
            preference.setSummary(buildSummary(value));
        }
    }

    private void persistValue(ValueType value) {
        if (shouldPersist()) {
            if (Objects.equals(value, getPersistedValue())) return;

            getSharedPreferences().edit()
                    .putString(getKey(), getGson().toJson(value))
                    .apply();
        }
    }

    private ValueType getPersistedValue() {
        if (!shouldPersist()) return null;

        String json = getSharedPreferences().getString(getKey(), null);
        return json == null ? null : fromJson(json);
    }

    private ValueType fromJson(String json) {
        return getGson().fromJson(json, type.getType());
    }

    private boolean shouldPersist() {
        return preference.getPreferenceManager() != null
                && preference.isPersistent()
                && !isEmpty(getKey());
    }

    private PreferenceManager getPreferenceManager() {
        return preference.getPreferenceManager();
    }

    private SharedPreferences getSharedPreferences() {
        return getPreferenceManager().getSharedPreferences();
    }

    private String getKey() {
        return preference.getKey();
    }

    private boolean callChangeListener(ValueType newValue) {
        Preference.OnPreferenceChangeListener listener = preference.getOnPreferenceChangeListener();
        return listener == null || listener.onPreferenceChange(preference, newValue);
    }

}
