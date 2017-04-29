package name.leesah.nirvana.ui.medication;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import name.leesah.nirvana.R;
import name.leesah.nirvana.data.Therapist;
import name.leesah.nirvana.model.medication.DosageForm;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.medication.MedicationBuilder;
import name.leesah.nirvana.model.medication.repeating.Everyday;
import name.leesah.nirvana.model.medication.starting.Delayed;
import name.leesah.nirvana.model.medication.starting.ExactDate;
import name.leesah.nirvana.model.medication.starting.Immediately;
import name.leesah.nirvana.model.medication.starting.StartingStrategy;
import name.leesah.nirvana.model.medication.stopping.InPeriod;
import name.leesah.nirvana.model.medication.stopping.Never;
import name.leesah.nirvana.ui.preference.CheckableDatePreference;
import name.leesah.nirvana.ui.preference.CheckableNonDialogPreference;
import name.leesah.nirvana.ui.preference.CheckablePeriodPreference;
import name.leesah.nirvana.ui.preference.CheckablePreference;

import static android.text.TextUtils.*;
import static java.lang.String.format;
import static name.leesah.nirvana.utils.DateTimeHelper.toDate;
import static name.leesah.nirvana.utils.DateTimeHelper.toPeriod;
import static name.leesah.nirvana.utils.DateTimeHelper.toText;

public class BasicsFragment extends PreferenceFragment {

    public static final String KEY_NAME = "name.leesah.nirvana:key:NAME";
    private static final String KEY_MANUFACTURER = "name.leesah.nirvana:key:MANUFACTURER";
    private static final String KEY_DOSAGE_FORM = "name.leesah.nirvana:key:FORM";
    private static final String KEY_STARTING = "name.leesah.nirvana:key:STARTING";
    private static final String KEY_STOPPING = "name.leesah.nirvana:key:STOPPING";
    private Preference.OnPreferenceClickListener remindingModelListener;
    private Preference.OnPreferenceClickListener repeatingModelListener;

    private EditTextPreference name;
    private EditTextPreference manufacturer;
    private ListPreference dosageForm;
    private Preference reminding;
    private CheckableNonDialogPreference repeating;
    private CheckablePreference starting;
    private CheckablePeriodPreference stopping;
    private MedicationActivity.ValidityReportListener validityReportListener;
    private String remindingModelSummary;
    private String repeatingModelSummary;
    private Medication editingExisting;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefscr_medication);
        if (isTreatmentSupportEnabled())
            addPreferencesFromResource(R.xml.prefscr_medication_starting_relative);
        else
            addPreferencesFromResource(R.xml.prefscr_medication_starting_exact_date);
        addPreferencesFromResource(R.xml.prefscr_medication_stopping);

        name = (EditTextPreference) findPreference(getString(R.string.pref_key_medication_name));
        manufacturer = (EditTextPreference) findPreference(getString(R.string.pref_key_medication_manufacturer));
        dosageForm = (ListPreference) findPreference(getString(R.string.pref_key_medication_dosage_form));
        reminding = findPreference(getString(R.string.pref_key_medication_reminding));
        repeating = (CheckableNonDialogPreference) findPreference(getString(R.string.pref_key_medication_repeating));
        starting = (CheckablePreference) findPreference(getString(R.string.pref_key_medication_starting));
        stopping = (CheckablePeriodPreference) findPreference(getString(R.string.pref_key_medication_stopping_after_period));

        name.setOnPreferenceChangeListener((p, v) -> reportValidity());
        dosageForm.setOnPreferenceChangeListener((p, v) -> reportValidity());

        reminding.setSummary(remindingModelSummary);
        reminding.setOnPreferenceClickListener(remindingModelListener);
        repeating.setOnPreferenceClickListener(repeatingModelListener);
        starting.setOnPreferenceChangeListener((p, v) -> reportValidity());
        stopping.setOnPreferenceChangeListener((p, v) -> reportValidity());

    }

    private boolean isTreatmentSupportEnabled() {
        return Therapist.getInstance(getContext()).isCycleSupportEnabled();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            name.setText(savedInstanceState.getString(KEY_NAME));
            manufacturer.setText(savedInstanceState.getString(KEY_MANUFACTURER));
            dosageForm.setValue(savedInstanceState.getString(KEY_DOSAGE_FORM));

            boolean hasStarting = savedInstanceState.containsKey(KEY_STARTING);
            starting.setChecked(hasStarting);
            if (hasStarting && starting instanceof CheckablePeriodPreference)
                ((CheckablePeriodPreference) starting).setValue(toPeriod(savedInstanceState.getString(KEY_STARTING)));
            else if (hasStarting && starting instanceof CheckableDatePreference)
                ((CheckableDatePreference) starting).setValue(toDate(savedInstanceState.getString(KEY_STARTING)));

            boolean hasStopping = savedInstanceState.containsKey(KEY_STOPPING);
            stopping.setChecked(hasStopping);
            if (hasStopping)
                stopping.setValue(toPeriod(savedInstanceState.getString(KEY_STOPPING)));

        } else if (editingExisting != null) {
            name.setText(editingExisting.getName());
            manufacturer.setText(editingExisting.getManufacturer());
            dosageForm.setValue(editingExisting.getForm().getName(getContext()));
            reminding.setSummary(editingExisting.getRemindingStrategy().toString(getContext()));

            boolean customRepeating = !(editingExisting.getRepeatingStrategy() instanceof Everyday);
            repeating.setChecked(customRepeating);
            if (customRepeating)
                repeating.setSummary(editingExisting.getRepeatingStrategy().toString(getContext()));

            boolean customStarting = editingExisting.getStartingStrategy() instanceof Delayed;
            starting.setChecked(customStarting);
            if (customStarting && starting instanceof CheckablePeriodPreference)
                ((CheckablePeriodPreference) starting).setValue(((Delayed) editingExisting.getStartingStrategy()).getPeriod());
            else if (customStarting && starting instanceof CheckableDatePreference)
                ((CheckableDatePreference) starting).setValue(((ExactDate) editingExisting.getStartingStrategy()).getStartDate());

            boolean customStopping = editingExisting.getStoppingStrategy() instanceof InPeriod;
            if (customStopping)
                stopping.setValue(((InPeriod) editingExisting.getStoppingStrategy()).getPeriod());

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
        if (starting.isChecked() && starting instanceof CheckablePeriodPreference)
            outState.putString(KEY_STARTING, toText(((CheckablePeriodPreference) starting).getValue()));
        else if (starting.isChecked() && starting instanceof CheckableDatePreference)
            outState.putString(KEY_STARTING, toText(((CheckableDatePreference) starting).getValue()));

        if (stopping.isChecked())
            outState.putString(KEY_STOPPING, toText(stopping.getValue()));
    }

    public void setValidityReportListener(MedicationActivity.ValidityReportListener listener) {
        this.validityReportListener = listener;
    }

    private boolean reportValidity() {
        validityReportListener.onValidityReport(isValid());
        return true;
    }

    private boolean isValid() {
        return !isEmpty(name.getText()) && !isEmpty(dosageForm.getValue())
                && (!starting.isChecked() || starting.getValue() != null)
                && (!stopping.isChecked() || stopping.getValue() != null);
    }

    Medication readMedication() {
        if (!isValid())
            throw new IllegalStateException("Premature invocation on readMedication().");

        return new MedicationBuilder()
                .setName(name.getText())
                .setManufacturer(manufacturer.getText())
                .setForm(DosageForm.withName(getContext(), dosageForm.getValue()))
                .setStartingStrategy(readStartingStrategy())
                .setStoppingStrategy(stopping.isChecked() ? new InPeriod(stopping.getValue()) : new Never())
                .build();
    }

    @NonNull
    private StartingStrategy readStartingStrategy() {
        if (!starting.isChecked())
            return new Immediately();
        else if (starting instanceof CheckablePeriodPreference)
            return new Delayed(((CheckablePeriodPreference)starting).getValue());
        else if (starting instanceof CheckableDatePreference)
            return new ExactDate(((CheckableDatePreference)starting).getValue());
        else
            throw new IllegalStateException(format("Unexpected starting strategy: [%s].", starting.getClass().getSimpleName()));
    }

    public void setRemindingModelListener(Preference.OnPreferenceClickListener listener) {
        this.remindingModelListener = listener;
        if (reminding != null)
            reminding.setOnPreferenceClickListener(listener);
    }

    public void setRepeatingModelListener(Preference.OnPreferenceClickListener listener) {
        this.repeatingModelListener = listener;
        if (repeating != null)
            repeating.setOnPreferenceClickListener(listener);
    }

    public void setRemindingModelSummary(String summary) {
        this.remindingModelSummary = summary;
        if (reminding != null)
            reminding.setSummary(summary);
    }

    public void setRepeatingModelSummary(String summary) {
        this.repeatingModelSummary = summary;
        if (repeating != null)
            repeating.setSummary(summary);
    }

    public void setEditingExisting(Medication medication) {
        this.editingExisting = medication;
    }
}
