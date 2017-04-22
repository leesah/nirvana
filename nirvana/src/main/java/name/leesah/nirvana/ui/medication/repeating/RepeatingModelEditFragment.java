package name.leesah.nirvana.ui.medication.repeating;

import android.app.Fragment;

import name.leesah.nirvana.model.medication.repeating.RepeatingStrategy;
import name.leesah.nirvana.ui.medication.MedicationActivity;

/**
 * Created by sah on 2017-04-16.
 */

abstract class RepeatingModelEditFragment extends Fragment{

    private MedicationActivity.ValidityReportListener validityReportListener;

    public abstract RepeatingStrategy readModel();

    public void setValidityReportListener(MedicationActivity.ValidityReportListener listener) {
        this.validityReportListener = listener;
    }

    protected void reportValidity(boolean valid) {
        validityReportListener.onValidityReport(valid);
    }
}
