package name.leesah.nirvana.ui.medication;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.repeating.DaysOfWeek;
import name.leesah.nirvana.model.medication.repeating.EveryNDays;
import name.leesah.nirvana.model.medication.repeating.Everyday;
import name.leesah.nirvana.model.medication.repeating.RepeatingStrategy;
import name.leesah.nirvana.ui.medication.repeating.DaysOfWeekEditFragment;
import name.leesah.nirvana.ui.medication.repeating.EveryNDaysEditFragment;
import name.leesah.nirvana.ui.preference.CheckableNonDialogPreference;

/**
 * Created by sah on 2017-05-03.
 */

public class RepeatingStrategyPreference extends CheckableNonDialogPreference {

    private StrategyPreferenceDelegate<RepeatingStrategy> delegate;

    public RepeatingStrategyPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToActivity() {
        super.onAttachedToActivity();

        setTitle(R.string.pref_title_medication_repeating);
        delegate = new StrategyPreferenceDelegate.Repeating(this);

        getSharedPreferences().registerOnSharedPreferenceChangeListener(delegate);
        setOnPreferenceClickListener(preference -> startStrategySelectActivity());

        RepeatingStrategy strategy = delegate.getValue();
        setChecked(strategy != null);
        if (strategy == null)
            delegate.setValue(new Everyday());
    }

    private boolean startStrategySelectActivity() {
        int selected = -1;
        if (delegate.getValue() instanceof EveryNDays) selected = 0;
        if (delegate.getValue() instanceof DaysOfWeek) selected = 1;
        StrategySelectActivity.start(getContext(), R.string.pref_title_medication_repeating,
                R.array.medication_repeating_strategies,
                new Class[]{EveryNDaysEditFragment.class, DaysOfWeekEditFragment.class},
                selected);
        return true;
    }

    public void setStrategy(@NonNull RepeatingStrategy strategy) {
        delegate.setValue(strategy);
    }

    @Nullable
    public RepeatingStrategy getStrategy() {
        return delegate.getValue();
    }

}
