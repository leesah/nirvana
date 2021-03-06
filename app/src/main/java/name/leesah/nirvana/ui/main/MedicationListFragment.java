package name.leesah.nirvana.ui.main;

import android.app.AlertDialog;
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

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.ui.medication.MedicationActivity;
import name.leesah.nirvana.ui.widget.MedicationCard;

import static android.app.Activity.RESULT_OK;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;
import static name.leesah.nirvana.PhoneBook.nurse;
import static name.leesah.nirvana.PhoneBook.pharmacist;
import static name.leesah.nirvana.persistence.Nurse.isFor;
import static name.leesah.nirvana.ui.medication.MedicationActivity.ACTION_ADD_MEDICATION;
import static name.leesah.nirvana.ui.medication.MedicationActivity.ACTION_EDIT_MEDICATION;
import static name.leesah.nirvana.ui.medication.MedicationActivity.REQUEST_CODE_ADD_MEDICATION;
import static name.leesah.nirvana.ui.medication.MedicationActivity.REQUEST_CODE_EDIT_MEDICATION;
import static name.leesah.nirvana.ui.medication.MedicationActivity.clearStaged;
import static name.leesah.nirvana.ui.medication.MedicationActivity.writeToStaged;

/**
 * A placeholder fragment containing a simple view.
 */
public class MedicationListFragment extends Fragment {

    private final List<Medication> medications = new ArrayList<>();
    private MedicationArrayAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private FirebaseAnalytics analytics;
    int selected = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        analytics = FirebaseAnalytics.getInstance(getContext());

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
        listView.setOnItemClickListener((p, v, position, n) -> {
            Bundle params = new Bundle();
            params.putInt("medication_id", medications.get(position).getId());
            params.putString("medication_name", medications.get(position).getName());
            analytics.logEvent("medication_button_bar_show", params);

            selected = selected == position ? -1 : position;
            adapter.notifyDataSetChanged();
        });

        FloatingActionButton addButton = (FloatingActionButton) view.findViewById(R.id.add_button);
        addButton.setOnClickListener(v -> add());

        refreshLayout = ((SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh));
        refreshLayout.setOnRefreshListener(() -> {
            analytics.logEvent("medication_list_refresh_swipe", null);
            new RefreshTask().execute();
        });

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
                analytics.logEvent("medication_list_refresh_menu", null);
                performRefresh();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Arrays.asList(REQUEST_CODE_ADD_MEDICATION, REQUEST_CODE_EDIT_MEDICATION).contains(requestCode)
                && resultCode == RESULT_OK)
            performRefresh();
    }

    @NonNull
    private List<Medication> buildMedicationList() {
        return pharmacist(getContext()).getMedications().stream()
                .sorted().collect(toList());
    }

    private void performRefresh() {
        refreshLayout.setRefreshing(true);
        new RefreshTask().execute();
    }

    private void onRefreshDone(List<Medication> result) {
        selected = -1;
        medications.clear();
        medications.addAll(result);
        if (adapter != null) adapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
    }

    public void add() {
        analytics.logEvent("medication_add", null);

        clearStaged(getContext());
        Intent intent = new Intent(getContext(), MedicationActivity.class)
                .setAction(ACTION_ADD_MEDICATION);
        startActivityForResult(intent, REQUEST_CODE_ADD_MEDICATION);
    }

    private void onEdit() {
        Bundle params = new Bundle();
        params.putInt("medication_id", medications.get(selected).getId());
        params.putString("medication_name", medications.get(selected).getName());
        analytics.logEvent("medication_edit", params);

        writeToStaged(getContext(), medications.get(selected));
        Intent intent = new Intent(getContext(), MedicationActivity.class)
                .setAction(ACTION_EDIT_MEDICATION);
        startActivityForResult(intent, REQUEST_CODE_EDIT_MEDICATION);
    }

    private void onDelete() {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.alert_delete_medication_title)
                .setMessage(getContext().getString(R.string.alert_delete_medication_message, medications.get(selected).getName()))
                .setPositiveButton(android.R.string.ok, (d, w) -> performDelete(medications.get(selected)))
                .setNegativeButton(android.R.string.cancel, null)
                .create().show();
    }

    private void performDelete(Medication medication) {
        Bundle params = new Bundle();
        params.putInt("medication_id", medication.getId());
        params.putString("medication_name", medication.getName());
        analytics.logEvent("medication_delete", params);

        nurse(getContext()).replace(isFor(medication), emptySet());
        pharmacist(getContext()).removeMedication(medication.getId());
        performRefresh();
    }

    private void onCancel() {
        selected = -1;
        adapter.notifyDataSetChanged();
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
            view.setMedication(getItem(position));
            if (position == selected)
                view.showButtons(() -> onEdit(), () -> onDelete(), () -> onCancel());
            else
                view.hideButtons();
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
