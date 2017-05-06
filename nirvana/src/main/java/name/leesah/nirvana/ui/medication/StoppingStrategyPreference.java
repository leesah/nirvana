package name.leesah.nirvana.ui.medication;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.stopping.InPeriod;
import name.leesah.nirvana.model.medication.stopping.Never;
import name.leesah.nirvana.model.medication.stopping.StoppingStrategy;
import name.leesah.nirvana.ui.preference.CheckableDialogPreference;
import name.leesah.nirvana.ui.widget.PeriodPicker;

/**
 * Created by sah on 2017-05-03.
 */

public class StoppingStrategyPreference extends CheckableDialogPreference {

    private StrategyPreferenceDelegate<StoppingStrategy> delegate;
    private PeriodPicker periodPicker;

    public StoppingStrategyPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        periodPicker = new PeriodPicker(context, attrs);
    }

    @Override
    protected void onAttachedToActivity() {
        super.onAttachedToActivity();

        setTitle(R.string.pref_title_medication_stopping);
        delegate = new StrategyPreferenceDelegate.Stopping(this);

        StoppingStrategy strategy = delegate.getValue();
        setChecked(strategy != null && !(strategy instanceof Never));
        if (strategy == null)
            delegate.setValue(new Never());
    }

    @Override
    protected View onCreateDialogView() {
        return periodPicker;
    }

    @Override
    protected void onBindDialogView(View view) {
        StoppingStrategy strategy = delegate.getValue();
        if (strategy instanceof InPeriod)
            periodPicker.setPeriod(((InPeriod) strategy).getPeriod());
        super.onBindDialogView(view);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult)
            delegate.setValue(new InPeriod(periodPicker.getPeriod()));
    }

    public void setStrategy(@NonNull StoppingStrategy strategy) {
        delegate.setValue(strategy);
    }

    @Nullable
    public StoppingStrategy getStrategy() {
        return delegate.getValue();
    }

}
