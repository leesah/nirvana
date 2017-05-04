package name.leesah.nirvana.ui.medication.reminding;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import org.joda.time.LocalTime;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.reminding.EveryNHours;
import name.leesah.nirvana.model.medication.reminding.RemindingStrategy;
import name.leesah.nirvana.model.reminder.TimedDosage;
import name.leesah.nirvana.ui.medication.StrategyEditFragment;

import static name.leesah.nirvana.model.medication.reminding.EveryNHours.VALID_VALUES;

public class EveryNHoursEditFragment extends StrategyEditFragment.Reminding {

    private NumberPicker amount;
    private TextView unit;
    private NumberPicker n;
    private TimePicker firstDoseTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.every_n_hours, container, false);
    }

    @Override
    @NonNull
    protected EveryNHours readStrategy() {
        LocalTime timeOfDay = new LocalTime(0).withHourOfDay(firstDoseTime.getHour()).withMinuteOfHour(firstDoseTime.getMinute());
        Integer everyN = VALID_VALUES.get(n.getValue());
        return new EveryNHours(new TimedDosage(timeOfDay, amount.getValue()), everyN);
    }

    @Override
    protected void updateView(RemindingStrategy strategy) {
        // TODO: initialize
    }

}
