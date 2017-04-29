package name.leesah.nirvana.ui.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import org.joda.time.Period;

import static java.lang.String.format;
import static name.leesah.nirvana.utils.DateTimeHelper.toPeriod;

/**
 * Created by sah on 2016-12-10.
 */

public class PeriodPreference extends DialogPreference {

    private PeriodPreferenceDelegate delegate;

    public PeriodPreference(Context context, AttributeSet attrs) {
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

    public void setPeriod(@NonNull Period period) {
        delegate.setPeriod(period);
    }

    @Nullable
    public Period getPeriod() {
        return delegate.getPeriod();
    }

}