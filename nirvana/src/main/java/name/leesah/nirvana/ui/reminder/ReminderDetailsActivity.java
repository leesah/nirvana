package name.leesah.nirvana.ui.reminder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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
import static name.leesah.nirvana.model.reminder.Reminder.State.DONE;
import static name.leesah.nirvana.ui.reminder.RemindingService.*;

public class ReminderDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_REMINDER_ID = "name.leesah.nirvana:extra:REMINDER_ID";
    private int reminderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_details);

        if (!getIntent().hasExtra(EXTRA_REMINDER_ID))
            throw new IllegalArgumentException("Reminder ID missing from intent.");

        Nurse nurse = Nurse.getInstance(this);
        reminderId = getIntent().getIntExtra(EXTRA_REMINDER_ID, 0);
        Reminder reminder = nurse.getReminder(reminderId);

        if (reminder == null)
            throw new IllegalArgumentException(
                    format(US, "Reminder #[%d] not recognized by nurse.", reminderId));

        Medication medication = Pharmacist.getInstance(this).getMedication(reminder.getMedicationId());
        if (medication == null)
            throw new IllegalArgumentException(
                    format(US, "Medication #[%d] not recognized by pharmacist.", reminder.getMedicationId()));

        MedicationCard medicationCard = (MedicationCard) findViewById(R.id.medication);
        medicationCard.setMedication(medication);

        TimedDosageCard dosageCard = (TimedDosageCard) findViewById(R.id.dosage);
        dosageCard.setDosage(new TimedDosage(reminder.getTime(), reminder.getDosageAmount()));

        View confirm = findViewById(R.id.done_button);
        View snooze = findViewById(R.id.snooze_button);
        View ignore = findViewById(R.id.ignore_button);

        switch (reminder.getState()) {
            case DONE:
            case PLANNED:
                confirm.setEnabled(false);
                snooze.setEnabled(false);
                ignore.setEnabled(false);
                break;

            case NOTIFIED:
            case SNOOZED:
                confirm.setOnClickListener(v -> confirmAndFinish());
                snooze.setOnClickListener(v -> snoozeAndFinish());
                ignore.setOnClickListener(v -> confirmAndFinish());
                break;
        }

    }

    private void confirmAndFinish() {
        confirmReminder(this, reminderId);
        setResult(RESULT_OK);
        finish();
    }

    private void snoozeAndFinish() {
        snoozeReminder(this, reminderId);
        setResult(RESULT_OK);
        finish();
    }

}
