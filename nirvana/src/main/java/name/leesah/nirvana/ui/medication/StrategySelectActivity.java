package name.leesah.nirvana.ui.medication;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

import name.leesah.nirvana.R;

import static android.view.View.MeasureSpec.UNSPECIFIED;
import static android.view.View.MeasureSpec.makeMeasureSpec;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Locale.US;
import static java.util.stream.Collectors.toList;
import static name.leesah.nirvana.ui.medication.StrategySelectActivity.ListAndDetailsFragment.EXTRA_FRAGMENTS;
import static name.leesah.nirvana.ui.medication.StrategySelectActivity.ListAndDetailsFragment.EXTRA_NAMES;
import static name.leesah.nirvana.ui.medication.StrategySelectActivity.ListAndDetailsFragment.EXTRA_TITLE;

/**
 * Created by sah on 2017-04-17.
 */
public class StrategySelectActivity extends AppCompatActivity {

    private static final String EXTRA_FRAGMENT_ARGS = "name.leesah.nirvana:extra:STRATEGY_FRAGMENT_ARGS";

    public static void start(Context context, @StringRes int title, String[] names, String[] fragments) {
        if (names.length != fragments.length)
            throw new IllegalArgumentException(format(US, "Number of names [%d] and fragments [%d] don't match.",
                    names.length, fragments.length));

        stream(fragments).forEach(StrategySelectActivity::validateClassName);

        final Bundle args = new Bundle(4);
        args.putInt(EXTRA_TITLE, title);
        args.putStringArray(EXTRA_NAMES, names);
        args.putStringArray(EXTRA_FRAGMENTS, fragments);
        context.startActivity(
                new Intent(context, StrategySelectActivity.class)
                        .putExtra(EXTRA_FRAGMENT_ARGS, args));
    }

    private static void validateClassName(String fragment) {
        try {
            if (!StrategyEditFragment.class.isAssignableFrom(Class.forName(fragment)))
                throw new IllegalArgumentException(format(US, "Not subclass of %s: [%s].",
                        StrategyEditFragment.class.getSimpleName(), fragment));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_container);

        ListAndDetailsFragment fragment = new ListAndDetailsFragment();
        fragment.setArguments(getIntent().getBundleExtra(EXTRA_FRAGMENT_ARGS));

        getFragmentManager().beginTransaction()
                .add(R.id.content, fragment)
                .commit();
    }

    public static class ListAndDetailsFragment extends PreferenceFragment {
        static final String EXTRA_TITLE = "name.leesah.nirvana:extra:STRATEGY_NAMES_TITLE";
        static final String EXTRA_NAMES = "name.leesah.nirvana:extra:STRATEGY_NAMES";
        static final String EXTRA_FRAGMENTS = "name.leesah.nirvana:extra:STRATEGY_FRAGMENTS";

        private boolean listViewOptimized = false;
        private String[] names;
        private List<Fragment> fragments;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefscr_medication_strategy_select);

            ListPreference list = (ListPreference) findPreference(getString(R.string.pref_key_strategies));
            list.setOnPreferenceChangeListener((p, v) -> onSelectStrategy(v.toString()));

            Bundle arguments = getArguments();
            list.setTitle(arguments.getInt(EXTRA_TITLE));

            names = arguments.getStringArray(EXTRA_NAMES);
            list.setEntries(names);
            list.setEntryValues(names);

            String[] classes = arguments.getStringArray(EXTRA_FRAGMENTS);
            fragments = stream(classes).map(this::instantiateFragment).collect(toList());

            int selected = 0;
            if (selected >= 0)
                list.setValueIndex(selected);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.list_and_details, container, false);
        }

        private boolean onSelectStrategy(String choice) {
            if (!listViewOptimized)
                optimizeListViewHeight();

            getFragmentManager().beginTransaction()
                    .replace(R.id.details_container, fragments.get(asList(names).indexOf(choice)))
                    .commit();
            return true;
        }

        private Fragment instantiateFragment(String fragmentClass) {
            Fragment fragment;
            try {
                fragment = (Fragment) Class.forName(fragmentClass).newInstance();
            } catch (java.lang.InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                throw new IllegalStateException(format(US, "Error instantiating StrategyEditFragment: [%s].", fragmentClass), e);
            }
            return fragment;
        }

        private void optimizeListViewHeight() {
            ListView listView = (ListView) getView().findViewById(android.R.id.list);

            int totalHeight = 0;
            ListAdapter adapter = listView.getAdapter();
            int childCount = adapter.getCount();
            for (int i = 0; i < childCount; i++) {
                View row = adapter.getView(i, null, listView);
                row.measure(makeMeasureSpec(0, UNSPECIFIED), makeMeasureSpec(0, UNSPECIFIED));
                totalHeight += row.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            totalHeight += (listView.getDividerHeight() * (childCount - 1));

            params.height = totalHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            listViewOptimized = true;
        }

    }
}