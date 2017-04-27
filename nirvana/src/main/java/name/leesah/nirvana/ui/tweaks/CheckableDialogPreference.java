package name.leesah.nirvana.ui.tweaks;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import name.leesah.nirvana.R;

import static java.util.stream.IntStream.*;

/**
 * Created by sah on 2017-04-26.
 */

public class CheckableDialogPreference extends DialogPreference {

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

    public CheckableDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.preference_unclickable);
        setWidgetLayoutResource(R.layout.preference_widget_switch);
        setShouldDisableView(false);

        TypedArray a = context.getTheme().obtainStyledAttributes(
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

    @Override
    protected void onBindView(View view) {
        view.setClickable(false);

        iconFrame = view.findViewById(android.R.id.icon_frame);
        textFrame = view.findViewById(R.id.text_frame);
        textFrame.setOnClickListener(v -> showDialog(null));

        switchWidget = (Switch) view.findViewById(android.R.id.switch_widget);
        switchWidget.setClickable(true);
        switchWidget.setTextOn(switchTextOn);
        switchWidget.setTextOff(switchTextOff);

        switchWidget.setOnCheckedChangeListener(null);
        switchWidget.setChecked(checked);
        switchWidget.setOnCheckedChangeListener(this::onCheckedChange);
        setViewTreeEnabled(iconFrame, checked);
        setViewTreeEnabled(textFrame, checked);

        super.onBindView(view);
    }

    @Override
    protected void onClick() {
    }

    private void onCheckedChange(CompoundButton buttonView, boolean checked) {
        if (!callChangeListener(checked)) {
            // Listener didn't like it, change it back.
            // CompoundButton will make sure we don't recurse.
            buttonView.setChecked(!checked);
            return;
        }

        this.checked = checked;
        updateSummary();
        setViewTreeEnabled(iconFrame, checked);
        setViewTreeEnabled(textFrame, checked);

        onSwitchStateChange(checked);
    }

    private void updateSummary() {
        if (checked) {
            super.setSummary(savedSummary == null ? summaryOn : savedSummary);
        } else {
            savedSummary = getSummary();
            super.setSummary(summaryOff);
        }
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

    protected void onSwitchStateChange(boolean checked) {

    }

}
