package name.leesah.nirvana.ui.tweaks;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.stream.IntStream;

import name.leesah.nirvana.R;

import static java.util.stream.IntStream.*;

/**
 * Created by sah on 2017-04-26.
 */

public class CheckableDialogPreference extends DialogPreference {

    private boolean checked;
    private String switchTextOn;
    private String switchTextOff;
    private String summaryOff;
    private String valueOff;
    private boolean checkedByDefault;

    private View iconFrame;
    private View textFrame;
    private CharSequence savedSummary;
    private Switch switchWidget;
    private View title;
    private View summary;

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
            summaryOff = a.getString(R.styleable.CheckableDialogPreference_summaryOff);
            valueOff = a.getString(R.styleable.CheckableDialogPreference_valueOff);
            checkedByDefault = a.getBoolean(R.styleable.CheckableDialogPreference_checked, false);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        switchWidget.setOnCheckedChangeListener(null);
        switchWidget.setChecked(checked);
        switchWidget.setOnCheckedChangeListener(this::onCheckedChange);
        setChildViewsEnabled(checked);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        View rootView = super.onCreateView(parent);
        rootView.setClickable(false);

        iconFrame = rootView.findViewById(android.R.id.icon_frame);
        textFrame = rootView.findViewById(R.id.text_frame);
        switchWidget = (Switch) rootView.findViewById(android.R.id.switch_widget);

        textFrame.setOnClickListener(v -> showDialog(null));

        switchWidget.setClickable(true);

        switchWidget.setTextOn(switchTextOn);
        switchWidget.setTextOff(switchTextOff);
        switchWidget.setChecked(checkedByDefault);
        if (!switchWidget.isChecked())
            setSummary(summaryOff);

        return rootView;
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
        if (checked) {
            setSummary(savedSummary);
        } else {
            savedSummary = getSummary();
            setSummary(summaryOff);
        }
        setChildViewsEnabled(checked);

        onSwitchStateChange(checked);
    }

    private void setChildViewsEnabled(boolean enabled) {
        setViewTreeEnabled(iconFrame, enabled);
        setViewTreeEnabled(textFrame, enabled);
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

    public boolean isCheckedByDefault() {
        return checkedByDefault;
    }

    public void setCheckedByDefault(boolean checkedByDefault) {
        this.checkedByDefault = checkedByDefault;
    }

    protected void onSwitchStateChange(boolean checked) {

    }

}
