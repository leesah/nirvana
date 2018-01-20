package name.leesah.nirvana.ui.reminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.reminder.Reminder;
import name.leesah.nirvana.model.reminder.TimedDosage;
import name.leesah.nirvana.ui.widget.MedicationCard;
import name.leesah.nirvana.ui.widget.TimedDosageCard;

import static java.lang.String.format;
import static java.util.Locale.US;
import static name.leesah.nirvana.PhoneBook.nurse;
import static name.leesah.nirvana.PhoneBook.pharmacist;
import static name.leesah.nirvana.ui.reminder.BellRinger.confirmReminder;

public class ReminderDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_REMINDER_ID = "name.leesah.nirvana:extra:REMINDER_ID";
    private int reminderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_details);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_REMINDER_ID))
            reminderId = intent.getIntExtra(EXTRA_REMINDER_ID, 0);
        else if  (TextUtils.isDigitsOnly(intent.getData().getLastPathSegment()))
            reminderId = Integer.valueOf(intent.getData().getLastPathSegment());
        else
            throw new IllegalArgumentException("Reminder ID missing from intent.");

        Reminder reminder = nurse(this).getReminder(reminderId);
        if (reminder == null)
            throw new IllegalArgumentException(
                    format(US, "Reminder #[%d] not recognized by nurse.", reminderId));

        Medication medication = pharmacist(this).getMedication(reminder.getMedicationId());
        if (medication == null)
            throw new IllegalArgumentException(
                    format(US, "Medication #[%d] not recognized by pharmacist.", reminder.getMedicationId()));

        MedicationCard medicationCard = (MedicationCard) findViewById(R.id.medication);
        medicationCard.setMedication(medication);

        TimedDosageCard dosageCard = (TimedDosageCard) findViewById(R.id.dosage);
        dosageCard.setDosage(new TimedDosage(reminder.getTime(), reminder.getDosageAmount()));

        View confirm = findViewById(R.id.done_button);
        View ignore = findViewById(R.id.ignore_button);

        switch (reminder.getState()) {
            case DONE:
            case PLANNED:
                confirm.setEnabled(false);
                ignore.setEnabled(false);
                break;

            case NOTIFIED:
                confirm.setOnClickListener(v -> confirmAndFinish());
                ignore.setOnClickListener(v -> confirmAndFinish());
                break;
        }

    }

    private void confirmAndFinish() {
        confirmReminder(this, reminderId);
        setResult(RESULT_OK);
        finish();
    }

}
