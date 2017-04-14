package name.leesah.nirvana.ui.components;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.EditTextPreference;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import org.apache.commons.lang3.StringUtils;

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

    public ValidatedEditTextPreference(Context context) {
        this(context, null);
    }

    public ValidatedEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (attrs != null) {
            blankAllowed = attrs.getAttributeBooleanValue(Constants.ATTRS_NAMESPACE, ATTR_EMPTY_TEXT_ALLOWED, false);
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
