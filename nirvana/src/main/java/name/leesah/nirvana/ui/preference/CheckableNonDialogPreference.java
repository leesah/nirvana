package name.leesah.nirvana.ui.preference;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by sah on 2017-04-26.
 */

public class CheckableNonDialogPreference extends Preference {

    private final CheckablePreferenceDelegate delegate;

    public CheckableNonDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        delegate = new CheckablePreferenceDelegate(this, attrs);
        delegate.setOnTextFrameClickListener(() -> getOnPreferenceClickListener().onPreferenceClick(this));
    }

    @Override
    protected void onBindView(View view) {
        delegate.onBindView(view);
        super.onBindView(view);
    }

    @Override
    protected void onClick() {
        delegate.onClick();
    }

}
