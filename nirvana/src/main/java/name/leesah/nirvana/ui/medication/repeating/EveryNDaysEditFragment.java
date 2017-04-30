package name.leesah.nirvana.ui.medication.repeating;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.repeating.EveryNDays;
import name.leesah.nirvana.model.medication.repeating.RepeatingStrategy;
import name.leesah.nirvana.ui.preference.IntegerPreference;

/**
 * Created by sah on 2017-04-16.
 */

public class EveryNDaysEditFragment extends RepeatingModelEditFragment {

    private EveryNDaysEditPreferenceFragment innerFragment;

    public EveryNDaysEditFragment() {
        innerFragment = new EveryNDaysEditPreferenceFragment();
        innerFragment.onPreferenceChangeListener = (p, v) -> {reportValidity(true); return true;};
    }

    @Override
    public RepeatingStrategy readModel() {
        return new EveryNDays(innerFragment.integerPreference.getValue());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setId(android.R.id.custom);
        return frameLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.custom, innerFragment).commit();
    }

    public void setEditingExisting(EveryNDays editingExisting) {
        innerFragment.editingExisting = editingExisting;
    }


    public static class EveryNDaysEditPreferenceFragment extends PreferenceFragment {

        private EveryNDays editingExisting;
        private IntegerPreference integerPreference;
        public Preference.OnPreferenceChangeListener onPreferenceChangeListener;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefscr_medication_repeating_every_n_days);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            integerPreference = ((IntegerPreference) findPreference(getString(R.string.pref_key_medication_repeating_every_n_days)));
            integerPreference.setMinValue(2);
            integerPreference.setOnPreferenceChangeListener(onPreferenceChangeListener);
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            if (editingExisting != null) {
                integerPreference.setValue(editingExisting.getN());
            }
        }
    }
}
