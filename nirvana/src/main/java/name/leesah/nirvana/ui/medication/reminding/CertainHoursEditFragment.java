package name.leesah.nirvana.ui.medication.reminding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.stream.Collectors;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.reminding.AtCertainHours;
import name.leesah.nirvana.model.medication.reminding.RemindingStrategy;
import name.leesah.nirvana.model.reminder.TimedDosage;
import name.leesah.nirvana.ui.widget.TimedDosageEditorCard;

import static org.parceler.Parcels.unwrap;
import static org.parceler.Parcels.wrap;

public class CertainHoursEditFragment extends RemindingModelEditFragment {

    private static final String TAG = CertainHoursEditFragment.class.getSimpleName();
    public static final String KEY_DOSAGES = "name.leesah.nirvana:key:DOSAGE";
    private final ArrayList<TimedDosage> dosages = new ArrayList<>();
    private TimedDosageArrayAdapter adapter;
    private TimedDosageEditorCard footer;
    private View emptyView;
    private AtCertainHours editingExisting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.timed_dosage_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new TimedDosageArrayAdapter(getContext(), dosages);
        adapter.setOnSaveListener(this::onSaveDosage);
        adapter.setOnDeleteListener(this::onDeleteDosage);

        footer = new TimedDosageEditorCard(getContext(), null);
        footer.setAddMode();
        footer.setOnSaveListener(this::onAddDosage);

        emptyView = view.findViewById(R.id.empty_view);
        ((TimedDosageEditorCard) emptyView.findViewById(R.id.editor_card)).setOnSaveListener(this::onAddDosage);

        ListView listView = (ListView) view.findViewById(R.id.dosages);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((p, v, position, n) -> editRow(position));
        listView.addFooterView(footer);
        listView.setEmptyView(emptyView);

        footer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_DOSAGES)) {
            dosages.addAll(unwrap(savedInstanceState.getParcelable(KEY_DOSAGES)));
            adapter.notifyDataSetChanged();
            reportValidity(true);
        } else if (editingExisting != null) {
            dosages.addAll(editingExisting.getDosages());
            adapter.notifyDataSetChanged();
            reportValidity(true);
            editingExisting = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_DOSAGES, wrap(dosages));
    }

    @Override
    public RemindingStrategy readModel() {
        return new AtCertainHours(dosages);
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
        footer.setVisibility(View.VISIBLE);
    }

    public void onDeleteDosage(int position) {
        dosages.remove(position);
        adapter.setEditingFinished();
        adapter.notifyDataSetChanged();
        footer.setVisibility(View.VISIBLE);
        reportValidity(false);
    }

    private boolean dosageExists(LocalTime timeOfDay) {
        return !dosages.stream()
                .filter(d -> d.getTimeOfDay().equals(timeOfDay))
                .collect(Collectors.toSet())
                .isEmpty();
    }

    public void setEditingExisting(AtCertainHours existing) {
        this.editingExisting = existing;
    }
}
