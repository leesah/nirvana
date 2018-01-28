package name.leesah.nirvana.ui.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import org.joda.time.Period;
import org.joda.time.format.PeriodFormat;

import name.leesah.nirvana.ui.widget.PeriodPicker;

import static name.leesah.nirvana.utils.DateTimeHelper.toPeriod;
import static name.leesah.nirvana.utils.DateTimeHelper.toText;
import static org.joda.time.format.PeriodFormat.wordBased;

/**
 * Created by sah on 2016-12-10.
 */

public class PeriodPreference extends DialogPreference {

    private static final String TAG = PeriodPreference.class.getSimpleName();

    private final PeriodPicker periodPicker;

    private String value = null;

    public PeriodPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        periodPicker = new PeriodPicker(context, attrs);
    }

    @Override
    protected View onCreateDialogView() {
        return periodPicker;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        if (value != null)
            periodPicker.setPeriod(toPeriod(value));
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            String newValue = toText(periodPicker.getPeriod());
            onNewValue(newValue);
        }
    }

    @Nullable
    public Period getPeriod() {
        return value == null ? null : toPeriod(value);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        value = restorePersistedValue ? getPersistedString(null) : defaultValue.toString();
        persistString(value);
        setSummary(value);
    }

    @Override
    public void setSummary(CharSequence original) {
        super.setSummary(original == null ?
                null : wordBased().print(toPeriod(original)));
    }

    public void setPeriod(@NonNull Period period) {
        String newValue = toText(period);
        onNewValue(newValue);
    }

    private void onNewValue(String newValue) {
        if (callChangeListener(newValue)) {
            value = newValue;
            setSummary(value);
            persistString(newValue);
        }
    }

}
