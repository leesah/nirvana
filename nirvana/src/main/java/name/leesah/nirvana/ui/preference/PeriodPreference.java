package name.leesah.nirvana.ui.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;

import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.ArrayList;
import java.util.List;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.PeriodUnit;
import name.leesah.nirvana.utils.DateTimeHelper;

import static java.lang.String.format;
import static java.util.EnumSet.allOf;
import static name.leesah.nirvana.model.PeriodUnit.DAY;
import static name.leesah.nirvana.model.PeriodUnit.MONTH;
import static name.leesah.nirvana.model.PeriodUnit.WEEK;
import static name.leesah.nirvana.utils.DateTimeHelper.toPeriod;
import static name.leesah.nirvana.utils.DateTimeHelper.toPeriodAsString;

/**
 * Created by sah on 2016-12-10.
 */

public class PeriodPreference extends DialogPreference {

    private static final String TAG = PeriodPreference.class.getSimpleName();

    public static final List<PeriodUnit> PERIOD_UNITS = new ArrayList<>(allOf(PeriodUnit.class));
    private final String[] displayedValuesPlural;
    private final String[] displayedValuesSingular;
    private final PeriodFormatter periodFormatter;

    private NumberPicker number;
    private NumberPicker unit;
    private String value = null;

    public PeriodPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.period_picker);

        displayedValuesSingular = context.getResources().getStringArray(R.array.period_unit_displayed_singular);
        displayedValuesPlural = context.getResources().getStringArray(R.array.period_unit_displayed_plural);
        periodFormatter = new PeriodFormatterBuilder()
                .appendMonths().appendSuffix(" ").appendSuffix(context.getString(R.string.month), context.getString(R.string.months))
                .appendWeeks().appendSuffix(" ").appendSuffix(context.getString(R.string.week), context.getString(R.string.weeks))
                .appendDays().appendSuffix(" ").appendSuffix(context.getString(R.string.day), context.getString(R.string.days))
                .toFormatter();
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

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

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            String newValue = toPeriodAsString(number.getValue(), readUnit());
            onNewValue(newValue);
        }
    }

    @Nullable
    public Period getPeriod() {
        return value == null ? null : toPeriod(value);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        value = restorePersistedValue ? getPersistedString(null) : defaultValue.toString();
        if (value != null) {
            persistString(value);
            setSummary(value);
        }
    }

    @Override
    public void setSummary(CharSequence original) {
        try {
            super.setSummary(periodFormatter.print(toPeriod(original)));
        } catch (IllegalArgumentException e) {
            Log.wtf(TAG, format("Unexpected format: [%s].", original));
            super.setSummary(original);
        }
    }

    private void updateWidgets(String value) {
        try {
            Period period = toPeriod(value);
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

    public void setPeriod(@NonNull Period period) {
        String newValue = DateTimeHelper.toText(period);
        onNewValue(newValue);
    }

    private void onNewValue(String newValue) {
        if (callChangeListener(newValue)) {
            value = newValue;
            setSummary(value);
            persistString(newValue);
        }
    }

}
