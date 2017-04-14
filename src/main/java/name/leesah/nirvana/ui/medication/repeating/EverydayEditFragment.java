package name.leesah.nirvana.ui.medication.repeating;

import android.os.Bundle;
import android.support.annotation.Nullable;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.repeating.RepeatingModel;
import name.leesah.nirvana.model.medication.repeating.Everyday;

/**
 * Created by sah on 2017-03-15.
 */

public class EverydayEditFragment extends RepeatingModelEditFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reportValidity(true);
    }

    @Override
    public RepeatingModel readModel() {
        return new Everyday();
    }


}
