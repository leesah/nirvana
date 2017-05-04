package name.leesah.nirvana.ui.medication;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.google.gson.reflect.TypeToken;

import name.leesah.nirvana.model.medication.reminding.RemindingStrategy;
import name.leesah.nirvana.ui.preference.CheckableDialogPreference;

/**
 * Created by sah on 2017-05-03.
 */

public class StoppingStrategyPreference extends CheckableDialogPreference {

    private JsonValuedPreferenceDelegate<RemindingStrategy> delegate;

    public StoppingStrategyPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        delegate = new JsonValuedPreferenceDelegate<RemindingStrategy>(
                this, new TypeToken<RemindingStrategy>() {
        }) {
            @Override
            protected String buildSummary(RemindingStrategy value) {
                return value.toString(getContext());
            }
        };
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return delegate.onGetDefaultValue(a, index);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        delegate.onSetInitialValue(restorePersistedValue, defaultValue);
    }

    public void setStrategy(@NonNull RemindingStrategy strategy) {
        delegate.setValue(strategy);
    }

    @Nullable
    public RemindingStrategy getStrategy() {
        return delegate.getValue();
    }

}
