package name.leesah.nirvana.model.reminder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import org.joda.time.LocalDate;

import java.util.Set;

import name.leesah.nirvana.persistence.Pharmacist;
import name.leesah.nirvana.persistence.Therapist;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.treatment.Treatment;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;
import static name.leesah.nirvana.PhoneBook.pharmacist;
import static name.leesah.nirvana.PhoneBook.therapist;
import static name.leesah.nirvana.utils.DateTimeHelper.toText;

public class ReminderMaker {
    private static final String TAG = ReminderMaker.class.getSimpleName();

    private static ReminderMaker instance;

    private Therapist therapist;
    private Pharmacist pharmacist;

    public ReminderMaker(Context context) {
        therapist = therapist(context);
        pharmacist = pharmacist(context);
    }

    public Set<Reminder> createReminders(@NonNull Medication medication, @NonNull LocalDate date) {
        return createReminders(medication, therapist.getTreatment(), date);
    }

    public Set<Reminder> createReminders(@NonNull LocalDate date) {
        Set<Reminder> reminders = pharmacist.getMedications().stream()
                .flatMap(medication -> createReminders(medication, therapist.getTreatment(), date).stream())
                .collect(toSet());

        Log.i(TAG, String.format("Created %d reminder(s) for %s.", reminders.size(), toText(date)));
        return reminders;
    }

    private Set<Reminder> createReminders(Medication medication, Treatment treatment, LocalDate date) {
        return medication.getStartingStrategy().hasStarted(treatment, date) &&
                !medication.getStoppingStrategy().hasStopped(treatment, date) &&
                medication.getRepeatingStrategy().matches(treatment, medication.getStartingStrategy(), date) ?

                medication.getRemindingStrategy().getRemindersThroughDay(medication, date) : emptySet();
    }

}