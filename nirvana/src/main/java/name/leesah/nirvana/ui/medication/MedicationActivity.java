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
import name.leesah.nirvana.model.medication.reminding.RemindingStrategy;
import name.leesah.nirvana.model.medication.repeating.RepeatingStrategy;
import name.leesah.nirvana.model.reminder.Reminder;
import name.leesah.nirvana.model.reminder.ReminderFactory;
import name.leesah.nirvana.ui.medication.reminding.RemindingStrategySelectFragment;
import name.leesah.nirvana.ui.medication.repeating.RepeatingStrategySelectFragment;
import name.leesah.nirvana.ui.reminder.AlarmSecretary;
import name.leesah.nirvana.utils.DateTimeHelper;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static java.lang.String.format;

public class MedicationActivity extends AppCompatActivity {

    private static final String TAG = MedicationActivity.class.getSimpleName();

    public static final String ACTION_ADD_MEDICATION = "name.leesah.nirvana:action:ADD_MEDICATION";
    public static final String ACTION_EDIT_MEDICATION = "name.leesah.nirvana:action:EDIT_MEDICATION";
    public static final String EXTRA_MEDICATION_ID = "name.leesah.nirvana:extra:MEDICATION_ID";
    private FloatingActionButton saveButton;
    private FloatingActionButton deleteButton;
    private BasicsFragment basicsFragment = new BasicsFragment();
    private RemindingStrategySelectFragment remindingStrategySelectFragment = new RemindingStrategySelectFragment();
    private RepeatingStrategySelectFragment repeatingStrategySelectFragment = new RepeatingStrategySelectFragment();
    private boolean basicsValid = false;
    private RemindingStrategy remindingStrategy;
    private RepeatingStrategy repeatingStrategy;
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

        deleteButton = (FloatingActionButton) findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(v -> deleteMedication());

        basicsFragment.setValidityReportListener(this::onBasicsReportValidity);
        remindingStrategySelectFragment.setValidityReportListener(this::onRemindingModelReportValidity);
        repeatingStrategySelectFragment.setValidityReportListener(this::onRepeatingModelReportValidity);

        getFragmentManager().beginTransaction()
                .replace(R.id.content_medication, basicsFragment)
                .commit();

        initializeAccordingToAction();
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

    private void initializeAccordingToAction() {
        String action = getIntent().getAction();
        switch (action) {
            case ACTION_ADD_MEDICATION:
                toAddMode();
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

    private void toAddMode() {
        setTitle(R.string.add_medication);
        deleteButton.setVisibility(GONE);
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

        editingExisting = medication;
        remindingStrategy = medication.getRemindingStrategy();
        repeatingStrategy = medication.getRepeatingStrategy();

        basicsFragment.setEditingExisting(medication);
        remindingStrategySelectFragment.setEditingExisting(medication.getRemindingStrategy());
        repeatingStrategySelectFragment.setEditingExisting(medication.getRepeatingStrategy());

        backToBasics();
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
        medication.setRemindingStrategy(remindingStrategy);
        medication.setRepeatingStrategy(repeatingStrategy);
        pharmacist.addMedication(medication);

        Set<Reminder> reminders = reminderFactory.createReminders(medication, DateTimeHelper.today());
        nurse.replace(reminder -> reminder.getMedicationId() == ((editingExisting == null ? medication : editingExisting).getId()), reminders);
        reminders.forEach(alarmSecretary::setAlarm);

        if (editingExisting != null)
            pharmacist.removeMedication(editingExisting.getId());

        setResult(RESULT_OK);
        finish();
    }

    private void deleteMedication() {
        if (editingExisting != null)
            pharmacist.removeMedication(editingExisting.getId());

        setResult(RESULT_OK);
        finish();
    }

    private void saveRemindingModel() {
        remindingStrategy = remindingStrategySelectFragment.readModel();
        basicsFragment.setRemindingModelSummary(remindingStrategy.toString(this));
        backToBasics();
    }

    private void saveRepeatingModel() {
        repeatingStrategy = repeatingStrategySelectFragment.readModel();
        basicsFragment.setRepeatingModelSummary(repeatingStrategy.toString(this));
        backToBasics();
    }

    private boolean startEditingRemindingModel() {
        saveButton.setOnClickListener(v -> saveRemindingModel());
        deleteButton.setVisibility(GONE);
        replaceFragmentAndAddToBackStack(this.remindingStrategySelectFragment);
        return true;
    }

    private boolean startEditingRepeatingModel() {
        saveButton.setOnClickListener(v -> saveRepeatingModel());
        deleteButton.setVisibility(GONE);
        replaceFragmentAndAddToBackStack(repeatingStrategySelectFragment);
        return true;
    }

    private void backToBasics() {
        getFragmentManager().popBackStackImmediate();
        saveButton.setOnClickListener(v -> saveMedication());
        if (editingExisting != null)
            deleteButton.setVisibility(VISIBLE);
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
        saveButton.setEnabled(basicsValid && remindingStrategy != null && repeatingStrategy != null);
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
