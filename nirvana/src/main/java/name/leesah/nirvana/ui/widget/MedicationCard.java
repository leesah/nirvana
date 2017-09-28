package name.leesah.nirvana.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
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
    private final ImageView dosageForm;
    private final Button edit;
    private final Button delete;
    private final Button cancel;
    private final View buttonBar;

    public MedicationCard(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        inflate(getContext(), R.layout.medication_card, this);
        name = (TextView) findViewById(R.id.name);
        manufacturer = (TextView) findViewById(R.id.manufacturer);
        repeating = (TextView) findViewById(R.id.repeating);
        reminding = (TextView) findViewById(R.id.reminding);
        dosageForm = (ImageView) findViewById(R.id.icon);
        buttonBar = findViewById(R.id.button_bar);
        edit = (Button) findViewById(R.id.edit_button);
        delete = (Button) findViewById(R.id.delete_button);
        cancel = (Button) findViewById(R.id.cancel_button);
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

    public void showButtons(Runnable onEdit, Runnable onDelete, Runnable onCancel) {
        buttonBar.setVisibility(VISIBLE);
        edit.setOnClickListener(v -> onEdit.run());
        delete.setOnClickListener(v -> onDelete.run());
        cancel.setOnClickListener(v -> onCancel.run());
    }

    public void hideButtons() {
        buttonBar.setVisibility(GONE);
    }

}
