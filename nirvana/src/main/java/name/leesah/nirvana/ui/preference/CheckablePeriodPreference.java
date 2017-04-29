package name.leesah.nirvana.ui.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import org.joda.time.Period;

import static name.leesah.nirvana.utils.DateTimeHelper.toPeriod;
import static org.joda.time.Period.*;

/**
 * Created by sah on 2016-12-10.
 */

public class CheckablePeriodPreference extends CheckableDialogPreference implements CheckablePreference {

    private PeriodPreferenceDelegate delegate;

    public CheckablePeriodPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        delegate = new PeriodPreferenceDelegate(this);

    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        delegate.onBindDialogView(view);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        delegate.onDialogClosed(positiveResult);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return delegate.onGetDefaultValue(a, index);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        delegate.onSetInitialValue(restorePersistedValue, defaultValue);
    }

    public void setValue(@Nullable Period period) {
        delegate.setPeriod(period);
        if (period != null && period.toStandardDuration().isLongerThan(ZERO.toStandardDuration()))
            setChecked(true);
    }

    @Nullable
    public Period getValue() {
        return isChecked() ? delegate.getPeriod()  :
                (getValueOff() == null ? null :
                        toPeriod(getValueOff()));
    }

}
