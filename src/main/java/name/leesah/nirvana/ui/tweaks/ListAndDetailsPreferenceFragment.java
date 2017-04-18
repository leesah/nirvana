package name.leesah.nirvana.ui.tweaks;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
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

public class ListAndDetailsPreferenceFragment<DetailsFragmentType> extends PreferenceFragment {

    private static final String TAG = ListAndDetailsPreferenceFragment.class.getSimpleName();
    private boolean listViewOptimized = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_and_details, container, false);
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

    protected DetailsFragmentType getCurrentFragment() {
        return (DetailsFragmentType) getFragmentManager().findFragmentById(R.id.details_container);
    }
}
