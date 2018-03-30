package name.leesah.nirvana.ui.preference;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by sah on 2017-04-26.
 */

public class CheckableDialogPreference extends DialogPreference {

    private final CheckablePreferenceDelegate delegate;

    public CheckableDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        delegate = new CheckablePreferenceDelegate(this, attrs);
        delegate.setOnTextFrameClickListener(() -> super.showDialog(null));
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

    public boolean isChecked() {
        return delegate.isChecked();
    }

    public void setChecked(boolean checked) {
        delegate.setChecked(checked);
    }

}
