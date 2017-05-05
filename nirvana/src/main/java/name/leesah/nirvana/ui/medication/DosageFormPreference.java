package name.leesah.nirvana.ui.medication;

import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.DosageForm;

import static java.util.Arrays.asList;
import static name.leesah.nirvana.model.medication.DosageForm.*;
import static name.leesah.nirvana.model.medication.DosageForm.CAPSULE;
import static name.leesah.nirvana.model.medication.DosageForm.INJECTION;
import static name.leesah.nirvana.model.medication.DosageForm.TABLET;

/**
 * Created by sah on 2017-05-05.
 */

public class DosageFormPreference extends ListPreference {

    private DosageForm enumValues[] = new DosageForm[]{TABLET, CAPSULE, INJECTION};

    public DosageFormPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTitle(R.string.pref_title_dosage_form);
        setEntries(R.array.dosage_forms);
        setEntryValues(R.array.dosage_forms);
        setSummary("%s");
    }

    @Override
    protected boolean persistString(String value) {
        String name = enumValues[asList(getEntries()).indexOf(value)].name();
        return super.persistString(name);
    }

    @Override
    protected String getPersistedString(String defaultReturnValue) {
        String name = super.getPersistedString(defaultReturnValue);
        return name == null ?
                null :
                getEntries()[asList(enumValues).indexOf(valueOf(name))].toString();
    }
}
