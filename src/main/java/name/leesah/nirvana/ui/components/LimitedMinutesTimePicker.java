package name.leesah.nirvana.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.String.format;

/**
 * Created by sah on 2016-12-10.
 */

public class LimitedMinutesTimePicker extends TimePicker {

    private static final String TAG = LimitedMinutesTimePicker.class.getSimpleName();

    public static final Integer[] DISPLAYED_MINUTES = new Integer[]{0, 15, 30, 45};

    public LimitedMinutesTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setIs24HourView(true);

        limitDisplayedMinutes();
    }


    @Override
    public void setMinute(int minute) {
        Log.d(TAG, format("Setting minute to [%d].", minute));
        int roundedUp = roundUpMinute(minute);
        super.setMinute(roundedUp);

        if (roundedUp == 0)
            setHour(getHour() + 1);
    }

    @Override
    public int getMinute() {
        return DISPLAYED_MINUTES[super.getMinute()];
    }

    private void limitDisplayedMinutes() {
        try {
            Class<?> classForid = Class.forName("com.android.internal.R$id");
            Field field = classForid.getField("minute");
            NumberPicker minutePicker = (NumberPicker) findViewById(field.getInt(null));
            minutePicker.setMinValue(0);
            minutePicker.setMaxValue(DISPLAYED_MINUTES.length - 1);

            List<String> displayedValues = Stream.of(DISPLAYED_MINUTES).map(minute -> format("%02d", minute)).collect(Collectors.toList());
            minutePicker.setDisplayedValues(displayedValues.toArray(new String[0]));
            minutePicker.setWrapSelectorWheel(true);

        } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    int roundUpMinute(int minute) {
        return IntStream.range(0, DISPLAYED_MINUTES.length)
                .filter(i -> DISPLAYED_MINUTES[i] >= minute)
                .findFirst().orElse(0);
    }
}
