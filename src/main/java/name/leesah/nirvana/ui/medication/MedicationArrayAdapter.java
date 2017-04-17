package name.leesah.nirvana.ui.medication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.Medication;

/**
 * Created by sah on 2016-12-11.
 */
class MedicationArrayAdapter extends ArrayAdapter<Medication> {

    private final RowActionsListener listener;

    MedicationArrayAdapter(Context context, int resource, List<Medication> objects, @NonNull RowActionsListener listener) {
        super(context, resource, objects);
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.medication_list_item, null);
        }

        ((TextView) view.findViewById(R.id.textView_name)).setText(getItem(position).getName());
        //TODO: set delete button listener
        //view.findViewById(R.id.imageButton_delete).setOnClickListener(v -> listener.onDeleteRow(position));

        return view;
    }

    interface RowActionsListener {
        void onEditRow(int row);

        void onDeleteRow(int row);
    }

}
