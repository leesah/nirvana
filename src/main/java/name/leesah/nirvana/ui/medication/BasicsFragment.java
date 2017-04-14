package name.leesah.nirvana.ui.medication;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.text.TextUtils;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.DosageForm;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.medication.MedicationBuilder;
import name.leesah.nirvana.ui.components.PeriodPreference;

public class BasicsFragment extends PreferenceFragment {

    private Preference.OnPreferenceClickListener remindingModelListener;
    private Preference.OnPreferenceClickListener repeatingModelListener;

    private EditTextPreference name;
    private EditTextPreference manufacturer;
    private ListPreference dosageForm;
    private SwitchPreference delayed;
    private PeriodPreference delayedBy;
    private Preference remindingModel;
    private Preference repeatingModel;
    private MedicationEditActivity.ValidityReportListener validityReportListener;
    private String remindingModelSummary;
    private String repeatingModelSummary;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_medication);

        name = (EditTextPreference) findPreference(getString(R.string.pref_key_medication_name));
        manufacturer = (EditTextPreference) findPreference(getString(R.string.pref_key_medication_manufacturer));
        dosageForm = (ListPreference) findPreference(getString(R.string.pref_key_medication_dosage_form));
        delayed = (SwitchPreference) findPreference(getString(R.string.pref_key_enable_skipped_period));
        delayedBy = (PeriodPreference) findPreference(getString(R.string.pref_key_medication_skipped_period));
        remindingModel = findPreference(getString(R.string.pref_key_medication_reminding_model));
        repeatingModel = findPreference(getString(R.string.pref_key_medication_repeating_model));

        name.setOnPreferenceChangeListener((p, v) -> onMandatoryFieldChange());
        dosageForm.setOnPreferenceChangeListener((p, v) -> onMandatoryFieldChange());
        delayedBy.setOnPreferenceChangeListener((p, v) -> onMandatoryFieldChange());

        remindingModel.setOnPreferenceClickListener(remindingModelListener);
        remindingModel.setSummary(remindingModelSummary);
        repeatingModel.setOnPreferenceClickListener(repeatingModelListener);
        repeatingModel.setSummary(repeatingModelSummary);
    }

    public void setValidityReportListener(MedicationEditActivity.ValidityReportListener listener) {
        this.validityReportListener = listener;
    }

    private boolean onMandatoryFieldChange() {
        validityReportListener.onValidityReport(
                !TextUtils.isEmpty(name.getText()) &&
                !TextUtils.isEmpty(dosageForm.getValue()) &&
                (!delayed.isChecked() || delayedBy.getPeriod() != null));
        return true;
    }

    Medication readMedication() {
        return new MedicationBuilder()
                .setName(name.getText())
                .setManufacturer(manufacturer.getText())
                .setForm(DosageForm.valueOf(dosageForm.getValue()))
                .setDelayed(delayed.isChecked())
                .setDelayedBy(delayedBy.getPeriod())
                .build();
    }

    public void setRemindingModelListener(Preference.OnPreferenceClickListener listener) {
        this.remindingModelListener = listener;
        if (remindingModel != null)
            remindingModel.setOnPreferenceClickListener(listener);
    }

    public void setRepeatingModelListener(Preference.OnPreferenceClickListener listener) {
        this.repeatingModelListener = listener;
        if (repeatingModel != null)
            repeatingModel.setOnPreferenceClickListener(listener);
    }

    public void setRemindingModelSummary(String summary) {
        this.remindingModelSummary = summary;
        if (remindingModel != null)
            remindingModel.setSummary(summary);
    }

    public void setRepeatingModelSummary(String summary) {
        this.repeatingModelSummary = summary;
        if (repeatingModel != null)
            repeatingModel.setSummary(summary);
    }
}
