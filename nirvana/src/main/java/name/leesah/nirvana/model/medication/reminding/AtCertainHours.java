package name.leesah.nirvana.model.medication.reminding;

import android.content.Context;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.reminder.Reminder;
import name.leesah.nirvana.model.reminder.TimedDosage;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

/**
 * Created by sah on 2016-12-11.
 */
public class AtCertainHours implements RemindingStrategy {
    private final List<TimedDosage> dosages = new ArrayList<>();

    public AtCertainHours(List<TimedDosage> dosages) {
        this.dosages.addAll(dosages);
    }

    @NonNull
    @Override
    public Set<Reminder> getRemindersThroughDay(Medication medication, LocalDate date) {
        return dosages.stream()
                .map(d -> new Reminder(date, d.getTimeOfDay(), medication.getId(), d.getAmount()))
                .collect(toSet());
    }

    @Override
    public String toString(Context context) {
        return dosages.stream()
                .map(dosage -> dosage.toString(context))
                .collect(joining(context.getString(R.string.comma_equivalent)));
    }

    public List<TimedDosage> getDosages() {
        return new ArrayList<>(dosages);
    }
}
