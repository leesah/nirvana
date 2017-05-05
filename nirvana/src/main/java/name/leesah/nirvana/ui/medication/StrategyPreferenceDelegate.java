package name.leesah.nirvana.ui.medication;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.google.gson.reflect.TypeToken;

import java.util.Objects;

import name.leesah.nirvana.model.medication.reminding.RemindingStrategy;
import name.leesah.nirvana.model.medication.repeating.RepeatingStrategy;
import name.leesah.nirvana.model.medication.starting.StartingStrategy;
import name.leesah.nirvana.model.medication.stopping.StoppingStrategy;

import static android.text.TextUtils.isEmpty;
import static name.leesah.nirvana.utils.AdaptedGsonFactory.getGson;

/**
 * Created by sah on 2017-04-28.
 */

public abstract class StrategyPreferenceDelegate<ValueType> implements SharedPreferences.OnSharedPreferenceChangeListener {

    protected final Preference preference;
    private final TypeToken<ValueType> type;

    private ValueType value;

    protected StrategyPreferenceDelegate(Preference preference, TypeToken<ValueType> type) {
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(preference.getKey())) {
            value = getPersistedValue();
            preference.setSummary(buildSummary(getValue()));
        }
    }

    public static class Reminding extends StrategyPreferenceDelegate<RemindingStrategy> {

        public Reminding(Preference preference) {
            super(preference, new TypeToken<RemindingStrategy>() {
            });
        }

        @Override
        protected String buildSummary(RemindingStrategy value) {
            return value == null ? null : value.toString(preference.getContext());
        }
    }

    public static class Repeating extends StrategyPreferenceDelegate<RepeatingStrategy> {

        public Repeating(Preference preference) {
            super(preference, new TypeToken<RepeatingStrategy>() {
            });
        }

        @Override
        protected String buildSummary(RepeatingStrategy value) {
            return value == null ? null : value.toString(preference.getContext());
        }
    }

    public static class Starting extends StrategyPreferenceDelegate<StartingStrategy> {

        public Starting(Preference preference) {
            super(preference, new TypeToken<StartingStrategy>() {
            });
        }

        @Override
        protected String buildSummary(StartingStrategy value) {
            return value == null ? null : value.toString(preference.getContext());
        }
    }

    public static class Stopping extends StrategyPreferenceDelegate<StoppingStrategy> {

        public Stopping(Preference preference) {
            super(preference, new TypeToken<StoppingStrategy>() {
            });
        }

        @Override
        protected String buildSummary(StoppingStrategy value) {
            return value == null ? null : value.toString(preference.getContext());
        }
    }


}
