package name.leesah.nirvana.ui.medication.reminding;

import android.os.Bundle;
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
import name.leesah.nirvana.model.medication.reminding.RemindingModel;
import name.leesah.nirvana.model.reminder.TimedDosage;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static name.leesah.nirvana.model.medication.reminding.EveryNHours.VALID_VALUES;
import static org.parceler.Parcels.wrap;

public class EveryNHoursEditFragment extends RemindingModelEditFragment {

    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_UNIT = "unit";
    public static final String KEY_EVERY_N = "every n";
    private NumberPicker amount;
    private TextView unit;
    private NumberPicker n;
    private TimePicker firstDoseTime;
    private EveryNHours editingExsiting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.every_n_hours, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        amount = (NumberPicker) view.findViewById(R.id.amount);
        unit = (TextView) view.findViewById(R.id.unit);
        n = (NumberPicker) view.findViewById(R.id.every_n);
        firstDoseTime = (TimePicker) view.findViewById(R.id.first_dose_time);

        amount.setMinValue(1);
        amount.setMaxValue(99);

        n.setMinValue(0);
        n.setMaxValue(VALID_VALUES.size() - 1);
        n.setDisplayedValues(VALID_VALUES.stream().map(String::valueOf).collect(toList()).toArray(new String[0]));

        reportValidity(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            amount.setValue(savedInstanceState.getInt(KEY_AMOUNT, 1));
            unit.setText(savedInstanceState.getString(KEY_UNIT));
            n.setValue(savedInstanceState.getInt(KEY_EVERY_N, 8));
        } else if (editingExsiting != null) {
            TimedDosage firstDose = editingExsiting.getFirstDose();
            amount.setValue(firstDose.getAmount());
            n.setValue(editingExsiting.getN());
            firstDoseTime.setHour(firstDose.getTimeOfDay().getHourOfDay());
            firstDoseTime.setMinute(firstDose.getTimeOfDay().getMinuteOfHour());
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_AMOUNT, amount.getValue());
        outState.putString(KEY_UNIT, unit.getText().toString());
        outState.putInt(KEY_EVERY_N, n.getValue());
    }

    @Override
    public RemindingModel readModel() {
        LocalTime timeOfDay = new LocalTime(0).withHourOfDay(firstDoseTime.getHour()).withMinuteOfHour(firstDoseTime.getMinute());
        Integer everyN = VALID_VALUES.get(n.getValue());
        return new EveryNHours(new TimedDosage(timeOfDay, amount.getValue()), everyN);
    }

    public void setEditingExsiting(EveryNHours editingExsiting) {
        this.editingExsiting = editingExsiting;
    }
}
