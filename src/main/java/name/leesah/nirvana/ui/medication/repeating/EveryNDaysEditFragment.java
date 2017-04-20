package name.leesah.nirvana.ui.medication.repeating;

import android.app.Fragment;

import name.leesah.nirvana.model.medication.repeating.EveryNDays;
import name.leesah.nirvana.model.medication.repeating.RepeatingModel;

/**
 * Created by sah on 2017-04-16.
 */

public class EveryNDaysEditFragment extends RepeatingModelEditFragment {
    private EveryNDays editingExsiting;

    @Override
    public RepeatingModel readModel() {
        return null;
    }

    public void setEditingExsiting(EveryNDays editingExsiting) {
        this.editingExsiting = editingExsiting;
    }
}
