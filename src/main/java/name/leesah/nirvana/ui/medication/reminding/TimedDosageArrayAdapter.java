package name.leesah.nirvana.ui.medication.reminding;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.reminder.TimedDosage;
import name.leesah.nirvana.ui.tweaks.TimedDosageCard;
import name.leesah.nirvana.ui.tweaks.TimedDosageEditorCard;

/**
 * Created by sah on 2016-12-11.
 */
public class TimedDosageArrayAdapter extends ArrayAdapter<TimedDosage> {

    private int editing = -1;
    private OnSaveDosageListener onSaveListener;
    private OnDeleteDosageListener onDeleteListener;

    TimedDosageArrayAdapter(Context context, List<TimedDosage> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        TimedDosage dosage = getItem(position);
        if (isEditing(position)) {
            TimedDosageEditorCard view = convertView instanceof TimedDosageEditorCard ? (TimedDosageEditorCard) convertView : new TimedDosageEditorCard(getContext(), null);
            view.setEditMode();
            view.setDosage(dosage);
            view.setOnSaveListener(d -> onSaveListener.onSaveDosage(position, d));
            view.setOnDeleteListener(() -> this.onDeleteListener.onDeleteDosage(position));
            return view;
        } else {
            TimedDosageCard view = convertView instanceof TimedDosageCard ? (TimedDosageCard) convertView : new TimedDosageCard(getContext(), null);
            view.setDosage(dosage);
            return view;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return isEditing(position) ? 1 : 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
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
