package name.leesah.nirvana.ui.medication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.Medication;

import static name.leesah.nirvana.ui.medication.MedicationEditActivity.ACTION_EDIT_MEDICATION;
import static name.leesah.nirvana.ui.medication.MedicationEditActivity.EXTRA_MEDICATION_ID;

/**
 * Created by sah on 2016-12-11.
 */
class MedicationArrayAdapter extends ArrayAdapter<Medication> {

    MedicationArrayAdapter(Context context, List<Medication> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = LayoutInflater.from(getContext()).inflate(R.layout.medication_card, parent, false);

        Medication medication = getItem(position);
        ((TextView) view.findViewById(R.id.name)).setText(medication.getName());
        ((TextView) view.findViewById(R.id.manufacturer)).setText(medication.getManufacturer());
        ((TextView) view.findViewById(R.id.repeating)).setText(medication.getRepeatingModel().toString(getContext()));
        ((TextView) view.findViewById(R.id.reminding)).setText(medication.getRemindingModel().toString(getContext()));
        ((TextView) view.findViewById(R.id.suggestions)).setText("");
        ImageView dosageForm = (ImageView) view.findViewById(R.id.dosageForm);
        switch (medication.getForm()) {
            case CAPSULE:
                dosageForm.setImageResource(R.drawable.ic_capsule);
                break;
            case TABLET:
                dosageForm.setImageResource(R.drawable.ic_tablet);
                break;
        }
        dosageForm.setImageTintList(getContext().getColorStateList(R.color.dosage_form_icon));

        view.setOnClickListener(v -> editMedication(medication.getId()));
        return view;
    }

    private void editMedication(int medicationId) {
        Intent intent = new Intent(getContext(), MedicationEditActivity.class)
                .setAction(ACTION_EDIT_MEDICATION)
                .putExtra(EXTRA_MEDICATION_ID, medicationId);
        getContext().startActivity(intent);
    }

}
