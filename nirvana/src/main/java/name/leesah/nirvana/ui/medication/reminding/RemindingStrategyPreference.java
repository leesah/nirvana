package name.leesah.nirvana.ui.medication.reminding;


import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.reminding.CertainHours;
import name.leesah.nirvana.model.medication.reminding.EveryNHours;
import name.leesah.nirvana.model.medication.reminding.RemindingStrategy;
import name.leesah.nirvana.ui.medication.JsonValuedPreferenceDelegate;
import name.leesah.nirvana.ui.medication.StrategySelectActivity;
import name.leesah.nirvana.ui.medication.reminding.CertainHoursEditFragment;
import name.leesah.nirvana.ui.medication.reminding.EveryNHoursEditFragment;

/**
 * Created by sah on 2017-05-03.
 */

public class RemindingStrategyPreference extends Preference {

    private JsonValuedPreferenceDelegate<RemindingStrategy> delegate;

    public RemindingStrategyPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        delegate = new JsonValuedPreferenceDelegate<RemindingStrategy>(
                this, new TypeToken<RemindingStrategy>() {
        }) {
            @Override
            protected String buildSummary(RemindingStrategy value) {
                return value.toString(getContext());
            }
        };

        setOnPreferenceClickListener(preference -> startStrategySelectActivity());
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return delegate.onGetDefaultValue(a, index);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        delegate.onSetInitialValue(restorePersistedValue, defaultValue);
    }

    private boolean startStrategySelectActivity() {
        StrategySelectActivity.start(getContext(), R.string.pref_title_medication_reminding,
                getContext().getResources().getStringArray(R.array.medication_reminding_strategies),
                new String[]{CertainHoursEditFragment.class.getName(), EveryNHoursEditFragment.class.getName()});
        return true;
    }

    public void setStrategy(@NonNull RemindingStrategy strategy) {
        delegate.setValue(strategy);
    }

    @Nullable
    public RemindingStrategy getStrategy() {
        return delegate.getValue();
    }

}
