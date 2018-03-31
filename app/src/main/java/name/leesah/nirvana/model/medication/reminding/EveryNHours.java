package name.leesah.nirvana.model.medication.reminding;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.common.collect.ImmutableList;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.reminder.Reminder;
import name.leesah.nirvana.model.reminder.TimedDosage;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.Locale.US;
import static java.util.stream.Collectors.toSet;
import static name.leesah.nirvana.utils.DateTimeHelper.toText;

/**
 * Created by sah on 2017-04-16.
 */

public class EveryNHours implements RemindingStrategy {

    public static final int FOUR = 4;
    public static final int SIX = 6;
    public static final int EIGHT = 8;
    public static final int TWELVE = 12;
    public static final List<Integer> VALID_VALUES = ImmutableList.of(FOUR, SIX, EIGHT, TWELVE);
    private final TimedDosage firstDose;
    private final int interval;

    public EveryNHours(TimedDosage firstDose, int interval) {
        if (!VALID_VALUES.contains(interval) || !isCombinationLogical(firstDose.getTimeOfDay(), interval))
            throw new IllegalArgumentException(format(US, "Illogical values for %s model: firstDosage=[%s], n=[%d].",
                    EveryNHours.class.getSimpleName(), firstDose, interval));

        this.firstDose = firstDose;
        this.interval = interval;
    }

    public static boolean isCombinationLogical(LocalTime firstDoseTime, int interval) {
        return firstDoseTime.plusHours(interval).isAfter(firstDoseTime);
    }

    @NonNull
    @Override
    public Set<Reminder> getRemindersThroughDay(Medication medication, LocalDate date) {
        List<Integer> hours = calculateHoursTillMidnight(firstDose.getTimeOfDay().getHourOfDay(), interval, emptyList());
        return hours.stream()
                .map(h -> new Reminder(date, firstDose.getTimeOfDay().withHourOfDay(h), medication.getId(), firstDose.getAmount()))
                .collect(toSet());
    }

    private List<Integer> calculateHoursTillMidnight(int since, int interval, List<Integer> accumulator) {
        List<Integer> hours = new ArrayList<>(accumulator);
        hours.add(since);
        int next = since + interval;
        return next >= 24 ? hours : calculateHoursTillMidnight(next, interval, hours);
    }

    public TimedDosage getFirstDose() {
        return firstDose;
    }

    public int getInterval() {
        return interval;
    }

    @Override
    public String toString(Context context) {
        String doses = context.getResources().getQuantityString(R.plurals.var_doses, firstDose.getAmount(), firstDose.getAmount());
        String everyNHours = context.getResources().getQuantityString(R.plurals.to_string_every_n_hours_every_n_hours, interval, interval);
        return context.getString(R.string.to_string_every_n_hours, doses, everyNHours, toText(firstDose.getTimeOfDay()));
    }
}
