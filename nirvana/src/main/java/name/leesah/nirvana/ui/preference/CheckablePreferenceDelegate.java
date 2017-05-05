package name.leesah.nirvana.ui.preference;

import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

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

    public CheckablePreferenceDelegate(Preference preference, AttributeSet attrs) {
        this.preference = preference;
        preference.setLayoutResource(R.layout.preference_unclickable);
        preference.setWidgetLayoutResource(R.layout.preference_widget_switch);
        preference.setShouldDisableView(false);

    }

    public void onBindView(View view) {
        view.setClickable(false);

        iconFrame = view.findViewById(android.R.id.icon_frame);
        textFrame = view.findViewById(R.id.text_frame);
        textFrame.setOnClickListener(v -> onTextFrameClickListener.onTextFrameClick());

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

    void onCheckedChange(CompoundButton buttonView, boolean checked) {
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