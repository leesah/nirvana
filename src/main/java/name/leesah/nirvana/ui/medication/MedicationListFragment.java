package name.leesah.nirvana.ui.medication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.data.Pharmacist;

import static android.app.Activity.RESULT_OK;
import static name.leesah.nirvana.ui.medication.MedicationEditActivity.ACTION_ADD_MEDICATION;
import static name.leesah.nirvana.ui.medication.MedicationEditActivity.ACTION_EDIT_MEDICATION;
import static name.leesah.nirvana.ui.medication.MedicationEditActivity.EXTRA_MEDICATION_ID;

/**
 * A placeholder fragment containing a simple view.
 */
public class MedicationListFragment extends Fragment {

    private final ArrayList<Medication> medications = new ArrayList<>();
    private ArrayAdapter<Medication> arrayAdapter;

    public MedicationListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.medication_list, container, false);

        arrayAdapter = new MedicationArrayAdapter(getContext(), medications);

        ListView listView = (ListView) view.findViewById(R.id.medications);
        listView.setAdapter(arrayAdapter);
        listView.setEmptyView(view.findViewById(R.id.empty_view));

        FloatingActionButton addButton = (FloatingActionButton) view.findViewById(R.id.add_button);
        addButton.setOnClickListener(this::addMedication);

        reloadMedications();
        return view;
    }

    public void addMedication(View view) {
        Intent intent = new Intent(getContext(), MedicationEditActivity.class)
                .setAction(ACTION_ADD_MEDICATION);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null
                && Arrays.asList(ACTION_ADD_MEDICATION, ACTION_EDIT_MEDICATION).contains(data.getAction()))
            reloadMedications();
    }

    private void reloadMedications() {
        medications.clear();
        medications.addAll(Pharmacist.getInstance(getContext()).getMedications());
        arrayAdapter.notifyDataSetChanged();
    }

    /**
     * Created by sah on 2016-12-11.
     */
    static class MedicationArrayAdapter extends ArrayAdapter<Medication> {

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
}
