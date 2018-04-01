package name.leesah.nirvana.ui.settings.treatment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;

import org.joda.time.Period;

import name.leesah.nirvana.PhoneBook;
import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.medication.starting.ExactDate;
import name.leesah.nirvana.model.medication.starting.Immediately;
import name.leesah.nirvana.model.treatment.recurring.NTimes;
import name.leesah.nirvana.persistence.Pharmacist;
import name.leesah.nirvana.ui.preference.DatePreference;
import name.leesah.nirvana.ui.preference.PeriodPreference;

import static name.leesah.nirvana.DebugTools.isDeveloperModeOn;
import static name.leesah.nirvana.PhoneBook.nurse;
import static name.leesah.nirvana.PhoneBook.therapist;
import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static org.joda.time.Period.weeks;

/**
 * Created by sah on 2017-04-17.
 */

public class TreatmentSettingsFragment extends PreferenceFragment {

    public static final Period TWO_WEEKS = weeks(2);
    private DatePreference dayZero;
    private PeriodPreference length;
    private RecurringStrategyPreference recurringStrategy;
    private SwitchPreference treatmentEnabled;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefscr_settings_treatment);

        treatmentEnabled = (SwitchPreference) findPreference(getString(R.string.pref_key_treatment_enabled));
        dayZero = (DatePreference) findPreference(getString(R.string.pref_key_treatment_first_day));
        length = (PeriodPreference) findPreference(getString(R.string.pref_key_treatment_cycle_length));
        recurringStrategy = (RecurringStrategyPreference) findPreference(getString(R.string.pref_key_treatment_recurring));

        treatmentEnabled.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean enabling = (boolean) newValue;
            if (isDeveloperModeOn(getContext())) {
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.alert_toggle_treatment_title)
                        .setMessage(R.string.alert_toggle_treatment_message)
                        .setPositiveButton(android.R.string.ok, (d, w) -> toggleTreatmentSupport(enabling))
                        .setNegativeButton(android.R.string.cancel, (d, w) -> treatmentEnabled.setChecked(!enabling))
                        .create().show();
                return true;
            } else {
                if (enabling) {
                    loadDefaultTreatment();
                    therapist(getContext()).invalidate();
                }
                return enabling;
            }
        });
    }

    private void toggleTreatmentSupport(boolean enabling) {
        resetStartingStrategyForAllMedications(enabling);
        if (enabling)
            loadDefaultTreatment();
        else
            clearTreatment();
        therapist(getContext()).invalidate();
    }

    private void loadDefaultTreatment() {
        if (dayZero.getDate() == null)
            dayZero.setDate(today());

        if (length.getPeriod() == null)
            length.setPeriod(TWO_WEEKS);

        if (recurringStrategy.getStrategy() == null)
            recurringStrategy.setStrategy(new NTimes(1));
    }

    private void clearTreatment() {
        dayZero.setDate(null);
        length.setPeriod(null);
        recurringStrategy.setStrategy(null);
    }

    private void resetStartingStrategyForAllMedications(boolean relativeMode) {
        Pharmacist pharmacist = PhoneBook.pharmacist(getContext());
        pharmacist.getMedications()
                .forEach(medication -> {
                    Medication replacement = medication.with(relativeMode ? new Immediately() : new ExactDate(today()));
                    pharmacist.removeMedication(medication.getId());
                    pharmacist.save(replacement);
                    nurse(getContext()).rescheduleForTheRestOfToday(medication);
                });
    }

}
