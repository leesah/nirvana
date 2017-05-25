package name.leesah.nirvana.ui.medication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.support.annotation.ArrayRes;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import name.leesah.nirvana.R;

import static com.google.firebase.analytics.FirebaseAnalytics.Param.ITEM_ID;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Locale.US;
import static java.util.stream.Collectors.toList;
import static name.leesah.nirvana.ui.medication.StrategySelectActivity.ListAndDetailsFragment.EXTRA_ENTRIES;
import static name.leesah.nirvana.ui.medication.StrategySelectActivity.ListAndDetailsFragment.EXTRA_FRAGMENTS;
import static name.leesah.nirvana.ui.medication.StrategySelectActivity.ListAndDetailsFragment.EXTRA_SELECTED;
import static name.leesah.nirvana.ui.medication.StrategySelectActivity.ListAndDetailsFragment.EXTRA_TITLE;
import static name.leesah.nirvana.utils.ListViewHeightOptimizer.optimize;

/**
 * Created by sah on 2017-04-17.
 */
public class StrategySelectActivity extends AppCompatActivity {

    private static final String EXTRA_FRAGMENT_ARGS = "name.leesah.nirvana:extra:STRATEGY_FRAGMENT_ARGS";

    public static void start(Context context, @StringRes int title, @ArrayRes int names, Class[] fragments, @IntRange(from = 0) int selected) {
        String[] entries = context.getResources().getStringArray(names);
        validateArraySizesEqual(entries, fragments);
        stream(fragments).forEach(StrategySelectActivity::validateClassName);
        validateSelectedIndex(selected, entries);

        final Bundle args = new Bundle(4);
        args.putString(EXTRA_TITLE, context.getString(title));
        args.putStringArray(EXTRA_ENTRIES, entries);
        args.putStringArrayList(EXTRA_FRAGMENTS,
                new ArrayList<>(stream(fragments).map(Class::getCanonicalName).collect(toList())));
        args.putInt(EXTRA_SELECTED, selected);

        context.startActivity(
                new Intent(context, StrategySelectActivity.class)
                        .putExtra(EXTRA_FRAGMENT_ARGS, args));
    }

    private static void validateSelectedIndex(@IntRange(from = 0) int selected, String[] entries) {
        if (selected >= entries.length)
            throw new ArrayIndexOutOfBoundsException(selected);
    }

    private static void validateArraySizesEqual(String[] names, Class[] fragments) {
        if (names.length != fragments.length)
            throw new IllegalArgumentException(format(US, "Number of names [%d] and fragments [%d] don't match.",
                    names.length, fragments.length));
    }

    private static void validateClassName(Class fragment) {
        if (!StrategyEditFragment.class.isAssignableFrom(fragment))
            throw new IllegalArgumentException(format(US, "Not subclass of %s: [%s].",
                    StrategyEditFragment.class.getSimpleName(), fragment));
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
        static final String EXTRA_ENTRIES = "name.leesah.nirvana:extra:STRATEGY_NAMES";
        static final String EXTRA_FRAGMENTS = "name.leesah.nirvana:extra:STRATEGY_FRAGMENTS";
        static final String EXTRA_SELECTED = "name.leesah.nirvana:extra:SELECTED_NAME";

        private boolean listViewOptimized = false;
        private String[] entries;
        private List<StrategyEditFragment> fragments;

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.strategy_selecting, container, false);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefscr_medication_strategy_select);
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            ListPreference listPreference = (ListPreference) findPreference(getString(R.string.pref_key_strategies));
            listPreference.setOnPreferenceChangeListener((p, v) -> onSelectStrategy(v.toString()));

            Bundle arguments = getArguments();
            listPreference.setTitle(arguments.getString(EXTRA_TITLE));

            entries = arguments.getStringArray(EXTRA_ENTRIES);
            listPreference.setEntries(entries);
            listPreference.setEntryValues(entries);

            fragments = arguments.getStringArrayList(EXTRA_FRAGMENTS).stream()
                    .map(this::instantiateFragment).collect(toList());

            int selected = arguments.getInt(EXTRA_SELECTED);
            if (selected >= 0) {
                listPreference.setValue(entries[selected]);
                onSelectStrategy(entries[selected]);
            }
        }

        private boolean onSelectStrategy(String choice) {
            Bundle params = new Bundle();
            params.putCharSequence(ITEM_ID, choice);
            FirebaseAnalytics.getInstance(getContext()).logEvent("SELECT_STRATEGY", params);

            if (!listViewOptimized)
                optimize((ListView) getView().findViewById(android.R.id.list));
            listViewOptimized = true;

            getFragmentManager().beginTransaction()
                    .replace(R.id.details_container, fragments.get(asList(entries).indexOf(choice)))
                    .commit();
            return true;
        }

        private StrategyEditFragment instantiateFragment(String fragmentClass) {
            StrategyEditFragment fragment;
            try {
                fragment = (StrategyEditFragment) Class.forName(fragmentClass).newInstance();
            } catch (java.lang.InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                throw new IllegalStateException(format(US, "Error instantiating StrategyEditFragment: [%s].", fragmentClass), e);
            }
            return fragment;
        }

    }
}