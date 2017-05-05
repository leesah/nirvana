package name.leesah.nirvana.ui.medication;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.Medication;

import static android.support.v4.app.NavUtils.navigateUpFromSameTask;
import static name.leesah.nirvana.utils.AdaptedGsonFactory.getGson;

public class MedicationActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_ADD_MEDICATION = 1000;
    public static final int REQUEST_CODE_EDIT_MEDICATION = 2000;
    public static final String ACTION_ADD_MEDICATION = "name.leesah.nirvana:action:ADD_MEDICATION";
    public static final String ACTION_EDIT_MEDICATION = "name.leesah.nirvana:action:EDIT_MEDICATION";
    public static final String STAGING = "name.leesah.nirvana:preference:MEDICATION_STAGING";
    private MedicationFragment medicationFragment = new MedicationFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_container);

        setTitle(getSharedPreferences(STAGING, MODE_PRIVATE).getString(
                getString(R.string.pref_key_medication_name),
                getString(R.string.add_medication)));

        getFragmentManager().beginTransaction()
                .add(R.id.content, medicationFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                confirmBeforeClosing(() -> {
                    clearStaged(this);
                    navigateUpFromSameTask(this);
                });
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        confirmBeforeClosing(() -> {
            clearStaged(this);
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    public static void add(Activity caller) {
        clearStaged(caller);
        Intent intent = new Intent(caller, MedicationActivity.class)
                .setAction(ACTION_ADD_MEDICATION);
        caller.startActivityForResult(intent, REQUEST_CODE_ADD_MEDICATION);
    }

    public static void edit(Activity caller, Medication medication) {
        writeToStaged(caller, medication);
        Intent intent = new Intent(caller, MedicationActivity.class)
                .setAction(ACTION_EDIT_MEDICATION);
        caller.startActivityForResult(intent, REQUEST_CODE_EDIT_MEDICATION);
    }

    private static void clearStaged(Context context) {
        context.getSharedPreferences(STAGING, MODE_PRIVATE).edit().clear().apply();
    }

    private void confirmBeforeClosing(Runnable onOkay) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.alert_abort_medication_title)
                .setMessage(R.string.alert_abort_medication_message)
                .setPositiveButton(android.R.string.ok, (d, w) -> onOkay.run())
                .setNegativeButton(android.R.string.cancel, null)
                .create().show();
    }

    private static void writeToStaged(Context context, Medication medication) {
        context.getSharedPreferences(STAGING, MODE_PRIVATE).edit()
                .putInt(context.getString(R.string.pref_key_medication_id),
                        medication.getId())
                .putString(context.getString(R.string.pref_key_medication_name),
                        medication.getName())
                .putString(context.getString(R.string.pref_key_medication_manufacturer),
                        medication.getManufacturer())
                .putString(context.getString(R.string.pref_key_medication_dosage_form),
                        medication.getForm().name())
                .putString(context.getString(R.string.pref_key_medication_reminding),
                        getGson().toJson(medication.getRemindingStrategy()))
                .putString(context.getString(R.string.pref_key_medication_repeating),
                        getGson().toJson(medication.getRepeatingStrategy()))
                .putString(context.getString(R.string.pref_key_medication_starting),
                        getGson().toJson(medication.getStartingStrategy()))
                .putString(context.getString(R.string.pref_key_medication_stopping),
                        getGson().toJson(medication.getStoppingStrategy()))
                .apply();
    }

}
