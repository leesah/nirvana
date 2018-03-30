package name.leesah.nirvana.model.medication.reminding;

import android.content.Context;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
//TODO
public class NTimesADay implements RemindingStrategy {
    @NonNull
    @Override
    public Set<Reminder> getRemindersThroughDay(Medication medication, LocalDate date) {
        return null;
    }

    @Override
    public String toString(Context context) {
        return null;
    }
}
