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
import name.leesah.nirvana.model.medication.repeating.RepeatingStrategy;
import name.leesah.nirvana.model.medication.repeating.WithInterval;
import name.leesah.nirvana.ui.medication.MedicationActivity;
import name.leesah.nirvana.ui.medication.StrategyEditFragment;

/**
 * Created by sah on 2017-04-16.
 */

public class WithIntervalEditFragment extends StrategyEditFragment.Repeating {

    private InnerPreferenceFragment innerFragment = new InnerPreferenceFragment();

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
        getFragmentManager().beginTransaction()
                .replace(android.R.id.custom, innerFragment)
                .commit();
        setSaveButtonEnabled(true);
    }

    @NonNull
    @Override
    protected WithInterval readStrategy() {
        return new WithInterval(getSharedPreferences().getInt(
                getString(R.string.pref_key_medication_repeating_with_interval),
                0));
    }

    @Override
    protected void updateView(RepeatingStrategy strategy) {
    }

    public static class InnerPreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefscr_medication_repeating_every_n_days);
            getPreferenceManager().setSharedPreferencesName(MedicationActivity.STAGING);
        }

    }
}
