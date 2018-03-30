package name.leesah.nirvana.ui.settings.treatment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import name.leesah.nirvana.R;

/**
 * Created by sah on 2017-04-17.
 */

public class NotRepeatingDetailsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.treatment_not_repeating, container, false);
    }

}
