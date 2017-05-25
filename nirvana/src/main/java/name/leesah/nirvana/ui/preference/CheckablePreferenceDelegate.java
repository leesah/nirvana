package name.leesah.nirvana.ui.preference;

import android.os.Bundle;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.firebase.analytics.FirebaseAnalytics;

import name.leesah.nirvana.R;

import static java.util.stream.IntStream.range;

public class CheckablePreferenceDelegate {
    private final Preference preference;
    private boolean checked;
    private View iconFrame;
    private View textFrame;
    private Switch switchWidget;
    private OnCheckedChangeListener onCheckedChangeListener;
    private OnTextFrameClickListener onTextFrameClickListener;
    private FirebaseAnalytics analytics;

    public CheckablePreferenceDelegate(Preference preference, AttributeSet attrs) {
        this.preference = preference;
        preference.setLayoutResource(R.layout.preference_unclickable);
        preference.setWidgetLayoutResource(R.layout.preference_widget_switch);
        preference.setShouldDisableView(false);
        analytics = FirebaseAnalytics.getInstance(preference.getContext());
    }

    public void onBindView(View view) {
        view.setClickable(false);

        iconFrame = view.findViewById(android.R.id.icon_frame);
        textFrame = view.findViewById(R.id.text_frame);
        textFrame.setOnClickListener(v -> onTextFrameClick());

        switchWidget = (Switch) view.findViewById(android.R.id.switch_widget);
        switchWidget.setClickable(true);

        switchWidget.setOnCheckedChangeListener(null);
        switchWidget.setChecked(checked);
        switchWidget.setOnCheckedChangeListener(this::onCheckedChange);

        setViewTreeEnabled(iconFrame, checked);
        setViewTreeEnabled(textFrame, checked);
    }

    public void onClick() {
    }

    private void onTextFrameClick() {
        analytics.logEvent("checkable_preference_click", null);

        onTextFrameClickListener.onTextFrameClick();
    }

    void onCheckedChange(CompoundButton buttonView, boolean checked) {
        Bundle params = new Bundle();
        params.putBoolean("checked", checked);
        analytics.logEvent("checkable_preference_toggle", params);

        if (!callChangeListener(checked)) {
            buttonView.setChecked(!checked);
            return;
        }

        this.checked = checked;
        setViewTreeEnabled(iconFrame, checked);
        setViewTreeEnabled(textFrame, checked);

        if (onCheckedChangeListener != null)
            onCheckedChangeListener.onCheckedChange(checked);
    }

    private boolean callChangeListener(Object newValue) {
        return preference.getOnPreferenceChangeListener() == null || preference.getOnPreferenceChangeListener().onPreferenceChange(preference, newValue);
    }

    private void setViewTreeEnabled(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup)
            range(0, ((ViewGroup) view).getChildCount())
                    .forEach(i -> setViewTreeEnabled(((ViewGroup) view).getChildAt(i), enabled));
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    public void setOnTextFrameClickListener(OnTextFrameClickListener onTextFrameClickListener) {
        this.onTextFrameClickListener = onTextFrameClickListener;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChange(boolean checked);
    }

    public interface OnTextFrameClickListener {
        void onTextFrameClick();
    }
}