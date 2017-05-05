package name.leesah.nirvana.ui.settings.treatment;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.reminding.RemindingStrategy;
import name.leesah.nirvana.model.treatment.recurring.NTimes;
import name.leesah.nirvana.model.treatment.recurring.RecurringStrategy;
import name.leesah.nirvana.model.treatment.recurring.UntilDate;
import name.leesah.nirvana.ui.medication.StrategyPreferenceDelegate;
import name.leesah.nirvana.ui.medication.StrategySelectActivity;

/**
 * Created by sah on 2017-05-03.
 */

public class RecurringStrategyPreference extends Preference implements SharedPreferences.OnSharedPreferenceChangeListener {

    private StrategyPreferenceDelegate<RecurringStrategy> delegate;

    public RecurringStrategyPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToActivity() {
        super.onAttachedToActivity();

        setTitle(R.string.pref_title_treatment_recurring);
        delegate = new StrategyPreferenceDelegate.Recurring(this);

        getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        setOnPreferenceClickListener(preference -> startStrategySelectActivity());

        updateSummary();
    }

    private boolean startStrategySelectActivity() {
        int selected = -1;
        if (delegate.getValue() instanceof NTimes) selected = 0;
        if (delegate.getValue() instanceof UntilDate) selected = 1;
        StrategySelectActivity.start(getContext(), R.string.pref_title_treatment_recurring,
                R.array.treatment_recurring_strategies,
                new Class[]{NTimesEditFragment.class, UntilDateEditFragment.class},
                selected);
        return true;
    }

    public void setStrategy(@NonNull RecurringStrategy strategy) {
        delegate.setValue(strategy);
    }

    @Nullable
    public RecurringStrategy getStrategy() {
        return delegate.getValue();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getKey()))
            updateSummary();
    }

    private void updateSummary() {
        if (delegate.getValue() == null)
            setSummary("change me");
    }

}
