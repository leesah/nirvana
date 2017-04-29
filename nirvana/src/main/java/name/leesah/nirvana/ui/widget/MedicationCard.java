package name.leesah.nirvana.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.Medication;

/**
 * Created by sah on 2017-04-19.
 */

public class MedicationCard extends FrameLayout {
    private final TextView name;
    private final TextView manufacturer;
    private final TextView repeating;
    private final TextView reminding;
    private final TextView suggestions;
    private final ImageView dosageForm;

    public MedicationCard(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        inflate(getContext(), R.layout.medication_card, this);
        name = ((TextView) findViewById(R.id.name));
        manufacturer = ((TextView) findViewById(R.id.manufacturer));
        repeating = ((TextView) findViewById(R.id.repeating));
        reminding = ((TextView) findViewById(R.id.reminding));
        suggestions = ((TextView) findViewById(R.id.suggestions));
        dosageForm = (ImageView) findViewById(R.id.icon);
    }

    public void setMedication(@NonNull Medication medication) {
        name.setText(medication.getName());
        manufacturer.setText(medication.getManufacturer());
        reminding.setText(medication.getRemindingStrategy().toString(getContext()));
        repeating.setText(medication.getRepeatingStrategy().toString(getContext()));
        switch (medication.getForm()) {
            case CAPSULE:
                dosageForm.setImageResource(R.drawable.ic_capsule);
                break;
            case TABLET:
                dosageForm.setImageResource(R.drawable.ic_tablet);
                break;
        }
    }
}
