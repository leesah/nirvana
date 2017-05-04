package name.leesah.nirvana.ui.settings.treatment;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import name.leesah.nirvana.R;

import static android.view.View.MeasureSpec.UNSPECIFIED;
import static android.view.View.MeasureSpec.makeMeasureSpec;
import static java.lang.String.format;

/**
 * Created by sah on 2017-04-17.
 */

public class TreatmentRepeatingModelSelectFragment extends PreferenceFragment {
    private static final String TAG = TreatmentRepeatingModelSelectFragment.class.getSimpleName();
    private ListPreference models;
    private NotRepeatingDetailsFragment notRepeating = new NotRepeatingDetailsFragment();
    private NTimesDetailsFragment nTimes = new NTimesDetailsFragment();
    private UntilDateDetailsFragment untilDate = new UntilDateDetailsFragment();
    private boolean listViewOptimized = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        addPreferencesFromResource(R.xml.prefscr_settings_treatment_repeating_model);

        models = (ListPreference) findPreference(getString(R.string.pref_key_treatment_recurring));
        models.setOnPreferenceChangeListener((p, v) -> switchDetailsFragment(p, v.toString()));
    }

    private boolean switchDetailsFragment(Preference preference, String choice) {
        preference.setSummary(choice);

        if (choice.equals(getString(R.string.treatment_recurring_none))) {
            replaceFragment(notRepeating);
            return true;
        } else if (choice.equals(getString(R.string.treatment_recurring_n_times))) {
            replaceFragment(nTimes);
            return true;
        } else if (choice.equals(getString(R.string.treatment_recurring_until_date))) {
            replaceFragment(untilDate);
            return true;
        }
        return false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_and_details, container, false);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.save_button).setEnabled(false);
        super.onPrepareOptionsMenu(menu);
    }

    protected void replaceFragment(Fragment fragment) {
        optimizeListViewHeight();
        getFragmentManager().beginTransaction()
                .replace(R.id.details_container, fragment)
                .commit();
    }

    private void optimizeListViewHeight() {
        if (listViewOptimized)
            return;

        ListView listView = (ListView) getView().findViewById(android.R.id.list);
        Log.d(TAG, format("Initial height of android.R.id.list: [%d].", listView.getMeasuredHeight()));

        int totalHeight = 0;
        ListAdapter adapter = listView.getAdapter();
        int childCount = adapter.getCount();
        for (int i = 0; i < childCount; i++) {
            View row = adapter.getView(i, null, listView);
            row.measure(makeMeasureSpec(0, UNSPECIFIED), makeMeasureSpec(0, UNSPECIFIED));
            totalHeight += row.getMeasuredHeight();
            Log.d(TAG, format("Summed to [%d] after measured [%s].", totalHeight, row.toString()));
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        totalHeight += (listView.getDividerHeight() * (childCount - 1));
        Log.d(TAG, format("Optimized height of android.R.id.list: [%d].", totalHeight));

        params.height = totalHeight;
        listView.setLayoutParams(params);
        listView.requestLayout();

        listViewOptimized = true;
    }

}
