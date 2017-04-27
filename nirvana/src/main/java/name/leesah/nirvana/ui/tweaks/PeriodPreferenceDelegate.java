package name.leesah.nirvana.ui.tweaks;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;

import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.PeriodUnit;
import name.leesah.nirvana.utils.DateTimeHelper;

import static java.lang.String.format;
import static name.leesah.nirvana.model.PeriodUnit.DAY;
import static name.leesah.nirvana.model.PeriodUnit.MONTH;
import static name.leesah.nirvana.model.PeriodUnit.WEEK;

class PeriodPreferenceDelegate {
    private static final String TAG = PeriodPreference.class.getSimpleName();

    private static final List<PeriodUnit> PERIOD_UNITS = new ArrayList<PeriodUnit>(EnumSet.allOf(PeriodUnit.class));
    private final String[] displayedValuesPlural;
    private final String[] displayedValuesSingular;
    private final PeriodFormatter formatter;

    private final DialogPreference preference;
    private NumberPicker number;
    private NumberPicker unit;
    private String value = null;

    PeriodPreferenceDelegate(DialogPreference preference) {
        this.preference = preference;
        this.preference.setDialogLayoutResource(R.layout.period_picker);

        Context context = this.preference.getContext();
        displayedValuesSingular = context.getResources().getStringArray(R.array.period_unit_displayed_singular);
        displayedValuesPlural = context.getResources().getStringArray(R.array.period_unit_displayed_plural);
        formatter = new PeriodFormatterBuilder()
                .appendMonths().appendSuffix(" ").appendSuffix(context.getString(R.string.month), context.getString(R.string.months))
                .appendWeeks().appendSuffix(" ").appendSuffix(context.getString(R.string.week), context.getString(R.string.weeks))
                .appendDays().appendSuffix(" ").appendSuffix(context.getString(R.string.day), context.getString(R.string.days))
                .toFormatter();
    }

    @Nullable
    Period getPeriod() {
        return value == null ? null : DateTimeHelper.toPeriod(value);
    }

    void setPeriod(@NonNull Period period) {
        String newValue = DateTimeHelper.toText(period);
        onNewValue(newValue);
    }

    void onBindDialogView(View view) {
        number = (NumberPicker) view.findViewById(R.id.number);
        number.setMinValue(1);
        number.setMaxValue(99);
        number.setOnValueChangedListener((p, oldVal, newVal) -> toggleSingularPlural(oldVal, newVal));

        unit = (NumberPicker) view.findViewById(R.id.unit);
        unit.setMinValue(0);
        unit.setMaxValue(PERIOD_UNITS.size() - 1);
        unit.setDisplayedValues(displayedValuesSingular);

        if (value != null)
            updateWidgets(value);
    }

    void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            String newValue = DateTimeHelper.toPeriodAsString(number.getValue(), readUnit());
            onNewValue(newValue);
        }
    }

    void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        value = restorePersistedValue ? getPersistedString() : defaultValue.toString();
        if (value != null) {
            persistString(value);
            setSummary(value);
        }
    }

    String onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }


    private void persistString(String value) {
        if (shouldPersist()) {
            if (TextUtils.equals(value, getPersistedString())) {
                return;
            }

            SharedPreferences.Editor editor = getPreferenceManager().getSharedPreferences().edit();
            editor.putString(getKey(), value);
            editor.apply();
        }
    }

    private String getPersistedString() {
        if (!shouldPersist()) {
            return null;
        }

        return getPreferenceManager().getSharedPreferences().getString(getKey(), null);
    }

    private boolean shouldPersist() {
        return getPreferenceManager() != null && preference.isPersistent() && !TextUtils.isEmpty(getKey());
    }

    private PreferenceManager getPreferenceManager() {
        return preference.getPreferenceManager();
    }

    private String getKey() {
        return preference.getKey();
    }

    private void updateWidgets(String value) {
        try {
            Period period = DateTimeHelper.toPeriod(value);
            int days = period.getDays();
            if (days > 0) updateWidgets(days, PERIOD_UNITS.indexOf(DAY));

            int weeks = period.getWeeks();
            if (weeks > 0) updateWidgets(weeks, PERIOD_UNITS.indexOf(WEEK));

            int months = period.getMonths();
            if (months > 0) updateWidgets(months, PERIOD_UNITS.indexOf(MONTH));
        } catch (IllegalArgumentException e) {
            updateWidgets(0, 0);
        }
    }

    private void updateWidgets(int numberValue, int unitValue) {
        toggleSingularPlural(number.getValue(), numberValue);
        number.setValue(numberValue);
        unit.setValue(unitValue);
    }

    private PeriodUnit readUnit() {
        return PERIOD_UNITS.get(unit.getValue());
    }

    private void toggleSingularPlural(int oldVal, int newVal) {
        if (oldVal == 1 && newVal > 1)
            unit.setDisplayedValues(displayedValuesPlural);
        else if (oldVal > 1 && newVal == 1)
            unit.setDisplayedValues(displayedValuesSingular);
    }

    private void onNewValue(String newValue) {
        if (callChangeListener(newValue)) {
            value = newValue;
            setSummary(value);
            persistString(newValue);
        }
    }

    private void setSummary(CharSequence original) {
        try {
            preference.setSummary(translateSummary(original));
        } catch (IllegalArgumentException e) {
            Log.wtf(TAG, format("Unexpected format: [%s].", original));
            preference.setSummary(original);
        }
    }

    private String translateSummary(CharSequence original) {
        return formatter.print(DateTimeHelper.toPeriod(original));
    }

    private boolean callChangeListener(String newValue) {
        Preference.OnPreferenceChangeListener listener = preference.getOnPreferenceChangeListener();
        return listener == null || listener.onPreferenceChange(preference, newValue);
    }
}