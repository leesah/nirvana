package name.leesah.nirvana.ui.settings.treatment;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.joda.time.LocalDate;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.treatment.recurring.RecurringStrategy;
import name.leesah.nirvana.model.treatment.recurring.UntilDate;
import name.leesah.nirvana.ui.medication.StrategyEditFragment;

import static java.lang.String.format;
import static java.util.Locale.US;
import static name.leesah.nirvana.utils.DateTimeHelper.toDate;

/**
 * Created by sah on 2017-05-05.
 */

public class UntilDateEditFragment extends StrategyEditFragment.Recurring {

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
    protected RecurringStrategy readStrategy() {
        LocalDate date = toDate(
                getSharedPreferences().getString(
                        getContext().getString(R.string.pref_key_treatment_recurring_until_date),
                        null));
        if (date == null)
            throw new IllegalStateException(format(
                    US,"SharedPreference [%s] == [null]",
                    getContext().getString(R.string.pref_key_treatment_recurring_until_date)));

        return new UntilDate(date);
    }

    @Override
    protected void updateView(RecurringStrategy strategy) {
    }

    public static class InnerPreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefscr_treatment_repeating_until_date);
        }

    }
}
