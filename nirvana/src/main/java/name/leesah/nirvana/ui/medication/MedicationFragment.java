package name.leesah.nirvana.ui.medication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseAdapter;

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
import static android.content.Context.MODE_PRIVATE;
import static name.leesah.nirvana.model.reminder.Reminder.State.NOTIFIED;
import static name.leesah.nirvana.ui.medication.MedicationActivity.STAGING;
import static name.leesah.nirvana.utils.DateTimeHelper.today;

public class MedicationFragment extends PreferenceFragment {

    private final SharedPreferences.OnSharedPreferenceChangeListener refreshSavedButtonEnabled = (p, k) -> refreshSaveButtonEnabled();

    private boolean saveButtonEnabled;
    private MenuItem saveButton;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(STAGING);
        sharedPreferences = getPreferenceManager().getSharedPreferences();
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(refreshSavedButtonEnabled);

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(refreshSavedButtonEnabled);
        super.onPause();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        saveButton = menu.findItem(R.id.save_button);
        saveButton.setEnabled(saveButtonEnabled);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            ((BaseAdapter) getPreferenceScreen().getRootAdapter()).notifyDataSetChanged();
        }
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
        SharedPreferences preferences = getContext().getSharedPreferences(STAGING, MODE_PRIVATE);
        setSaveButtonEnabled(preferences.contains(getString(R.string.pref_key_medication_name)) &&
                preferences.contains(getString(R.string.pref_key_medication_dosage_form)) &&
                preferences.contains(getString(R.string.pref_key_medication_reminding)) &&
                preferences.contains(getString(R.string.pref_key_medication_repeating)) &&
                preferences.contains(getString(R.string.pref_key_medication_starting)) &&
                preferences.contains(getString(R.string.pref_key_medication_stopping))
        );
    }

    private void setSaveButtonEnabled(boolean enabled) {
        saveButtonEnabled = enabled;
        if (saveButton != null)
            saveButton.setEnabled(saveButtonEnabled);
    }

}
