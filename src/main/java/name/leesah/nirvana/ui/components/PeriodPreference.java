package name.leesah.nirvana.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

import org.joda.time.Period;

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
import static name.leesah.nirvana.utils.DateTimeHelper.toPeriod;
import static name.leesah.nirvana.utils.DateTimeHelper.toPeriodAsString;

/**
 * Created by sah on 2016-12-10.
 */

public class PeriodPreference extends DialogPreference {

    private static final String DEFAULT_VALUE = toPeriodAsString(1, DAY);
    private final List<PeriodUnit> periodUnits = new ArrayList<>(EnumSet.allOf(PeriodUnit.class));

    private NumberPicker number;
    private NumberPicker unit;
    private String value = null;

    public PeriodPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.dialog_period_picker);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        number = (NumberPicker) view.findViewById(R.id.number);
        number.setMinValue(1);
        number.setMaxValue(99);

        unit = (NumberPicker) view.findViewById(R.id.unit);
        unit.setMinValue(0);
        unit.setMaxValue(periodUnits.size() - 1);
        unit.setDisplayedValues(getContext().getResources().getStringArray(R.array.period_unit_displayed_texts));

        if (value != null)
            updateWidgets(value);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            String newValue = toPeriodAsString(number.getMaxValue(), readUnit());
            onNewValue(newValue);
        }
    }

    private PeriodUnit readUnit() {
        return periodUnits.get(unit.getValue());
    }

    public void setPeriod(Period period) {
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

    public Period getPeriod() {
        return value == null ? null : toPeriod(value);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        value = restorePersistedValue ? getPersistedString(DEFAULT_VALUE) : defaultValue.toString();
        persistString(value);
        setSummary(value);
    }

    @Override
    public void setSummary(CharSequence original) {
        try {
            Period period = toPeriod(original);
            int days = period.getDays();
            if (days > 0)
                super.setSummary(getContext().getString(R.string.pref_summary_days_template, days));

            int weeks = period.getWeeks();
            if (weeks > 0)
                super.setSummary(getContext().getString(R.string.pref_summary_weeks_template, weeks));

            int months = period.getMonths();
            if (months > 0)
                super.setSummary(getContext().getString(R.string.pref_summary_months_template, months));

        } catch (IllegalArgumentException e) {
            super.setSummary(original);
        }
    }

    private void updateWidgets(String value) {
        try {
            Period period = toPeriod(value);
            int days = period.getDays();
            if (days > 0) updateWidgets(days, periodUnits.indexOf(DAY));

            int weeks = period.getWeeks();
            if (weeks > 0) updateWidgets(weeks, periodUnits.indexOf(WEEK));

            int months = period.getMonths();
            if (months > 0) updateWidgets(months, periodUnits.indexOf(MONTH));
        } catch (IllegalArgumentException e) {
            updateWidgets(0, 0);
        }
    }

    private void updateWidgets(int number, int unit) {
        this.number.setValue(number);
        this.unit.setValue(unit);
    }

}