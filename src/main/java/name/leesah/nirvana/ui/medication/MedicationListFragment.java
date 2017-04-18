package name.leesah.nirvana.ui.medication;

import android.app.Activity;
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

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.data.Pharmacist;

/**
 * A placeholder fragment containing a simple view.
 */
public class MedicationListFragment extends Fragment implements MedicationArrayAdapter.RowActionsListener {

    private final ArrayList<Medication> medications = new ArrayList<>();
    private ArrayAdapter<Medication> arrayAdapter;

    public MedicationListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.medication_list, container, false);

        arrayAdapter = new MedicationArrayAdapter(getContext(), R.layout.medication_card, medications, this);

        ListView listView = (ListView) view.findViewById(R.id.medications);
        listView.setAdapter(arrayAdapter);
        listView.setEmptyView(view.findViewById(R.id.empty_view));

        FloatingActionButton newMedicationButton = (FloatingActionButton) view.findViewById(R.id.add_button);
        newMedicationButton.setOnClickListener(this::newMedication);

        reloadMedications();
        return view;
    }

    @Override
    public void onEditRow(int row) {

    }

    @Override
    public void onDeleteRow(int row) {

    }

    public void newMedication(View view) {
        Intent intent = new Intent(getContext(), MedicationEditActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            reloadMedications();
        }
    }

    private void reloadMedications() {
        medications.clear();
        medications.addAll(Pharmacist.getInstance(getContext()).getMedications());
        arrayAdapter.notifyDataSetChanged();
    }
}
