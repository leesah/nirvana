package name.leesah.nirvana.ui.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import name.leesah.nirvana.R;

/**
 * Created by sah on 2017-04-17.
 */

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefscr_settings);

        //TODO: initialize to "forever"
        //TODO: fragment for repeating model selecting
    }


}
