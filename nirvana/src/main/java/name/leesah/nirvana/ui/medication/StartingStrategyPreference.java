package name.leesah.nirvana.ui.medication;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;

import org.joda.time.LocalDate;

import name.leesah.nirvana.PhoneBook;
import name.leesah.nirvana.R;
import name.leesah.nirvana.data.Therapist;
import name.leesah.nirvana.model.medication.starting.Delayed;
import name.leesah.nirvana.model.medication.starting.ExactDate;
import name.leesah.nirvana.model.medication.starting.Immediately;
import name.leesah.nirvana.model.medication.starting.StartingStrategy;
import name.leesah.nirvana.ui.preference.CheckableDialogPreference;
import name.leesah.nirvana.ui.widget.PeriodPicker;

import static name.leesah.nirvana.PhoneBook.therapist;
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
        relativeMode = therapist(context).isCycleSupportEnabled();

        setDialogLayoutResource(relativeMode ?
                R.layout.dialog_period_picker : R.layout.dialog_date_picker);
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
    protected void onBindDialogView(View view) {
        StartingStrategy strategy = delegate.getValue();

        if (relativeMode) {
            periodPicker = (PeriodPicker) view.findViewById(R.id.period_picker);
            if (relativeMode && strategy instanceof Delayed)
                periodPicker.setPeriod(((Delayed) strategy).getPeriod());

        } else {
            datePicker = (DatePicker) view.findViewById(R.id.date_picker);
            if (strategy instanceof ExactDate) {
                LocalDate startDate = ((ExactDate) strategy).getStartDate();
                datePicker.updateDate(
                        startDate.getYear(),
                        startDate.getMonthOfYear() - 1,
                        startDate.getDayOfMonth());
            }
        }

        super.onBindDialogView(view);
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
