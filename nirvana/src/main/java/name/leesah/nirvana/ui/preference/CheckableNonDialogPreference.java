package name.leesah.nirvana.ui.preference;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by sah on 2017-04-26.
 */

public class CheckableNonDialogPreference extends Preference implements CheckablePreference {

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

    @Override
    public boolean isChecked() {
        return delegate.isChecked();
    }

    @Override
    public void setChecked(boolean checked) {
        delegate.setChecked(checked);
    }

    @Override
    public String getSwitchTextOn() {
        return delegate.getSwitchTextOn();
    }

    @Override
    public void setSwitchTextOn(String switchTextOn) {
        delegate.setSwitchTextOn(switchTextOn);
    }

    @Override
    public String getSwitchTextOff() {
        return delegate.getSwitchTextOff();
    }

    @Override
    public void setSwitchTextOff(String switchTextOff) {
        delegate.setSwitchTextOff(switchTextOff);
    }

    @Override
    public String getSummaryOff() {
        return delegate.getSummaryOff();
    }

    @Override
    public void setSummaryOff(String summaryOff) {
        delegate.setSummaryOff(summaryOff);
    }

    @Override
    public String getValueOff() {
        return delegate.getValueOff();
    }

    @Override
    public void setValueOff(String valueOff) {
        delegate.setValueOff(valueOff);
    }

    @Override
    public Object getValue() {
        return null;
    }


}
