package name.leesah.nirvana.ui.medication;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Set;
import java.util.function.Predicate;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.reminder.Reminder;

import static android.app.Activity.RESULT_OK;
import static name.leesah.nirvana.PhoneBook.alarmSecretary;
import static name.leesah.nirvana.PhoneBook.nurse;
import static name.leesah.nirvana.PhoneBook.pharmacist;
import static name.leesah.nirvana.PhoneBook.reminderMaker;
import static name.leesah.nirvana.model.reminder.Reminder.State.NOTIFIED;
import static name.leesah.nirvana.persistence.Nurse.isUpcoming;
import static name.leesah.nirvana.ui.medication.MedicationActivity.STAGING;
import static name.leesah.nirvana.ui.reminder.BellRinger.NOTIFICATION_TAG;
import static name.leesah.nirvana.utils.DateTimeHelper.today;

public class MedicationFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private MenuItem saveButton;
    private FirebaseAnalytics analytics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        analytics = FirebaseAnalytics.getInstance(getContext());

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
                analytics.logEvent("medication_save", null);
                saveMedication();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        Bundle params = new Bundle();
        params.putString("preference", preference.getKey());
        analytics.logEvent("preference_click", params);
        return false;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        refreshSaveButtonEnabled();
    }

    private void saveMedication() {

        Context c = getContext();
        Medication medication = Medication.Builder.buildFromStaged(c);
        pharmacist(c).save(medication);

        Set<Reminder> reminders = reminderMaker(c).createReminders(medication, today());
        Set<Reminder> deprecated = nurse(c).replace(
                isOf(medication).and(isUpcoming()),
                reminders);
        reminders.forEach(alarmSecretary(c)::setAlarm);
        deprecated.stream()
                .filter(reminder -> reminder.getState().equals(NOTIFIED))
                .forEach(reminder -> getContext().getSystemService(NotificationManager.class)
                        .cancel(NOTIFICATION_TAG, reminder.getNotificationId()));

        getActivity().setResult(RESULT_OK);
        getActivity().finish();
    }

    @NonNull
    private Predicate<Reminder> isOf(Medication medication) {
        return reminder -> reminder.getMedicationId() == medication.getId();
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
