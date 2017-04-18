package name.leesah.nirvana.ui.medication;

import android.content.Context;
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

/**
 * Created by sah on 2016-12-11.
 */
class MedicationArrayAdapter extends ArrayAdapter<Medication> {

    private final RowActionsListener listener;

    MedicationArrayAdapter(Context context, int resource, List<Medication> objects, @NonNull RowActionsListener listener) {
        super(context, resource, objects);
        this.listener = listener;
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

        return view;
    }

    interface RowActionsListener {
        void onEditRow(int row);

        void onDeleteRow(int row);
    }

}
