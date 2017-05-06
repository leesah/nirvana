package name.leesah.nirvana.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import org.joda.time.LocalTime;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.reminder.TimedDosage;

import static java.lang.String.format;

/**
 * Created by sah on 2017-04-16.
 */

public class TimedDosageEditorCard extends FrameLayout {
    private static final String TAG = TimedDosageEditorCard.class.getSimpleName();
    private Button saveButton;
    private Button deleteButton;
    private NumberPicker numberPicker;
    private TimePicker timePicker;
    private OnSaveDosageListener onSaveListener;
    private OnDeleteDosageListener onDeleteListener;
    private final int mode;

    public TimedDosageEditorCard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TimedDosageEditorCard,
                0, 0);
        try {
            mode = a.getInt(R.styleable.TimedDosageEditorCard_mode, 0);
        } finally {
            a.recycle();
        }

        init(mode);
    }

    private void init(int mode) {
        inflate(getContext(), R.layout.timed_dosage_editor_card, this);
        numberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        saveButton = (Button) findViewById(R.id.save_button);
        deleteButton = (Button) findViewById(R.id.delete_button);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(99);

        saveButton.setOnClickListener(this::callOnSaveListener);
        deleteButton.setOnClickListener(this::callOnDeleteListener);

        if (mode == 0)
            setAddMode();
        else
            setEditMode();
    }

    public void setEditMode() {
        saveButton.setText(R.string.save);
        deleteButton.setVisibility(VISIBLE);
    }

    public void setAddMode() {
        saveButton.setText(R.string.add);
        deleteButton.setVisibility(GONE);

    }

    public void setOnSaveListener(OnSaveDosageListener listener) {
        onSaveListener = listener;
    }

    public void setOnDeleteListener(OnDeleteDosageListener listener) {
        onDeleteListener = listener;
    }

    private void callOnSaveListener(View view) {
        if (onSaveListener != null)
            onSaveListener.onSaveDosage(readDosage());
    }

    private void callOnDeleteListener(View view) {
        if (onDeleteListener != null)
            onDeleteListener.onDeleteDosage();
    }

    private TimedDosage readDosage() {
        LocalTime timeOfDay = new LocalTime(0)
                .withHourOfDay(timePicker.getHour())
                .withMinuteOfHour(timePicker.getMinute());
        return new TimedDosage(timeOfDay, numberPicker.getValue());
    }

    public void setDosage(TimedDosage dosage) {
        Log.d(TAG, format("Updating widgets according to dosage [%s].", dosage));
        LocalTime timeOfDay = dosage.getTimeOfDay();
        timePicker.setHour(timeOfDay.getHourOfDay());
        timePicker.setMinute(timeOfDay.getMinuteOfHour());
        numberPicker.setValue(dosage.getAmount());
    }

    @Keep
    public interface OnSaveDosageListener {
        void onSaveDosage(TimedDosage dosage);
    }

    @Keep
    public interface OnDeleteDosageListener {
        void onDeleteDosage();
    }
}
