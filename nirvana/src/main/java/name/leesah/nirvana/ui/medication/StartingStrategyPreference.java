package name.leesah.nirvana.ui.medication;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import name.leesah.nirvana.R;
import name.leesah.nirvana.data.Therapist;
import name.leesah.nirvana.model.medication.starting.Delayed;
import name.leesah.nirvana.model.medication.starting.ExactDate;
import name.leesah.nirvana.model.medication.starting.Immediately;
import name.leesah.nirvana.model.medication.starting.StartingStrategy;
import name.leesah.nirvana.ui.preference.CheckableDialogPreference;

import static name.leesah.nirvana.utils.DateTimeHelper.today;

/**
 * Created by sah on 2017-05-03.
 */

public class StartingStrategyPreference extends CheckableDialogPreference {

    private StrategyPreferenceDelegate<StartingStrategy> delegate;
    private boolean cycleEnabled;

    public StartingStrategyPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToActivity() {
        super.onAttachedToActivity();

        setTitle(R.string.pref_title_medication_starting);
        delegate = new StrategyPreferenceDelegate.Starting(this);

        cycleEnabled = Therapist.getInstance(getContext()).isCycleSupportEnabled();

        StartingStrategy strategy = delegate.getValue();
        setChecked(strategy != null);
        if (!isReasonableStrategy(strategy))
            delegate.setValue(getDefaultStrategy());
    }

    private boolean isReasonableStrategy(StartingStrategy strategy) {
        return cycleEnabled ?
                strategy instanceof Immediately || strategy instanceof Delayed :
                strategy instanceof ExactDate;
    }

    @NonNull
    private StartingStrategy getDefaultStrategy() {
        return cycleEnabled ?
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
