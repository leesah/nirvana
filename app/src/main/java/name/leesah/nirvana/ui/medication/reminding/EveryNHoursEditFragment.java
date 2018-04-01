package name.leesah.nirvana.ui.medication.reminding;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import org.joda.time.LocalTime;

import java.util.List;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.reminding.EveryNHours;
import name.leesah.nirvana.model.medication.reminding.RemindingStrategy;
import name.leesah.nirvana.model.reminder.TimedDosage;
import name.leesah.nirvana.ui.medication.StrategyEditFragment;

import static java.util.stream.Collectors.toList;
import static name.leesah.nirvana.DebugTools.isDeveloperModeOn;
import static name.leesah.nirvana.R.layout.every_n_hours;
import static name.leesah.nirvana.R.layout.in_development;
import static name.leesah.nirvana.model.medication.reminding.EveryNHours.VALID_VALUES;
import static name.leesah.nirvana.model.medication.reminding.EveryNHours.isCombinationLogical;

public class EveryNHoursEditFragment extends StrategyEditFragment.Reminding {

    private NumberPicker amount;
    private NumberPicker interval;
    private TimePicker firstDoseTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(isDeveloperModeOn(getContext()) ? every_n_hours : in_development, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        amount = view.findViewById(R.id.amount);
        interval = view.findViewById(R.id.every_n);
        firstDoseTime = view.findViewById(R.id.first_dose_time);

        amount.setMinValue(1);
        amount.setMaxValue(99);
        interval.setMinValue(0);
        firstDoseTime.setOnTimeChangedListener((v, h, m) -> refreshN(new LocalTime(h, m)));
        refreshN(LocalTime.now());
    }

    private void refreshN(LocalTime selectedTime) {
        List<Integer> validIntervals = VALID_VALUES.stream()
                .filter(n -> isCombinationLogical(selectedTime, n))
                .collect(toList());

        if (validIntervals.isEmpty()) {
            updateIntervalWidget(VALID_VALUES);
            interval.setEnabled(false);
            setSaveButtonEnabled(false);
        } else {
            updateIntervalWidget(validIntervals);
            interval.setEnabled(true);
            setSaveButtonEnabled(true);
        }
    }

    private void updateIntervalWidget(List<Integer> validIntervals) {
        interval.setMaxValue(0);
        interval.setDisplayedValues(valuesAsStringArray(validIntervals));
        interval.setMaxValue(validIntervals.size() - 1);
    }

    @NonNull
    private String[] valuesAsStringArray(List<Integer> values) {
        return values.stream()
                .map(String::valueOf)
                .collect(toList())
                .toArray(new String[]{});
    }

    @Override
    @NonNull
    protected EveryNHours readStrategy() {
        LocalTime timeOfDay = new LocalTime(0)
                .withHourOfDay(firstDoseTime.getHour())
                .withMinuteOfHour(firstDoseTime.getMinute());
        TimedDosage firstDose = new TimedDosage(timeOfDay, amount.getValue());
        Integer interval = VALID_VALUES.get(this.interval.getValue());
        return new EveryNHours(firstDose, interval);
    }

    @Override
    protected void updateView(RemindingStrategy strategy) {
        EveryNHours enh = (EveryNHours) strategy;
        amount.setValue(enh.getFirstDose().getAmount());
        interval.setValue(enh.getInterval());
        firstDoseTime.setHour(enh.getFirstDose().getTimeOfDay().getHourOfDay());
        firstDoseTime.setMinute(enh.getFirstDose().getTimeOfDay().getMinuteOfHour());
    }

}
