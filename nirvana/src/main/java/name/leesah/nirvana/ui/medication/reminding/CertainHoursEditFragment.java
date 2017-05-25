package name.leesah.nirvana.ui.medication.reminding;

import android.os.Bundle;
import android.support.annotation.NonNull;
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
import name.leesah.nirvana.model.medication.reminding.CertainHours;
import name.leesah.nirvana.model.medication.reminding.RemindingStrategy;
import name.leesah.nirvana.model.reminder.TimedDosage;
import name.leesah.nirvana.ui.medication.StrategyEditFragment;
import name.leesah.nirvana.ui.widget.TimedDosageEditorCard;

public class CertainHoursEditFragment extends StrategyEditFragment.Reminding {

    private final ArrayList<TimedDosage> dosages = new ArrayList<>();
    private TimedDosageArrayAdapter adapter;
    private TimedDosageEditorCard footer;

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

        View emptyView = view.findViewById(R.id.empty_view);
        ((TimedDosageEditorCard) emptyView.findViewById(R.id.editor_card)).setOnSaveListener(this::onAddDosage);

        ListView listView = (ListView) view.findViewById(R.id.dosages);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((p, v, position, n) -> editRow(position));
        listView.addFooterView(footer);
        listView.setEmptyView(emptyView);
    }


    @Override
    @NonNull
    protected CertainHours readStrategy() {
        return new CertainHours(dosages);
    }

    @Override
    protected void updateView(RemindingStrategy strategy) {
        dosages.addAll(((CertainHours) strategy).getDosages());
        setSaveButtonEnabled(!dosages.isEmpty());
    }

    private void hideListFooter() {
        footer.setVisibility(View.GONE);
    }

    public void editRow(int position) {
        setSaveButtonEnabled(false);
        hideListFooter();
        adapter.setEditing(position);
        adapter.notifyDataSetChanged();
    }

    private void onAddDosage(TimedDosage dosage) {
        Bundle params = new Bundle();
        params.putCharSequence("DOSAGE", dosage.toString());
        analytics.logEvent("ADD_BUTTON", null);

        if (dosageExists(dosage.getTimeOfDay())) {
            Toast.makeText(getContext(), R.string.err_dosage_already_added, Toast.LENGTH_LONG).show();
            return;
        }

        dosages.add(dosage);
        dosages.sort(TimedDosage.comparator);
        adapter.notifyDataSetChanged();
        setSaveButtonEnabled(!dosages.isEmpty());
    }

    public void onSaveDosage(int position, TimedDosage dosage) {
        Bundle params = new Bundle();
        params.putCharSequence("DOSAGE", dosage.toString());
        analytics.logEvent("SAVE_BUTTON", null);

        if (dosageExists(dosage.getTimeOfDay())) {
            Toast.makeText(getContext(), R.string.err_dosage_already_added, Toast.LENGTH_LONG).show();
            return;
        }

        dosages.set(position, dosage);
        adapter.setEditingFinished();
        adapter.notifyDataSetChanged();
        footer.setVisibility(View.VISIBLE);
        setSaveButtonEnabled(!dosages.isEmpty());
    }

    public void onDeleteDosage(int position) {
        Bundle params = new Bundle();
        params.putCharSequence("DOSAGE", dosages.get(position).toString());
        analytics.logEvent("DELETE_BUTTON", null);

        dosages.remove(position);
        adapter.setEditingFinished();
        adapter.notifyDataSetChanged();
        footer.setVisibility(View.VISIBLE);
        setSaveButtonEnabled(!dosages.isEmpty());
    }

    private boolean dosageExists(LocalTime timeOfDay) {
        return !dosages.stream()
                .filter(d -> d.getTimeOfDay().equals(timeOfDay))
                .collect(Collectors.toSet())
                .isEmpty();
    }

}
