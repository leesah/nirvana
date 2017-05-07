package name.leesah.nirvana.ui.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;

import org.joda.time.LocalDate;

import name.leesah.nirvana.R;

import static android.text.TextUtils.isEmpty;
import static name.leesah.nirvana.utils.DateTimeHelper.toDate;
import static name.leesah.nirvana.utils.DateTimeHelper.toText;
import static org.joda.time.format.DateTimeFormat.longDate;

/**
 * Created by sah on 2016-12-10.
 */

public class DatePreference extends DialogPreference {

    private DatePicker picker = null;
    private String value;

    public DatePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.dialog_date_picker);
    }


    @Override
    protected void onBindDialogView(View view) {
        picker = (DatePicker) view.findViewById(R.id.date_picker);
        if (!isEmpty(value)) {
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
        persistString(value);
        setSummary(value);
    }

    @Override
    public void setSummary(CharSequence original) {
        super.setSummary(original == null ?
                null : longDate().print(toDate(original)));
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
