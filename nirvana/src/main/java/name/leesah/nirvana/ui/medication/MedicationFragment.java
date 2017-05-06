package name.leesah.nirvana.ui.medication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Set;

import name.leesah.nirvana.R;
import name.leesah.nirvana.data.Nurse;
import name.leesah.nirvana.data.Pharmacist;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.reminder.Reminder;
import name.leesah.nirvana.model.reminder.ReminderFactory;
import name.leesah.nirvana.ui.reminder.AlarmSecretary;
import name.leesah.nirvana.ui.reminder.NotificationSecretary;

import static android.app.Activity.RESULT_OK;
import static name.leesah.nirvana.model.reminder.Reminder.State.NOTIFIED;
import static name.leesah.nirvana.ui.medication.MedicationActivity.STAGING;
import static name.leesah.nirvana.utils.DateTimeHelper.today;

public class MedicationFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private MenuItem saveButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        getPreferenceManager().setSharedPreferencesName(STAGING);
        getPreferenceManager().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

        addPreferencesFromResource(R.xml.prefscr_medication);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        saveButton = menu.findItem(R.id.save_button);
        refreshSaveButtonEnabled();
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_button:
                saveMedication();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        refreshSaveButtonEnabled();
    }

    private void saveMedication() {
        Pharmacist pharmacist = Pharmacist.getInstance(getContext());
        Nurse nurse = Nurse.getInstance(getContext());
        AlarmSecretary alarmSecretary = AlarmSecretary.getInstance(getContext());
        NotificationSecretary notificationSecretary = NotificationSecretary.getInstance(getContext());

        Medication medication = new Medication.Builder().buildFromStaged(getContext());
        pharmacist.save(medication);

        Set<Reminder> reminders = new ReminderFactory(getContext()).createReminders(medication, today());
        Set<Reminder> deprecated = nurse.replace(
                reminder -> reminder.getMedicationId() == medication.getId() &&
                        reminder.getDate().equals(today()),
                reminders);
        reminders.forEach(alarmSecretary::setAlarm);
        deprecated.stream()
                .filter(reminder -> reminder.getState().equals(NOTIFIED))
                .forEach(reminder -> notificationSecretary.dismiss(reminder.getNotificationId()));

        getActivity().setResult(RESULT_OK);
        getActivity().finish();
    }

    private void refreshSaveButtonEnabled() {
        if (!isAdded() || saveButton == null)
            return;

        SharedPreferences preferences = getPreferenceManager().getSharedPreferences();
        boolean name = preferences.contains(getString(R.string.pref_key_medication_name));
        boolean form = preferences.contains(getString(R.string.pref_key_medication_dosage_form));
        boolean reminding = preferences.contains(getString(R.string.pref_key_medication_reminding));
        boolean repeating = preferences.contains(getString(R.string.pref_key_medication_repeating));
        boolean starting = preferences.contains(getString(R.string.pref_key_medication_starting));
        boolean stopping = preferences.contains(getString(R.string.pref_key_medication_stopping));

        saveButton.setEnabled(name && form && reminding && repeating && starting && stopping);
    }

}
