package name.leesah.nirvana.ui.medication.reminding;

import android.app.Fragment;

import name.leesah.nirvana.model.medication.reminding.RemindingStrategy;
import name.leesah.nirvana.ui.medication.MedicationActivity;

/**
 * Created by sah on 2016-12-11.
 */
public abstract class RemindingModelEditFragment extends Fragment {

    private MedicationActivity.ValidityReportListener validityReportListener;

    public abstract RemindingStrategy readModel();

    public void setValidityReportListener(MedicationActivity.ValidityReportListener listener) {
        this.validityReportListener = listener;
    }

    protected void reportValidity(boolean valid) {
        validityReportListener.onValidityReport(valid);
    }
}
