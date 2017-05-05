package name.leesah.nirvana.ui.reminder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import name.leesah.nirvana.R;
import name.leesah.nirvana.data.Nurse;
import name.leesah.nirvana.data.Pharmacist;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.reminder.Reminder;
import name.leesah.nirvana.model.reminder.TimedDosage;
import name.leesah.nirvana.ui.widget.MedicationCard;
import name.leesah.nirvana.ui.widget.TimedDosageCard;

import static java.lang.String.format;
import static java.util.Locale.US;

public class ReminderDetailsActivity extends AppCompatActivity {

    private static final String TAG = ReminderDetailsActivity.class.getSimpleName();

    public static final String EXTRA_REMINDER_ID = "name.leesah.nirvana:extra:REMINDER_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_details);

        if (!getIntent().hasExtra(EXTRA_REMINDER_ID)) {
            Log.wtf(TAG, "Reminder ID missing from intent. Closing.");
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        int reminderId = getIntent().getIntExtra(EXTRA_REMINDER_ID, 0);
        Reminder reminder = Nurse.getInstance(this).getReminder(reminderId);

        if (reminder == null) {
            Log.wtf(TAG, format(US, "Reminder ID [%d] not recognized by nurse. Closing.", reminderId));
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        Medication medication = Pharmacist.getInstance(this).getMedication(reminder.getMedicationId());
        if (medication == null) {
            Log.wtf(TAG, format(US, "Medication ID [%d] not recognized by pharmacist. Closing.", reminder.getMedicationId()));
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        MedicationCard medicationCard = (MedicationCard) findViewById(R.id.medication);
        medicationCard.setMedication(medication);

        TimedDosageCard dosageCard = (TimedDosageCard) findViewById(R.id.dosage);
        dosageCard.setDosage(new TimedDosage(reminder.getTime(), reminder.getDosageAmount()));

    }
}
