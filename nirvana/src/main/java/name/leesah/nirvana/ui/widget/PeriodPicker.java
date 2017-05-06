package name.leesah.nirvana.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import org.joda.time.Period;

import java.util.ArrayList;
import java.util.List;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.PeriodUnit;

import static java.util.EnumSet.allOf;
import static name.leesah.nirvana.model.PeriodUnit.DAY;
import static name.leesah.nirvana.model.PeriodUnit.MONTH;
import static name.leesah.nirvana.model.PeriodUnit.WEEK;
import static name.leesah.nirvana.utils.DateTimeHelper.toPeriod;

/**
 * Created by sah on 2017-05-05.
 */

public class PeriodPicker extends LinearLayout {

    public static final List<PeriodUnit> PERIOD_UNITS = new ArrayList<>(allOf(PeriodUnit.class));
    private final String[] displayedValuesPlural;
    private final String[] displayedValuesSingular;

    private NumberPicker number;
    private NumberPicker unit;

    public PeriodPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        displayedValuesSingular = context.getResources().getStringArray(R.array.period_unit_displayed_singular);
        displayedValuesPlural = context.getResources().getStringArray(R.array.period_unit_displayed_plural);

        inflate(context, R.layout.period_picker, this);

        number = (NumberPicker) findViewById(R.id.number);
        number.setMinValue(1);
        number.setMaxValue(99);
        number.setOnValueChangedListener((p, oldVal, newVal) -> toggleSingularPlural(oldVal, newVal));

        unit = (NumberPicker) findViewById(R.id.unit);
        unit.setMinValue(0);
        unit.setMaxValue(PERIOD_UNITS.size() - 1);
        unit.setDisplayedValues(displayedValuesSingular);

    }

    public Period getPeriod() {
        PeriodUnit unit = PERIOD_UNITS.get(this.unit.getValue());
        return toPeriod(number.getValue(), unit);
    }

    public void setPeriod(Period period) {
        int days = period.getDays();
        if (days > 0) updateWidgets(days, PERIOD_UNITS.indexOf(DAY));

        int weeks = period.getWeeks();
        if (weeks > 0) updateWidgets(weeks, PERIOD_UNITS.indexOf(WEEK));

        int months = period.getMonths();
        if (months > 0) updateWidgets(months, PERIOD_UNITS.indexOf(MONTH));
    }

    private void updateWidgets(int numberValue, int unitValue) {
        toggleSingularPlural(number.getValue(), numberValue);
        number.setValue(numberValue);
        unit.setValue(unitValue);
    }

    private void toggleSingularPlural(int oldVal, int newVal) {
        if (oldVal == 1 && newVal > 1)
            unit.setDisplayedValues(displayedValuesPlural);
        else if (oldVal > 1 && newVal == 1)
            unit.setDisplayedValues(displayedValuesSingular);
    }

}
