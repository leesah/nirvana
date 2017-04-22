package name.leesah.nirvana.model.reminder;

import android.content.Context;
import android.util.ArraySet;
import android.util.Log;

import org.joda.time.LocalDate;

import java.util.Set;

import name.leesah.nirvana.data.Pharmacist;
import name.leesah.nirvana.data.Therapist;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.medication.starting.StartingStrategy;
import name.leesah.nirvana.model.medication.stopping.StoppingStrategy;
import name.leesah.nirvana.model.treatment.TreatmentCycle;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;
import static name.leesah.nirvana.utils.DateTimeHelper.toText;
import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static org.joda.time.Days.days;

public class ReminderFactory {
    private static final String TAG = ReminderFactory.class.getSimpleName();
    private Therapist therapist;
    private Pharmacist pharmacist;

    public ReminderFactory(Context context) {
        therapist = Therapist.getInstance(context);
        pharmacist = Pharmacist.getInstance(context);
    }

    public Set<Reminder> createReminders(Medication medication, LocalDate date) {
        TreatmentCycle cycle = therapist.getTreatmentCycle(date);
        if (cycle == null) {
            Log.i(TAG, "Not in a treatment cycle today. Nothing to set reminder for.");
            return new ArraySet<>();
        }

        return createReminders(medication, cycle, date);

    }

    public Set<Reminder> createReminders(LocalDate date) {
        TreatmentCycle cycle = therapist.getTreatmentCycle(date);
        if (cycle == null) {
            Log.i(TAG, "Not in a treatment cycle. Not creating reminders.");
            return emptySet();
        }

        Set<Medication> medications = pharmacist.getMedications();
        Set<Reminder> reminders = medications.stream()
                .flatMap(medication -> createReminders(medication, cycle, date).stream())
                .collect(toSet());

        Log.i(TAG, String.format("Created %d reminder(s) for %s.", reminders.size(), toText(date)));
        return reminders;
    }

    private Set<Reminder> createReminders(Medication medication, TreatmentCycle cycle, LocalDate date) {
        if (medication.getRepeatingStrategy().matchesDate(cycle, date)
                && trim(cycle, medication.getStartingStrategy(), medication.getStoppingStrategy()).contains(date))
            return medication.getRemindingStrategy().getRemindersThroughDay(medication, date);
        else
            return emptySet();
    }

    private TreatmentCycle trim(TreatmentCycle cycle, StartingStrategy startingStrategy, StoppingStrategy stoppingStrategy) {
        LocalDate firstDay = startingStrategy.getFirstDay(cycle);
        return new TreatmentCycle(firstDay, stoppingStrategy.getLastDay(firstDay));
    }

}