package name.leesah.nirvana.ui.medication.reminding;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import static name.leesah.nirvana.DebugTools.isDeveloperModeOn;
import static name.leesah.nirvana.R.layout.every_n_hours;
import static name.leesah.nirvana.R.layout.in_development;
import static name.leesah.nirvana.model.medication.reminding.EveryNHours.VALID_VALUES;

public class EveryNHoursEditFragment extends StrategyEditFragment.Reminding {

    private NumberPicker amount;
    private TextView unit;
    private NumberPicker n;
    private TimePicker firstDoseTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(isDeveloperModeOn(getContext()) ? every_n_hours : in_development, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        amount = view.findViewById(R.id.amount);
        unit = view.findViewById(R.id.unit);
        n = view.findViewById(R.id.every_n);
        firstDoseTime = view.findViewById(R.id.first_dose_time);
    }

    @Override
    @NonNull
    protected EveryNHours readStrategy() {
        LocalTime timeOfDay = new LocalTime(0)
                .withHourOfDay(firstDoseTime.getHour())
                .withMinuteOfHour(firstDoseTime.getMinute());
        Integer everyN = VALID_VALUES.get(n.getValue());

        return new EveryNHours(
                new TimedDosage(timeOfDay, amount.getValue()),
                everyN);
    }

    @Override
    protected void updateView(RemindingStrategy strategy) {
        EveryNHours enh = (EveryNHours) strategy;
        amount.setValue(enh.getFirstDose().getAmount());
        n.setValue(enh.getN());
        firstDoseTime.setHour(enh.getFirstDose().getTimeOfDay().getHourOfDay());
        firstDoseTime.setMinute(enh.getFirstDose().getTimeOfDay().getMinuteOfHour());
    }

}
