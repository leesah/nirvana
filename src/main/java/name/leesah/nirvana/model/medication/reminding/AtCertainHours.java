package name.leesah.nirvana.model.medication.reminding;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.reminder.Reminder;
import name.leesah.nirvana.model.reminder.TimedDosage;

import static java.util.stream.Collectors.joining;
import static name.leesah.nirvana.utils.DateTimeHelper.today;

/**
 * Created by sah on 2016-12-11.
 */
public class AtCertainHours implements RemindingModel {
    private final List<TimedDosage> dosages = new ArrayList<>();

    public AtCertainHours(List<TimedDosage> dosages) {
        this.dosages.addAll(dosages);
    }

    @Override
    public Set<Reminder> getRemindersThroughDay(Medication medication) {
        return dosages.stream()
                .map(d -> new Reminder(today(), d.getTimeOfDay(), medication.getId(), d.getAmount()))
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return dosages.stream()
                .map(TimedDosage::toString)
                .collect(joining(", "));
    }
}
