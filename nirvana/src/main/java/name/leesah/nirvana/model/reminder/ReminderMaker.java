package name.leesah.nirvana.model.reminder;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.joda.time.LocalDate;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.treatment.Treatment;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static name.leesah.nirvana.PhoneBook.pharmacist;
import static name.leesah.nirvana.PhoneBook.therapist;
import static name.leesah.nirvana.utils.DateTimeHelper.toText;
import static org.joda.time.format.DateTimeFormat.shortDateTime;

public class ReminderMaker {
    private static final String TAG = ReminderMaker.class.getSimpleName();
    private final Context context;

    public ReminderMaker(Context context) {
        this.context = context;
    }

    public Set<Reminder> createReminders(@NonNull Medication medication, @NonNull LocalDate date) {
        return createRemindersIfAllConditionsMet(singleton(medication), therapist(context).getTreatment(), date);
    }

    public Set<Reminder> createReminders(@NonNull LocalDate date) {
        return createRemindersIfAllConditionsMet(pharmacist(context).getMedications(), therapist(context).getTreatment(), date);
    }

    @NonNull
    private Set<Reminder> createRemindersIfAllConditionsMet(Set<Medication> medications, Treatment treatment, @NonNull LocalDate date) {
        Set<Reminder> reminders = medications.stream()
                .filter(hasStarted(treatment, date))
                .filter(hasStopped(treatment, date).negate())
                .filter(matchesDate(treatment, date))
                .flatMap(createRemindersOn(date))
                .collect(toSet());

        Bundle params = new Bundle();
        params.putInt("reminders_count", reminders.size());
        FirebaseAnalytics.getInstance(context).logEvent("reminders_created", params);
        Log.i(TAG, String.format("Created %d reminder(s) for %s.", reminders.size(), toText(date)));
        return reminders;
    }

    private Predicate<Medication> hasStarted(Treatment treatment, LocalDate date) {
        return medication -> medication.getStartingStrategy().hasStarted(treatment, date);
    }

    private Predicate<Medication> hasStopped(Treatment treatment, LocalDate date) {
        return medication -> medication.getStoppingStrategy().hasStopped(treatment, date);
    }

    private Predicate<Medication> matchesDate(Treatment treatment, LocalDate date) {
        return medication -> medication.getRepeatingStrategy().matches(treatment, medication.getStartingStrategy(), date);
    }

    @NonNull
    private Function<Medication, Stream<? extends Reminder>> createRemindersOn(@NonNull LocalDate date) {
        return medication -> medication.getRemindingStrategy().getRemindersThroughDay(medication, date).stream();
    }

}