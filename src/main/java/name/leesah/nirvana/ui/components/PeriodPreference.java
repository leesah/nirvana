package name.leesah.nirvana.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import org.joda.time.Period;

import java.util.Arrays;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.PeriodUnit;
import name.leesah.nirvana.utils.DateTimeHelper;

import static name.leesah.nirvana.model.PeriodUnit.DAY;
import static name.leesah.nirvana.model.PeriodUnit.MONTH;
import static name.leesah.nirvana.model.PeriodUnit.WEEK;

/**
 * Created by sah on 2016-12-10.
 */

public class PeriodPreference extends DialogPreference {

    private static final String DEFAULT_VALUE = DateTimeHelper.PERIOD_FORMATTER.print(Period.months(1));

    private EditText editText;
    private Spinner spinner;
    private String value = null;

    public PeriodPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.dialog_period_picker);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        editText = (EditText) view.findViewById(R.id.editText_number);
        spinner = (Spinner) view.findViewById(R.id.spinner_unit);
        updateWidgets(value);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            int number = Integer.valueOf(editText.getText().toString());
            PeriodUnit unit = Arrays.stream(PeriodUnit.values())
                    .filter(u -> u.getPositionInSpinner() == spinner.getSelectedItemPosition())
                    .findFirst().get();
            String newValue = DateTimeHelper.toPeriodAsString(number, unit);
            onNewValue(newValue);
        }
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
        return value == null ? null : DateTimeHelper.toPeriod(value);
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
            Period period = DateTimeHelper.toPeriod(original);
            int days = period.getDays();
            int weeks = period.getWeeks();
            int months = period.getMonths();
            if (days != 0) {
                super.setSummary(getContext().getString(R.string.pref_summary_days_template, days));
            } else if (weeks != 0) {
                super.setSummary(getContext().getString(R.string.pref_summary_weeks_template, weeks));
            } else if (months != 0) {
                super.setSummary(getContext().getString(R.string.pref_summary_months_template, months));
            }
        } catch (IllegalArgumentException e) {
            super.setSummary(original);
        }
    }

    private void updateWidgets(String value) {
        try {
            Period period = DateTimeHelper.toPeriod(value);
            int days = period.getDays();
            int weeks = period.getWeeks();
            int months = period.getMonths();
            if (days != 0) {
                updateWidgets(days, DAY.getPositionInSpinner());
            } else if (weeks != 0) {
                updateWidgets(weeks, WEEK.getPositionInSpinner());
            } else if (months != 0) {
                updateWidgets(months, MONTH.getPositionInSpinner());
            }
        } catch (IllegalArgumentException e) {
            updateWidgets(0, 0);
        }
    }

    private void updateWidgets(int numberInEditText, int positionInSpinner) {
        editText.setText(String.valueOf(numberInEditText));
        spinner.setSelection(positionInSpinner);
    }

}