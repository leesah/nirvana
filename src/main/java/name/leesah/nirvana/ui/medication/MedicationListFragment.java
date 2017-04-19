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
import name.leesah.nirvana.ui.tweaks.MedicationCard;

import static android.app.Activity.RESULT_OK;
import static name.leesah.nirvana.ui.medication.MedicationEditActivity.ACTION_ADD_MEDICATION;
import static name.leesah.nirvana.ui.medication.MedicationEditActivity.ACTION_EDIT_MEDICATION;
import static name.leesah.nirvana.ui.medication.MedicationEditActivity.EXTRA_MEDICATION_ID;

/**
 * A placeholder fragment containing a simple view.
 */
public class MedicationListFragment extends Fragment {

    public static final int REQUEST_CODE_ADD_MEDICATION = 1000;
    public static final int REQUEST_CODE_EDIT_MEDICATION = 2000;
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
        startActivityForResult(intent, REQUEST_CODE_ADD_MEDICATION);
    }

    private void editMedication(int medicationId) {
        Intent intent = new Intent(getContext(), MedicationEditActivity.class)
                .setAction(ACTION_EDIT_MEDICATION)
                .putExtra(EXTRA_MEDICATION_ID, medicationId);
        startActivityForResult(intent, REQUEST_CODE_EDIT_MEDICATION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Arrays.asList(REQUEST_CODE_ADD_MEDICATION, REQUEST_CODE_EDIT_MEDICATION).contains(requestCode)
                && resultCode == RESULT_OK)
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
    class MedicationArrayAdapter extends ArrayAdapter<Medication> {

        MedicationArrayAdapter(Context context, List<Medication> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            MedicationCard view = convertView == null ? new MedicationCard(getContext(), null) : (MedicationCard) convertView;
            Medication medication = getItem(position);
            view.setMedication(medication);
            view.setOnClickListener(v -> editMedication(medication.getId()));
            return view;
        }

    }
}
