package name.leesah.nirvana.ui.medication;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.stopping.Never;
import name.leesah.nirvana.model.medication.stopping.StoppingStrategy;
import name.leesah.nirvana.ui.preference.CheckableDialogPreference;

/**
 * Created by sah on 2017-05-03.
 */

public class StoppingStrategyPreference extends CheckableDialogPreference {

    private StrategyPreferenceDelegate<StoppingStrategy> delegate;

    public StoppingStrategyPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToActivity() {
        super.onAttachedToActivity();

        setTitle(R.string.pref_title_medication_stopping);
        delegate = new StrategyPreferenceDelegate.Stopping(this);

        StoppingStrategy strategy = delegate.getValue();
        setChecked(strategy != null);
        if (strategy == null)
            delegate.setValue(new Never());
    }

    public void setStrategy(@NonNull StoppingStrategy strategy) {
        delegate.setValue(strategy);
    }

    @Nullable
    public StoppingStrategy getStrategy() {
        return delegate.getValue();
    }

}
