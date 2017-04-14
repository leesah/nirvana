package name.leesah.nirvana.ui.medication.repeating;

import android.app.Fragment;

import name.leesah.nirvana.model.medication.repeating.RepeatingModel;
import name.leesah.nirvana.ui.medication.MedicationEditActivity;

/**
 * Created by sah on 2017-04-16.
 */

abstract class RepeatingModelEditFragment extends Fragment{

    private MedicationEditActivity.ValidityReportListener validityReportListener;

    public abstract RepeatingModel readModel();

    public void setValidityReportListener(MedicationEditActivity.ValidityReportListener listener) {
        this.validityReportListener = listener;
    }

    protected void reportValidity(boolean valid) {
        validityReportListener.onValidityReport(valid);
    }
}
