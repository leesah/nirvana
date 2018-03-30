package name.leesah.nirvana.ui.medication;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.repeating.DaysOfWeek;
import name.leesah.nirvana.model.medication.repeating.Everyday;
import name.leesah.nirvana.model.medication.repeating.RepeatingStrategy;
import name.leesah.nirvana.model.medication.repeating.WithInterval;
import name.leesah.nirvana.ui.medication.repeating.DaysOfWeekEditFragment;
import name.leesah.nirvana.ui.medication.repeating.WithIntervalEditFragment;
import name.leesah.nirvana.ui.preference.CheckableNonDialogPreference;

/**
 * Created by sah on 2017-05-03.
 */

public class RepeatingStrategyPreference extends CheckableNonDialogPreference implements SharedPreferences.OnSharedPreferenceChangeListener {

    private StrategyPreferenceDelegate<RepeatingStrategy> delegate;

    public RepeatingStrategyPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToActivity() {
        super.onAttachedToActivity();

        setTitle(R.string.pref_title_medication_repeating);
        delegate = new StrategyPreferenceDelegate.Repeating(this);

        RepeatingStrategy strategy = delegate.getValue();
        setChecked(strategy != null && !(strategy instanceof Everyday));
        setWithDefaultStrategyIfNeeded();

        setOnPreferenceClickListener(preference -> startStrategySelectActivity());
        getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    private boolean startStrategySelectActivity() {
        int selected;
        RepeatingStrategy strategy = delegate.getValue();
        if (strategy instanceof WithInterval) selected = 0;
        else if (strategy instanceof DaysOfWeek) selected = 1;
        else selected = -1;

        StrategySelectActivity.start(getContext(), R.string.pref_title_medication_repeating,
                R.array.medication_repeating_strategies,
                new Class[]{WithIntervalEditFragment.class, DaysOfWeekEditFragment.class},
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (!key.equals(getKey()))
            return;

        setWithDefaultStrategyIfNeeded();
        updateSummary();
    }

    private void setWithDefaultStrategyIfNeeded() {
        if (delegate.getValue() == null) delegate.setValue(new Everyday());
    }

    private void updateSummary() {
        RepeatingStrategy strategy = delegate.getValue();
        if (strategy == null)
            setSummary(R.string.pref_summary_medication_reminding);
    }

}
