package name.leesah.nirvana.ui.preference;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.preference.EditTextPreference;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import org.apache.commons.lang3.StringUtils;

import name.leesah.nirvana.R;

/**
 * Created by sah on 2016-12-15.
 */

public class ValidatedEditTextPreference extends EditTextPreference {

    private static final String ATTR_EMPTY_TEXT_ALLOWED = "allowBlank";
    private boolean blankAllowed = false;

    public boolean isBlankAllowed() {
        return blankAllowed;
    }

    public void setBlankAllowed(boolean blankAllowed) {
        this.blankAllowed = blankAllowed;
    }

    public ValidatedEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TimedDosageEditorCard,
                0, 0);
        try {
            blankAllowed = a.getBoolean(R.styleable.ValidatedEditTextPreference_allowBlank, false);
        } finally {
            a.recycle();
        }

        getEditText().addTextChangedListener(new ForbidBlank());
    }

    private void togglePositiveButtonEnabled(CharSequence charSequence) {
        AlertDialog alertDialog = (AlertDialog) getDialog();
        if (alertDialog != null)
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(blankAllowed || !StringUtils.isBlank(charSequence));
    }

    @Override
    protected void onClick() {
        super.onClick();
        togglePositiveButtonEnabled(getText());
    }

    private class ForbidBlank implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            togglePositiveButtonEnabled(s);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    @Override
    public CharSequence getSummary() {
        return getText() == null ? super.getSummary() : getText();
    }


}
