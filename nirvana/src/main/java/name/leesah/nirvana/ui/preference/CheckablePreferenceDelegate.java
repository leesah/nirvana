package name.leesah.nirvana.ui.preference;

import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.stream.IntStream;

import name.leesah.nirvana.R;

public class CheckablePreferenceDelegate {
    private final Preference preference;
    private boolean checked;
    private String switchTextOn;
    private String switchTextOff;
    private String summaryOn;
    private String summaryOff;
    private String valueOff;
    private View iconFrame;
    private View textFrame;
    private CharSequence savedSummary;
    private Switch switchWidget;
    private OnCheckedChangeListener onCheckedChangeListener;
    private OnTextFrameClickListener onTextFrameClickListener;

    public CheckablePreferenceDelegate(Preference preference, AttributeSet attrs) {
        this.preference = preference;
        preference.setLayoutResource(R.layout.preference_unclickable);
        preference.setWidgetLayoutResource(R.layout.preference_widget_switch);
        preference.setShouldDisableView(false);

        TypedArray a = preference.getContext().getTheme().obtainStyledAttributes(
                attrs, R.styleable.CheckableDialogPreference,
                0, 0);
        try {
            switchTextOn = a.getString(R.styleable.CheckableDialogPreference_switchTextOn);
            switchTextOff = a.getString(R.styleable.CheckableDialogPreference_switchTextOff);
            summaryOn = a.getString(R.styleable.CheckableDialogPreference_summaryOn);
            summaryOff = a.getString(R.styleable.CheckableDialogPreference_summaryOff);
            valueOff = a.getString(R.styleable.CheckableDialogPreference_valueOff);
            checked = a.getBoolean(R.styleable.CheckableDialogPreference_checked, false);
        } finally {
            a.recycle();
        }

        updateSummary();
    }

    public void onBindView(View view) {
        view.setClickable(false);

        iconFrame = view.findViewById(android.R.id.icon_frame);
        textFrame = view.findViewById(R.id.text_frame);
        textFrame.setOnClickListener(v -> onTextFrameClickListener.onTextFrameClick());

        switchWidget = (Switch) view.findViewById(android.R.id.switch_widget);
        switchWidget.setClickable(true);
        switchWidget.setTextOn(switchTextOn);
        switchWidget.setTextOff(switchTextOff);

        switchWidget.setOnCheckedChangeListener(null);
        switchWidget.setChecked(checked);
        switchWidget.setOnCheckedChangeListener(this::onCheckedChange);

        setViewTreeEnabled(iconFrame, checked);
        setViewTreeEnabled(textFrame, checked);
    }

    public void onClick() {
    }

    void onCheckedChange(CompoundButton buttonView, boolean checked) {
        this.checked = checked;
        updateSummary();
        setViewTreeEnabled(iconFrame, checked);
        setViewTreeEnabled(textFrame, checked);

        if (onCheckedChangeListener != null)
            onCheckedChangeListener.onCheckedChange(checked);
    }

    private boolean callChangeListener(Object newValue) {
        return preference.getOnPreferenceChangeListener() == null || preference.getOnPreferenceChangeListener().onPreferenceChange(preference, newValue);
    }

    private void updateSummary() {
        if (checked) {
            preference.setSummary(savedSummary == null ? summaryOn : savedSummary);
        } else {
            savedSummary = preference.getSummary();
            preference.setSummary(summaryOff);
        }
    }

    private void setViewTreeEnabled(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup)
            IntStream.range(0, ((ViewGroup) view).getChildCount())
                    .forEach(i -> setViewTreeEnabled(((ViewGroup) view).getChildAt(i), enabled));
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getSwitchTextOn() {
        return switchTextOn;
    }

    public void setSwitchTextOn(String switchTextOn) {
        this.switchTextOn = switchTextOn;
    }

    public String getSwitchTextOff() {
        return switchTextOff;
    }

    public void setSwitchTextOff(String switchTextOff) {
        this.switchTextOff = switchTextOff;
    }

    public String getSummaryOff() {
        return summaryOff;
    }

    public void setSummaryOff(String summaryOff) {
        this.summaryOff = summaryOff;
    }

    public String getValueOff() {
        return valueOff;
    }

    public void setValueOff(String valueOff) {
        this.valueOff = valueOff;
    }

    public OnCheckedChangeListener getOnCheckedChangeListener() {
        return onCheckedChangeListener;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    public OnTextFrameClickListener getOnTextFrameClickListener() {
        return onTextFrameClickListener;
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