package name.leesah.nirvana.ui.medication;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.DosageForm;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.medication.MedicationBuilder;
import name.leesah.nirvana.ui.tweaks.PeriodPreference;

import static name.leesah.nirvana.utils.DateTimeHelper.toPeriod;
import static name.leesah.nirvana.utils.DateTimeHelper.toText;

public class BasicsFragment extends PreferenceFragment {

    public static final String KEY_NAME = "name.leesah.nirvana:key:NAME";
    private static final String KEY_MANUFACTURER = "name.leesah.nirvana:key:MANUFACTURER";
    private static final String KEY_DOSAGE_FORM = "name.leesah.nirvana:key:FORM";
    private static final String KEY_DELAYED = "name.leesah.nirvana:key:DELAYED";
    private static final String KEY_DELAYED_PERIOD = "name.leesah.nirvana:key:DELAYED_PERIOD";
    private Preference.OnPreferenceClickListener remindingModelListener;
    private Preference.OnPreferenceClickListener repeatingModelListener;

    private EditTextPreference name;
    private EditTextPreference manufacturer;
    private ListPreference dosageForm;
    private SwitchPreference delayed;
    private PeriodPreference delayedPeriod;
    private Preference remindingModel;
    private Preference repeatingModel;
    private MedicationActivity.ValidityReportListener validityReportListener;
    private String remindingModelSummary;
    private String repeatingModelSummary;
    private Medication editingExisting;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefscr_medication);

        name = (EditTextPreference) findPreference(getString(R.string.pref_key_medication_name));
        manufacturer = (EditTextPreference) findPreference(getString(R.string.pref_key_medication_manufacturer));
        dosageForm = (ListPreference) findPreference(getString(R.string.pref_key_medication_dosage_form));
        delayed = (SwitchPreference) findPreference(getString(R.string.pref_key_medication_starting_delay_enabled));
        delayedPeriod = (PeriodPreference) findPreference(getString(R.string.pref_key_medication_starting_delay_period));
        remindingModel = findPreference(getString(R.string.pref_key_medication_reminding));
        repeatingModel = findPreference(getString(R.string.pref_key_medication_repeating));

        name.setOnPreferenceChangeListener((p, v) -> reportValidity());
        dosageForm.setOnPreferenceChangeListener((p, v) -> reportValidity());
        delayedPeriod.setOnPreferenceChangeListener((p, v) -> reportValidity());

        remindingModel.setOnPreferenceClickListener(remindingModelListener);
        remindingModel.setSummary(remindingModelSummary);
        repeatingModel.setOnPreferenceClickListener(repeatingModelListener);
        repeatingModel.setSummary(repeatingModelSummary);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            name.setText(savedInstanceState.getString(KEY_NAME));
            manufacturer.setText(savedInstanceState.getString(KEY_MANUFACTURER));
            dosageForm.setValue(savedInstanceState.getString(KEY_DOSAGE_FORM));
            delayed.setChecked(savedInstanceState.getBoolean(KEY_DELAYED));
            if (savedInstanceState.containsKey(KEY_DELAYED_PERIOD))
                delayedPeriod.setPeriod(toPeriod(savedInstanceState.getString(KEY_DELAYED_PERIOD)));
        } else if (editingExisting != null) {
            name.setText(editingExisting.getName());
            manufacturer.setText(editingExisting.getManufacturer());
            dosageForm.setValue(editingExisting.getForm().getName(getContext()));
            delayed.setChecked(editingExisting.isDelayed());
            if (editingExisting.getDelayedPeriod() != null)
                delayedPeriod.setPeriod(editingExisting.getDelayedPeriod());
            remindingModel.setSummary(editingExisting.getRemindingStrategy().toString(getContext()));
            repeatingModel.setSummary(editingExisting.getRepeatingStrategy().toString(getContext()));
            editingExisting = null;
        }
        reportValidity();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_NAME, name.getText());
        outState.putString(KEY_MANUFACTURER, manufacturer.getText());
        outState.putString(KEY_DOSAGE_FORM, dosageForm.getValue());
        outState.putBoolean(KEY_DELAYED, delayed.isChecked());
        outState.putString(KEY_DELAYED_PERIOD, toText(delayedPeriod.getPeriod()));
    }

    public void setValidityReportListener(MedicationActivity.ValidityReportListener listener) {
        this.validityReportListener = listener;
    }

    private boolean reportValidity() {
        validityReportListener.onValidityReport(
                !TextUtils.isEmpty(name.getText()) &&
                        !TextUtils.isEmpty(dosageForm.getValue()) &&
                        (!delayed.isChecked() || delayedPeriod.getPeriod() != null));
        return true;
    }

    Medication readMedication() {
        return new MedicationBuilder()
                .setName(name.getText())
                .setManufacturer(manufacturer.getText())
                .setForm(DosageForm.withName(getContext(), dosageForm.getValue()))
                .setDelayed(delayed.isChecked())
                .setDelayedBy(delayedPeriod.getPeriod())
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

    public void setEditingExisting(Medication medication) {
        this.editingExisting = medication;
    }
}
