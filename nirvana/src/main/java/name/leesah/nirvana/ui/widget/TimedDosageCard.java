package name.leesah.nirvana.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.reminder.TimedDosage;

/**
 * Created by sah on 2017-04-19.
 */

public class TimedDosageCard extends FrameLayout {
    private final TextView text;

    public TimedDosageCard(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.timed_dosage_card, this);
        text = (TextView)findViewById(R.id.text);
    }

    public void setDosage(TimedDosage dosage) {
        text.setText(dosage.toString(getContext()));
    }
}
