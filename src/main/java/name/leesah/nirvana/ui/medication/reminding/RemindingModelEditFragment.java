package name.leesah.nirvana.ui.medication.reminding;

import android.app.Fragment;

import name.leesah.nirvana.model.medication.reminding.RemindingModel;
import name.leesah.nirvana.ui.medication.MedicationEditActivity;

/**
 * Created by sah on 2016-12-11.
 */
public abstract class RemindingModelEditFragment extends Fragment {

    private MedicationEditActivity.ValidityReportListener validityReportListener;

    public abstract RemindingModel readModel();

    public void setValidityReportListener(MedicationEditActivity.ValidityReportListener listener) {
        this.validityReportListener = listener;
    }

    protected void reportValidity(boolean valid) {
        validityReportListener.onValidityReport(valid);
    }
}
