package name.leesah.nirvana.ui.preference;

import android.preference.DialogPreference;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import org.joda.time.LocalDate;

import name.leesah.nirvana.R;

import static android.text.TextUtils.*;
import static java.util.Locale.*;
import static name.leesah.nirvana.utils.DateTimeHelper.*;
import static org.joda.time.format.DateTimeFormat.*;

public class DatePreferenceDelegate extends StringValuedDialogPreferenceDelegate {
    static final String TAG = DatePreference.class.getSimpleName();
    DatePicker picker = null;

    public DatePreferenceDelegate(DialogPreference preference) {
        super(preference);
        preference.setDialogLayoutResource(R.layout.date_preference);
    }

    protected void onBindDialogView(View view) {
        picker = (DatePicker) view.findViewById(R.id.datePicker);
        if (!isEmpty(value)) {
            LocalDate date = toDate(value);
            picker.updateDate(date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth());
        }
    }

    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            LocalDate date = new LocalDate()
                    .withYear(picker.getYear())
                    .withMonthOfYear(picker.getMonth() + 1)
                    .withDayOfMonth(picker.getDayOfMonth());
            updateValue(toText(date));
        }
    }

    @Override
    protected String translateSummary(CharSequence original) {
        try {
        return original == null ? null : toDate(original).toString(longDate().withLocale(getDefault()));
        } catch (IllegalArgumentException e) {
            Log.wtf(TAG, String.format("Unexpected format: [%s].", original));
            return original.toString();
        }
    }

    public void setDate(@Nullable LocalDate date) {
        updateValue(toText(date));
    }

    @Nullable
    public LocalDate getDate() {
        return value == null ? null : toDate(value);
    }

}