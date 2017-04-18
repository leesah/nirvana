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

import static org.parceler.Parcels.wrap;

public class EveryNHoursEditFragment extends RemindingModelEditFragment {

    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_UNIT = "unit";
    public static final String KEY_EVERY_N = "every n";
    public static final String[] EVERY_N_VALUES = {"4", "6", "8", "12"};
    private NumberPicker amount;
    private TextView unit;
    private NumberPicker everyN;
    private TimePicker firstDoseTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.medication_reminding_model_details_every_n_hours, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        amount = (NumberPicker) view.findViewById(R.id.amount);
        unit = (TextView) view.findViewById(R.id.unit);
        everyN = (NumberPicker) view.findViewById(R.id.every_n);
        firstDoseTime = (TimePicker) view.findViewById(R.id.first_dose_time);

        amount.setMinValue(1);
        amount.setMaxValue(99);

        everyN.setMinValue(0);
        everyN.setMaxValue(EVERY_N_VALUES.length - 1);
        everyN.setDisplayedValues(EVERY_N_VALUES);

        reportValidity(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            amount.setValue(savedInstanceState.getInt(KEY_AMOUNT, 1));
            unit.setText(savedInstanceState.getString(KEY_UNIT));
            everyN.setValue(savedInstanceState.getInt(KEY_EVERY_N, 8));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_AMOUNT, amount.getValue());
        outState.putString(KEY_UNIT, unit.getText().toString());
        outState.putInt(KEY_EVERY_N, everyN.getValue());
    }

    @Override
    public RemindingModel readModel() {
        LocalTime timeOfDay = new LocalTime(0).withHourOfDay(firstDoseTime.getHour()).withMinuteOfHour(firstDoseTime.getMinute());
        Integer everyN = Integer.valueOf(EVERY_N_VALUES[this.everyN.getValue()]);
        return new EveryNHours(new TimedDosage(timeOfDay, amount.getValue()), everyN);
    }

}
