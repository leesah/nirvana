package name.leesah.nirvana.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;

import org.joda.time.LocalDate;

import static java.util.Locale.getDefault;
import static name.leesah.nirvana.utils.DateTimeHelper.toDate;
import static name.leesah.nirvana.utils.DateTimeHelper.toText;
import static name.leesah.nirvana.utils.DateTimeHelper.todayAsString;
import static org.joda.time.format.DateTimeFormat.longDate;

/**
 * Created by sah on 2016-12-10.
 */

public class DatePreference extends DialogPreference {

    private DatePicker picker = null;
    private String value;

    public DatePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected View onCreateDialogView() {
        picker = new DatePicker(getContext());
        if (!TextUtils.isEmpty(value)) {
            LocalDate date = toDate(value);
            picker.updateDate(date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth());
        }
        return picker;
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

    public void setValue(LocalDate date) {
        onNewValue(toText(date));
    }

    private void onNewValue(String newValue) {
        if (callChangeListener(newValue)) {
            value = newValue;
            setSummary(value);
            persistString(newValue);
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return (a.getString(index));
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        value = restoreValue ? getPersistedString(todayAsString()) : defaultValue.toString();
        if (shouldPersist())
            persistString(value);
        setSummary(value);
    }

    @Override
    public void setSummary(CharSequence original) {
        try {
            String formatted = toDate(original).toString(longDate().withLocale(getDefault()));
            super.setSummary(formatted);
        } catch (IllegalArgumentException e) {
            super.setSummary(original);
        }
    }

}