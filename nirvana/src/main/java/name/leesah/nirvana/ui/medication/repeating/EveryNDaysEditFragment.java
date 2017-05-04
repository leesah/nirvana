package name.leesah.nirvana.ui.medication.repeating;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.repeating.EveryNDays;
import name.leesah.nirvana.model.medication.repeating.RepeatingStrategy;
import name.leesah.nirvana.ui.medication.MedicationActivity;
import name.leesah.nirvana.ui.medication.StrategyEditFragment;

/**
 * Created by sah on 2017-04-16.
 */

public class EveryNDaysEditFragment extends StrategyEditFragment.Repeating {

    private EveryNDaysEditPreferenceFragment innerFragment;

    public EveryNDaysEditFragment() {
        innerFragment = new EveryNDaysEditPreferenceFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setId(android.R.id.custom);
        return frameLayout;
    }

    @NonNull
    @Override
    protected EveryNDays readStrategy() {
        return new EveryNDays(
                getStagingPreferences().getInt(getString(R.string.pref_key_medication_repeating_every_n_days), 0)
        );
    }

    @Override
    protected void updateView(RepeatingStrategy strategy) {
        getStagingPreferences().edit()
                .putInt(getString(R.string.pref_key_medication_repeating_every_n_days), ((EveryNDays)strategy).getN())
                .apply();
        setSaveButtonEnabled(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.custom, innerFragment).commit();
    }

    public static class EveryNDaysEditPreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefscr_medication_repeating_every_n_days);
            getPreferenceManager().setSharedPreferencesName(MedicationActivity.STAGING);
        }

    }
}
