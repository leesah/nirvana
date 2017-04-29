package name.leesah.nirvana.ui.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

import name.leesah.nirvana.R;

/**
 * Created by sah on 2016-12-10.
 */

public class IntegerPreference extends DialogPreference {

    private static final int DEFAULT_MIN_VALUE = 1;
    private static final int DEFAULT_MAX_VALUE = 99;

    private NumberPicker picker = null;

    private int value;
    private int minValue = DEFAULT_MIN_VALUE;
    private int maxValue = DEFAULT_MAX_VALUE;

    public IntegerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.integer_preference);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TimedDosageEditorCard,
                0, 0);
        try {
            minValue = a.getInt(R.styleable.IntegerPreference_minValue, DEFAULT_MIN_VALUE);
            maxValue = a.getInt(R.styleable.IntegerPreference_maxValue, DEFAULT_MAX_VALUE);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onBindDialogView(View view) {
        picker = (NumberPicker) view.findViewById(R.id.numberPicker);
        picker.setMinValue(minValue);
        picker.setMaxValue(maxValue);
        picker.setValue(minValue < value && value < maxValue ? value : minValue);

        super.onBindDialogView(view);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            int newValue = picker.getValue();
            if (callChangeListener(newValue)) {
                value = newValue;
                setSummary(String.valueOf(value));
                persistInt(newValue);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return (a.getInt(index, minValue));
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        value = restoreValue ? getPersistedInt(minValue) : (int) defaultValue;
        persistInt(value);
        setSummary(String.valueOf(value));
    }

    @Override
    public CharSequence getSummary() {
        CharSequence summary = super.getSummary();
        return TextUtils.isEmpty(summary) ? summary : String.format(summary.toString(), value);
    }

    public int getValue() {
        return value;
    }

}