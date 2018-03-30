package name.leesah.nirvana.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import static android.view.View.MeasureSpec.UNSPECIFIED;
import static android.view.View.MeasureSpec.makeMeasureSpec;

/**
 * Created by sah on 2017-05-06.
 */

public class ListViewHeightOptimizer {
    public static void optimize(ListView listView) {
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
    }
}
