package name.leesah.nirvana.ui.medication;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;

import org.joda.time.LocalDate;

import name.leesah.nirvana.R;
import name.leesah.nirvana.data.Therapist;
import name.leesah.nirvana.model.medication.starting.Delayed;
import name.leesah.nirvana.model.medication.starting.ExactDate;
import name.leesah.nirvana.model.medication.starting.Immediately;
import name.leesah.nirvana.model.medication.starting.StartingStrategy;
import name.leesah.nirvana.ui.preference.CheckableDialogPreference;
import name.leesah.nirvana.ui.widget.PeriodPicker;

import static name.leesah.nirvana.utils.DateTimeHelper.toText;
import static name.leesah.nirvana.utils.DateTimeHelper.today;

/**
 * Created by sah on 2017-05-03.
 */

public class StartingStrategyPreference extends CheckableDialogPreference {

    private StrategyPreferenceDelegate<StartingStrategy> delegate;
    private boolean relativeMode;
    private PeriodPicker periodPicker;
    private DatePicker datePicker;

    public StartingStrategyPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        periodPicker = new PeriodPicker(context, attrs);
        datePicker = new DatePicker(context, attrs);
        relativeMode = Therapist.getInstance(context).isCycleSupportEnabled();
    }

    @Override
    protected void onAttachedToActivity() {
        super.onAttachedToActivity();

        setTitle(R.string.pref_title_medication_starting);
        delegate = new StrategyPreferenceDelegate.Starting(this);

        StartingStrategy strategy = delegate.getValue();
        setChecked(strategy != null &&
                !(strategy instanceof Immediately) &&
                !(strategy.equals(new ExactDate(today()))));
        if (!isReasonableStrategy(strategy))
            delegate.setValue(getDefaultStrategy());
    }

    @Override
    protected View onCreateDialogView() {
        return relativeMode ? periodPicker : datePicker;
    }

    @Override
    protected void onBindDialogView(View view) {
        StartingStrategy strategy = delegate.getValue();

        if (relativeMode && strategy instanceof Delayed)
            updatePeriodPickerView((Delayed) strategy);
        else if (strategy instanceof ExactDate)
            updateDatePickerView((ExactDate) strategy);

        super.onBindDialogView(view);
    }

    private void updatePeriodPickerView(Delayed delayed) {
        periodPicker.setPeriod(delayed.getPeriod());
    }

    private void updateDatePickerView(ExactDate strategy) {
        LocalDate date = strategy.getStartDate();
        datePicker.updateDate(date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth());
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (!positiveResult)
            return;

        if (relativeMode)
            delegate.setValue(readDelayed());
        else
            delegate.setValue(readExactDate());
    }

    private Delayed readDelayed() {
        return new Delayed(periodPicker.getPeriod());
    }

    private ExactDate readExactDate() {
        LocalDate date = new LocalDate()
                .withYear(datePicker.getYear())
                .withMonthOfYear(datePicker.getMonth() + 1)
                .withDayOfMonth(datePicker.getDayOfMonth());
        return new ExactDate(date);
    }

    private boolean isReasonableStrategy(StartingStrategy strategy) {
        return relativeMode ?
                strategy instanceof Immediately || strategy instanceof Delayed :
                strategy instanceof ExactDate;
    }

    @NonNull
    private StartingStrategy getDefaultStrategy() {
        return relativeMode ?
                new Immediately() :
                new ExactDate(today());
    }

    public void setStrategy(@NonNull StartingStrategy strategy) {
        delegate.setValue(strategy);
    }

    @Nullable
    public StartingStrategy getStrategy() {
        return delegate.getValue();
    }

}
