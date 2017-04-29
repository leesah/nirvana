package name.leesah.nirvana.ui.preference;

import android.content.Context;
import android.preference.DialogPreference;
import android.support.annotation.Nullable;
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

import static java.lang.String.format;
import static name.leesah.nirvana.model.PeriodUnit.DAY;
import static name.leesah.nirvana.model.PeriodUnit.MONTH;
import static name.leesah.nirvana.model.PeriodUnit.WEEK;
import static name.leesah.nirvana.utils.DateTimeHelper.*;

public class PeriodPreferenceDelegate extends StringValuedDialogPreferenceDelegate {
    private static final String TAG = PeriodPreference.class.getSimpleName();

    public static final List<PeriodUnit> PERIOD_UNITS = new ArrayList<>(EnumSet.allOf(PeriodUnit.class));
    private final String[] displayedValuesPlural;
    private final String[] displayedValuesSingular;
    private final PeriodFormatter formatter;

    private NumberPicker number;
    private NumberPicker unit;
    private String value = null;

    PeriodPreferenceDelegate(DialogPreference preference) {
        super(preference);
        preference.setDialogLayoutResource(R.layout.period_picker);

        Context context = preference.getContext();
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
        return value == null ? null : toPeriod(value);
    }

    void setPeriod(@Nullable Period period) {
        String newValue = toText(period);
        updateValue(newValue);
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
            String newValue = toPeriodAsString(number.getValue(), readUnit());
            updateValue(newValue);
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


    protected String translateSummary(CharSequence original) {
        try {
            return original == null ? null : formatter.print(toPeriod(original));
        } catch (IllegalArgumentException e) {
            Log.wtf(TAG, format("Unexpected format: [%s].", original));
            return original.toString();
        }
    }

}