package name.leesah.nirvana.ui.medication;


import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import static java.lang.String.format;

public class MedicationEditActivity extends AppCompatActivity {

    private static final String TAG = MedicationEditActivity.class.getSimpleName();

    public static final String ACTION_ADD_MEDICATION = "name.leesah.nirvana:action:ADD_MEDICATION";
    public static final String ACTION_EDIT_MEDICATION = "name.leesah.nirvana:action:EDIT_MEDICATION";
    public static final String EXTRA_MEDICATION_ID = "name.leesah.nirvana:extra:MEDICATION_ID";
    private FloatingActionButton saveButton;
    private BasicsFragment basicsFragment = new BasicsFragment();
    private RemindingModelSelectFragment remindingModelSelectFragment = new RemindingModelSelectFragment();
    private RepeatingModelSelectFragment repeatingModelSelectFragment = new RepeatingModelSelectFragment();
    private boolean basicsValid = false;
    private RemindingModel remindingModel;
    private RepeatingModel repeatingModel;
    private Medication editingExisting;
    private Pharmacist pharmacist;
    private ReminderFactory reminderFactory;
    private Nurse nurse;
    private AlarmSecretary alarmSecretary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_edit);

        pharmacist = Pharmacist.getInstance(this);
        reminderFactory = new ReminderFactory(this);
        nurse = Nurse.getInstance(this);
        alarmSecretary = AlarmSecretary.getInstance(this);

        basicsFragment.setRemindingModelListener(p -> startEditingRemindingModel());
        basicsFragment.setRepeatingModelListener(p -> startEditingRepeatingModel());

        saveButton = (FloatingActionButton) findViewById(R.id.save_button);
        saveButton.setEnabled(false);

        basicsFragment.setValidityReportListener(this::onBasicsReportValidity);
        remindingModelSelectFragment.setValidityReportListener(this::onRemindingModelReportValidity);
        repeatingModelSelectFragment.setValidityReportListener(this::onRepeatingModelReportValidity);

        reactToIntentAction();

        getFragmentManager().beginTransaction()
                .replace(R.id.content_medication, basicsFragment)
                .commit();
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

    private void reactToIntentAction() {
        String action = getIntent().getAction();
        switch (action) {
            case ACTION_ADD_MEDICATION:
                setTitle(R.string.add_medication);
                break;
            case ACTION_EDIT_MEDICATION:
                toEditMode();
                break;
            default:
                Log.wtf(TAG, format("Unexpected action: [%s]", action));
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }

    private void toEditMode() {
        int medicationId = getIntent().getIntExtra(EXTRA_MEDICATION_ID, 0);
        if (medicationId == 0) {
            Log.wtf(TAG, "Medication ID missing in intent.");
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        Medication medication = Pharmacist.getInstance(this).getMedication(medicationId);
        if (medication == null) {
            Log.wtf(TAG, "Medication not found at pharmacist's.");
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        setTitle(getString(R.string.edit_medication, medication.getName()));
        setEditingExisting(medication);
        basicsFragment.setEditingExisting(medication);
        remindingModelSelectFragment.setEditingExisting(medication.getRemindingModel());
        repeatingModelSelectFragment.setEditingExisting(medication.getRepeatingModel());
    }

    private void onNavigateBack() {
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment == basicsFragment)
            NavUtils.navigateUpFromSameTask(this);
        else
            backToBasics();
    }

    private void saveMedication() {
        Medication medication = basicsFragment.readMedication();
        medication.setRemindingModel(remindingModel);
        medication.setRepeatingModel(repeatingModel);
        pharmacist.addMedication(medication);

        Set<Reminder> reminders = reminderFactory.createReminders(medication, DateTimeHelper.today());
        nurse.replace(reminder -> reminder.getMedicationId() == ((editingExisting == null ? medication : editingExisting).getId()), reminders);
        reminders.forEach(alarmSecretary::setAlarm);

        if (editingExisting != null)
            pharmacist.removeMedication(editingExisting.getId());

        setResult(RESULT_OK);
        finish();
    }

    private void saveRemindingModel() {
        remindingModel = remindingModelSelectFragment.readModel();
        basicsFragment.setRemindingModelSummary(remindingModel.toString(this));
        backToBasics();
    }

    private void saveRepeatingModel() {
        repeatingModel = repeatingModelSelectFragment.readModel();
        basicsFragment.setRepeatingModelSummary(repeatingModel.toString(this));
        backToBasics();
    }

    private boolean startEditingRemindingModel() {
        saveButton.setOnClickListener(v -> saveRemindingModel());
        replaceFragmentAndAddToBackStack(this.remindingModelSelectFragment);
        return true;
    }

    private boolean startEditingRepeatingModel() {
        saveButton.setOnClickListener(v -> saveRepeatingModel());
        replaceFragmentAndAddToBackStack(repeatingModelSelectFragment);
        return true;
    }

    private void backToBasics() {
        replaceFragmentAndAddToBackStack(basicsFragment);
        saveButton.setOnClickListener(v -> saveMedication());
        enableSaveButtonInBasics();
    }

    private void replaceFragmentAndAddToBackStack(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.content_medication, fragment)
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

    private Fragment getCurrentFragment() {
        return getFragmentManager().findFragmentById(R.id.content_medication);
    }

    private void enableSaveButtonInBasics() {
        saveButton.setEnabled(basicsValid && remindingModel != null && repeatingModel != null);
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

    public void setEditingExisting(Medication editingExisting) {
        this.editingExisting = editingExisting;
    }

    public interface ValidityReportListener {
        void onValidityReport(boolean valid);
    }
}
