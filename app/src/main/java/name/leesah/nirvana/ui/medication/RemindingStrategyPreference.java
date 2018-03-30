package name.leesah.nirvana.ui.medication;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.reminding.CertainHours;
import name.leesah.nirvana.model.medication.reminding.EveryNHours;
import name.leesah.nirvana.model.medication.reminding.NTimesADay;
import name.leesah.nirvana.model.medication.reminding.RemindingStrategy;
import name.leesah.nirvana.ui.medication.reminding.CertainHoursEditFragment;
import name.leesah.nirvana.ui.medication.reminding.EveryNHoursEditFragment;
import name.leesah.nirvana.ui.medication.reminding.NTimesADayEditFragment;

import static name.leesah.nirvana.R.array.medication_reminding_strategies;
import static name.leesah.nirvana.R.string.pref_summary_medication_reminding;
import static name.leesah.nirvana.R.string.pref_title_medication_reminding;

/**
 * Created by sah on 2017-05-03.
 */

public class RemindingStrategyPreference extends Preference implements SharedPreferences.OnSharedPreferenceChangeListener {

    private StrategyPreferenceDelegate<RemindingStrategy> delegate;

    public RemindingStrategyPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToActivity() {
        super.onAttachedToActivity();

        setTitle(pref_title_medication_reminding);
        delegate = new StrategyPreferenceDelegate.Reminding(this);

        getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        setOnPreferenceClickListener(preference -> startStrategySelectActivity());

        updateSummary();
    }

    private boolean startStrategySelectActivity() {
        int selected = -1;
        if (delegate.getValue() instanceof CertainHours) selected = 0;
        if (delegate.getValue() instanceof EveryNHours) selected = 1;
        if (delegate.getValue() instanceof NTimesADay) selected = 1;
        StrategySelectActivity.start(getContext(), pref_title_medication_reminding,
                medication_reminding_strategies,
                new Class[]{CertainHoursEditFragment.class, EveryNHoursEditFragment.class, NTimesADayEditFragment.class},
                selected);
        return true;
    }

    public void setStrategy(@NonNull RemindingStrategy strategy) {
        delegate.setValue(strategy);
    }

    @Nullable
    public RemindingStrategy getStrategy() {
        return delegate.getValue();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getKey()))
            updateSummary();
    }

    private void updateSummary() {
        if (delegate.getValue() == null)
            setSummary(pref_summary_medication_reminding);
    }

}
