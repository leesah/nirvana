package name.leesah.nirvana.ui.main;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.ui.medication.MedicationActivity;
import name.leesah.nirvana.ui.widget.MedicationCard;

import static android.app.Activity.RESULT_OK;
import static java.util.stream.Collectors.toList;
import static name.leesah.nirvana.PhoneBook.pharmacist;
import static name.leesah.nirvana.ui.medication.MedicationActivity.REQUEST_CODE_ADD_MEDICATION;
import static name.leesah.nirvana.ui.medication.MedicationActivity.REQUEST_CODE_EDIT_MEDICATION;

/**
 * A placeholder fragment containing a simple view.
 */
public class MedicationListFragment extends Fragment {

    private final List<Medication> medications = new ArrayList<>();
    private ArrayAdapter<Medication> adapter;
    private SwipeRefreshLayout refreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (medications.isEmpty()) medications.addAll(buildMedicationList());
        if (adapter == null) adapter = new MedicationArrayAdapter(getContext(), medications);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.medication_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ListView listView = (ListView) view.findViewById(R.id.medications);
        listView.setAdapter(adapter);
        listView.setEmptyView(view.findViewById(R.id.empty_view));

        FloatingActionButton addButton = (FloatingActionButton) view.findViewById(R.id.add_button);
        addButton.setOnClickListener(v -> MedicationActivity.add(getActivity()));

        refreshLayout = ((SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh));
        refreshLayout.setOnRefreshListener(() -> new RefreshTask().execute());

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.refresh_button, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_button:
                refreshLayout.setRefreshing(true);
                new RefreshTask().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Arrays.asList(REQUEST_CODE_ADD_MEDICATION, REQUEST_CODE_EDIT_MEDICATION).contains(requestCode)
                && resultCode == RESULT_OK)
            new RefreshTask().execute();
    }

    @NonNull
    private List<Medication> buildMedicationList() {
        return pharmacist(getContext()).getMedications().stream()
                .sorted().collect(toList());
    }

    private void onRefreshDone(List<Medication> result) {
        medications.clear();
        medications.addAll(result);
        if (adapter != null) adapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
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
            view.setOnClickListener(v -> MedicationActivity.edit(getActivity(), medication));
            return view;
        }

    }

    private class RefreshTask extends AsyncTask<Void, Void, List<Medication>> {
        @Override
        protected List<Medication> doInBackground(Void... voids) {
            return buildMedicationList();
        }

        @Override
        protected void onPostExecute(List<Medication> result) {
            super.onPostExecute(result);
            onRefreshDone(result);
        }

    }


}
