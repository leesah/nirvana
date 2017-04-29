package name.leesah.nirvana.ui.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import org.joda.time.LocalDate;

import static java.lang.String.format;
import static java.util.Locale.getDefault;
import static name.leesah.nirvana.utils.DateTimeHelper.toText;

/**
 * Created by sah on 2016-12-10.
 */

public class DatePreference extends DialogPreference {

    private final DatePreferenceDelegate delegate;

    public DatePreference(Context context, AttributeSet attrs) {
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

    @Override
    public void setSummary(CharSequence original) {
        delegate.setSummary(original);
    }

    public void setDate(@NonNull LocalDate date) {
        delegate.setDate(date);
    }

    @Nullable
    public LocalDate getDate() {
        return delegate.getDate();
    }

}