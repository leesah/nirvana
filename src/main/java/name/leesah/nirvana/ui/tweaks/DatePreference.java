package name.leesah.nirvana.ui.tweaks;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import org.joda.time.LocalDate;

import name.leesah.nirvana.R;

import static java.lang.String.format;
import static java.util.Locale.getDefault;
import static name.leesah.nirvana.utils.DateTimeHelper.toDate;
import static name.leesah.nirvana.utils.DateTimeHelper.toText;
import static org.joda.time.format.DateTimeFormat.longDate;

/**
 * Created by sah on 2016-12-10.
 */

public class DatePreference extends DialogPreference {

    private static final String TAG = DatePreference.class.getSimpleName();

    private DatePicker picker = null;
    private String value;

    public DatePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.date_preference);
    }


    @Override
    protected void onBindDialogView(View view) {
        picker = (DatePicker) view.findViewById(R.id.datePicker);
        if (!TextUtils.isEmpty(value)) {
            LocalDate date = toDate(value);
            picker.updateDate(date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth());
        }
        super.onBindDialogView(view);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            LocalDate date = new LocalDate()
                    .withYear(picker.getYear())
                    .withMonthOfYear(picker.getMonth() + 1)
                    .withDayOfMonth(picker.getDayOfMonth());
            onNewValue(toText(date));
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return (a.getString(index));
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        value = restoreValue ? getPersistedString(null) : defaultValue.toString();
        if (value != null) {
            persistString(value);
            setSummary(value);
        }
    }

    @Override
    public void setSummary(CharSequence original) {
        try {
            super.setSummary(toDate(original).toString(longDate().withLocale(getDefault())));
        } catch (IllegalArgumentException e) {
            Log.wtf(TAG, format("Unexpected format: [%s].", original));
            super.setSummary(original);
        }
    }

    public void setDate(@NonNull LocalDate date) {
        onNewValue(toText(date));
    }

    @Nullable
    public LocalDate getDate() {
        return value == null ? null : toDate(value);
    }

    private void onNewValue(String newValue) {
        if (callChangeListener(newValue)) {
            value = newValue;
            setSummary(value);
            persistString(newValue);
        }
    }

}