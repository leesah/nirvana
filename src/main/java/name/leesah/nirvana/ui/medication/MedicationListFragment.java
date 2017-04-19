package name.leesah.nirvana.ui.medication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

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
}
