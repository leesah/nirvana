package name.leesah.nirvana.ui.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import org.joda.time.LocalDate;

import static name.leesah.nirvana.utils.DateTimeHelper.toDate;
import static name.leesah.nirvana.utils.DateTimeHelper.toPeriod;
import static name.leesah.nirvana.utils.DateTimeHelper.today;

/**
 * Created by sah on 2016-12-10.
 */

public class CheckableDatePreference extends CheckableDialogPreference implements CheckablePreference {

    private final DatePreferenceDelegate delegate;

    public CheckableDatePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        delegate = new DatePreferenceDelegate(this);
    }


    @Override
    protected void onBindDialogView(View view) {
        delegate.onBindDialogView(view);
        super.onBindDialogView(view);
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
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        delegate.onSetInitialValue(restoreValue, defaultValue);
    }

    public void setValue(@Nullable LocalDate date) {
        delegate.setDate(date);
        if (date != null && !date.isEqual(today()))
            setChecked(true);
    }

    @Override
    @Nullable
    public LocalDate getValue() {
        return isChecked() ? delegate.getDate() :
                (getValueOff() == null ? null :
                        toDate(getValueOff()));
    }

}