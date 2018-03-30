package name.leesah.nirvana.ui.settings.treatment;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import name.leesah.nirvana.R;

/**
 * Created by sah on 2017-04-17.
 */

public class NTimesDetailsFragment extends PreferenceFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefscr_treatment_repeating_n_times);
    }
}
