package name.leesah.nirvana.ui.medication;


import android.app.Fragment;
import android.os.Bundle;
import android.preference.Preference;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.Set;

import name.leesah.nirvana.R;
import name.leesah.nirvana.data.Nurse;
import name.leesah.nirvana.data.Pharmacist;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.medication.reminding.RemindingModel;
import name.leesah.nirvana.model.medication.repeating.RepeatingModel;
import name.leesah.nirvana.model.reminder.Reminder;
import name.leesah.nirvana.model.reminder.ReminderFactory;
import name.leesah.nirvana.ui.medication.reminding.RemindingModelSelectFragment;
import name.leesah.nirvana.ui.medication.repeating.RepeatingModelSelectFragment;
import name.leesah.nirvana.utils.DateTimeHelper;
import name.leesah.nirvana.ui.reminder.AlarmSecretary;

public class MedicationEditActivity extends AppCompatActivity {

    private FloatingActionButton saveButton;
    private BasicsFragment basicsFragment;
    private RemindingModelSelectFragment remindingModelSelectFragment;
    private RepeatingModelSelectFragment repeatingModelSelectFragment;
    private Preference remindingModelPreference;
    private Preference repeatingModelPreference;
    private boolean basicsValid = false;
    private RemindingModel remindingModel;
    private RepeatingModel repeatingModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_edit);

        basicsFragment = new BasicsFragment();
        remindingModelSelectFragment = new RemindingModelSelectFragment();
        repeatingModelSelectFragment = new RepeatingModelSelectFragment();

        basicsFragment.setRemindingModelListener(p -> startEditingRemindingModel());
        basicsFragment.setRepeatingModelListener(p -> startEditingRepeatingModel());

        saveButton = (FloatingActionButton) findViewById(R.id.save_button);
        saveButton.setEnabled(false);
        saveButton.setBackgroundTintList(getColorStateList(R.color.fab_colors));

        basicsFragment.setValidityReportListener(this::onBasicsReportValidity);
        remindingModelSelectFragment.setValidityReportListener(this::onRemindingModelReportValidity);
        repeatingModelSelectFragment.setValidityReportListener(this::onRepeatingModelReportValidity);

        backToBasics();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onNavigateBack();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onNavigateBack() {
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment == basicsFragment)
            NavUtils.navigateUpFromSameTask(this);
        else
            backToBasics();
    }

    private void saveMedication() {
        Pharmacist pharmacist = Pharmacist.getInstance(this);
        ReminderFactory reminderFactory = new ReminderFactory(this);
        Nurse nurse = Nurse.getInstance(this);
        AlarmSecretary alarmSecretary = AlarmSecretary.getInstance(this);

        Medication medication = basicsFragment.readMedication();
        medication.setRemindingModel(remindingModel);
        medication.setRepeatingModel(repeatingModel);
        pharmacist.addMedication(medication);

        Set<Reminder> reminders = reminderFactory.createReminders(medication, DateTimeHelper.today());
        nurse.replace(reminder -> reminder.getMedicationId() == medication.getId(), reminders);
        reminders.forEach(alarmSecretary::setAlarm);

        setResult(RESULT_OK);
        finish();
    }

    private void saveRemindingModel() {
        remindingModel = remindingModelSelectFragment.readModel();
        basicsFragment.setRemindingModelSummary(remindingModel.toString());
        backToBasics();
    }

    private void saveRepeatingModel() {
        repeatingModel = repeatingModelSelectFragment.readModel();
        basicsFragment.setRepeatingModelSummary(repeatingModel.toString());
        backToBasics();
    }

    private boolean startEditingRemindingModel() {
        saveButton.setOnClickListener(v -> saveRemindingModel());
        replaceFragment(this.remindingModelSelectFragment);
        return true;
    }

    private boolean startEditingRepeatingModel() {
        saveButton.setOnClickListener(v -> saveRepeatingModel());
        replaceFragment(repeatingModelSelectFragment);
        return true;
    }

    private void backToBasics() {
        replaceFragment(basicsFragment);
        saveButton.setOnClickListener(v -> saveMedication());
        enableSaveButtonInBasics();
    }

    private void replaceFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.content_medication, fragment)
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

    private Fragment getCurrentFragment() {
        return getFragmentManager().findFragmentById(R.id.content_medication);
    }

    private void enableSaveButtonInBasics() {
        boolean allValid = basicsValid && remindingModel !=null && repeatingModel != null;
        saveButton.setBackgroundResource(allValid ? R.color.colorAccent : android.R.color.darker_gray);
    }

    private void onBasicsReportValidity(boolean valid) {
        this.basicsValid = valid;
        enableSaveButtonInBasics();
    }

    private void onRemindingModelReportValidity(boolean valid) {
        saveButton.setEnabled(valid);
    }

    private void onRepeatingModelReportValidity(boolean valid) {
        saveButton.setEnabled(valid);
    }

    public interface ValidityReportListener {
        void onValidityReport(boolean valid);
    }
}
