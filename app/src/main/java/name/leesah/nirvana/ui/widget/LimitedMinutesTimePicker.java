package name.leesah.nirvana.ui.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.google.common.collect.ImmutableList;

import org.joda.time.DateTime;

import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import name.leesah.nirvana.DebugTools;
import name.leesah.nirvana.utils.DateTimeHelper;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Locale.getDefault;

/**
 * Created by sah on 2016-12-10.
 */

public class LimitedMinutesTimePicker extends TimePicker {

    private static final String TAG = LimitedMinutesTimePicker.class.getSimpleName();

    public static final List<Integer> DISPLAYED_MINUTES = ImmutableList.of(0, 15, 30, 45);

    public LimitedMinutesTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setIs24HourView(true);

        limitDisplayedMinutes();
        setMinute(DateTime.now().getMinuteOfHour());
    }

    @Override
    public void setMinute(int minute) {
        int roundedUp = roundUpMinute(minute);
        Log.d(TAG, format("Minute rounded up: [%d] -> [%d].", minute, roundedUp));
        super.setMinute(DISPLAYED_MINUTES.indexOf(roundedUp));
    }

    @Override
    public int getMinute() {
        return DISPLAYED_MINUTES.get(super.getMinute());
    }

    @TargetApi(24)
    @SuppressWarnings("deprecation")
    @Override
    public void setCurrentMinute(@NonNull Integer currentMinute) {
        setMinute(currentMinute);
    }

    @TargetApi(24)
    @SuppressWarnings("deprecation")
    @NonNull
    @Override
    public Integer getCurrentMinute() {
        return getMinute();
    }

    private void limitDisplayedMinutes() {
        try {
            @SuppressLint("PrivateApi")
            Class<?> classForId = Class.forName("com.android.internal.R$id");

            Field field = classForId.getField("minute");
            NumberPicker minutePicker = (NumberPicker) findViewById(field.getInt(null));
            minutePicker.setMinValue(0);
            minutePicker.setMaxValue(DISPLAYED_MINUTES.size() - 1);

            List<String> displayedValues = DISPLAYED_MINUTES.stream()
                    .map(minute -> format(getDefault(), "%02d", minute))
                    .collect(Collectors.toList());
            minutePicker.setDisplayedValues(displayedValues.toArray(new String[0]));
            minutePicker.setWrapSelectorWheel(true);

            Log.d(TAG, format(getDefault(),"Displayed minutes limited to [%s].", DISPLAYED_MINUTES.toString()));
        } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException e) {
            throw new IllegalStateException("Unexpected error occurred trying to limit displayed minutes:", e);
        }
    }

    /**
     * Round up {@code minute}. e.g.:
     *
     * <pre>{@code
     *        0 -> 0
     *  [1, 15] -> 15
     * [16, 30] -> 30
     * [31, 45] -> 45
     * [46, 59] -> 0
     * }</pre>
     *
     * @param minute an int in [0-59]
     * @return Minute, rounded up.
     */
    static int roundUpMinute(int minute) {
        checkArgument(minute >= 0 && minute < 60, "Minute must be in range [0, 59]. However, %d is provided.", minute);
        return DISPLAYED_MINUTES.stream().sorted()
                .filter(displayed -> displayed >= minute)
                .findFirst().orElse(0);
    }
}
