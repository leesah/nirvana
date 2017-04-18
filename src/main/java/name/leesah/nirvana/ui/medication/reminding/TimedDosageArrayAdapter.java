package name.leesah.nirvana.ui.medication.reminding;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import name.leesah.nirvana.model.reminder.TimedDosage;
import name.leesah.nirvana.ui.tweaks.TimedDosageEditorCard;

/**
 * Created by sah on 2016-12-11.
 */
public class TimedDosageArrayAdapter extends ArrayAdapter<TimedDosage> {

    private int editing = -1;
    private OnSaveDosageListener onSaveListener;
    private OnDeleteDosageListener onDeleteListener;

    TimedDosageArrayAdapter(Context context, int resource, int textViewResourceId, List<TimedDosage> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (isEditing(position)) {
            TimedDosageEditorCard view = new TimedDosageEditorCard(getContext(), null);
            view.setEditMode();
            view.setDosage(getItem(position));
            view.setOnSaveListener(dosage -> onSaveListener.onSaveDosage(position, dosage));
            view.setOnDeleteListener(() -> this.onDeleteListener.onDeleteDosage(position));
            return view;
        } else if (convertView instanceof TimedDosageEditorCard) {
            return super.getView(position, null, parent);
        } else {
            return super.getView(position, convertView, parent);
        }
    }

    private boolean isEditing(int position) {
        return position == editing;
    }

    public void setEditing(int postition) {
        this.editing = postition;
    }

    public void setEditingFinished() {
        this.editing = -1;
    }

    public void setOnSaveListener(OnSaveDosageListener onSaveListener) {
        this.onSaveListener = onSaveListener;
    }

    public void setOnDeleteListener(OnDeleteDosageListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    public interface OnSaveDosageListener {
        void onSaveDosage(int position, TimedDosage dosage);
    }

    public interface OnDeleteDosageListener {
        void onDeleteDosage(int position);
    }
}
