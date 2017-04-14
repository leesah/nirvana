package name.leesah.nirvana.ui.medication.reminding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.stream.Collectors;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.reminding.RemindingModel;
import name.leesah.nirvana.model.medication.reminding.AtCertainHours;
import name.leesah.nirvana.model.reminder.TimedDosage;
import name.leesah.nirvana.ui.components.TimedDosageEditorCard;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static org.parceler.Parcels.unwrap;
import static org.parceler.Parcels.wrap;

public class CertainHoursEditFragment extends RemindingModelEditFragment {

    private static final String TAG = CertainHoursEditFragment.class.getSimpleName();
    public static final String KEY_DOSAGES = "dosages";
    private final ArrayList<TimedDosage> dosages = new ArrayList<>();
    private TimedDosageArrayAdapter adapter;
    private NumberPicker numberPicker;
    private TimePicker timePicker;
    private TimedDosageEditorCard footer;
    private View emptyView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reminding_model_details_certain_hours, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeAdapter();
        initializeFooter();
        initializeEmptyView(view);
        initializeListView(view);
        showListFooter();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_DOSAGES)) {
            dosages.addAll(unwrap(savedInstanceState.getParcelable(KEY_DOSAGES)));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_DOSAGES, wrap(dosages));
    }

    @Override
    public RemindingModel readModel() {
        return new AtCertainHours(dosages);
    }

    private void initializeAdapter() {
        adapter = new TimedDosageArrayAdapter(getContext(), R.layout.list_item_timed_dosage, R.id.textView_dosage, dosages);
        adapter.setOnSaveListener(this::onSaveDosage);
        adapter.setOnDeleteListener(this::onDeleteDosage);
    }

    private void initializeFooter() {
        footer = new TimedDosageEditorCard(getContext(), null);
        footer.setAddMode();
        footer.setOnSaveListener(this::onAddDosage);
    }

    private void initializeEmptyView(View view) {
        emptyView = view.findViewById(R.id.dosage_list_empty);
        ((TimedDosageEditorCard) emptyView.findViewById(R.id.editor_card)).setOnSaveListener(this::onAddDosage);
    }

    private void initializeListView(View view) {
        ListView listView = (ListView) view.findViewById(R.id.listView_dosages);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((p, v, position, n) -> editRow(position));
        listView.addFooterView(footer);
        listView.setEmptyView(emptyView);
    }

    private void showListFooter() {
        footer.setVisibility(View.VISIBLE);
    }

    private void hideListFooter() {
        footer.setVisibility(View.GONE);
    }

    public void editRow(int position) {
        reportValidity(false);
        hideListFooter();
        adapter.setEditing(position);
        adapter.notifyDataSetChanged();
    }

    private void onAddDosage(TimedDosage dosage) {
        if (dosageExists(dosage.getTimeOfDay())) {
            Toast.makeText(getContext(), R.string.err_dosage_already_added, Toast.LENGTH_LONG).show();
            return;
        }

        dosages.add(dosage);
        dosages.sort(TimedDosage.comparator);
        adapter.notifyDataSetChanged();
        reportValidity(true);
    }

    public void onSaveDosage(int position, TimedDosage dosage) {
        if (dosageExists(dosage.getTimeOfDay())) {
            Toast.makeText(getContext(), R.string.err_dosage_already_added, Toast.LENGTH_LONG).show();
            return;
        }

        dosages.set(position, dosage);
        adapter.setEditingFinished();
        adapter.notifyDataSetChanged();
        showListFooter();
    }

    public void onDeleteDosage(int position) {
        dosages.remove(position);
        adapter.setEditingFinished();
        adapter.notifyDataSetChanged();
        showListFooter();
        reportValidity(false);
    }

    private boolean dosageExists(LocalTime timeOfDay) {
        return !dosages.stream()
                .filter(d -> d.getTimeOfDay().equals(timeOfDay))
                .collect(Collectors.toSet())
                .isEmpty();
    }

}
