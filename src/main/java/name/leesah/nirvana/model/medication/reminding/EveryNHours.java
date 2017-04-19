package name.leesah.nirvana.model.medication.reminding;

import android.content.Context;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.reminder.Reminder;
import name.leesah.nirvana.model.reminder.TimedDosage;

import static java.lang.String.format;
import static java.util.stream.Collectors.toSet;
import static name.leesah.nirvana.utils.DateTimeHelper.toText;

/**
 * Created by sah on 2017-04-16.
 */

public class EveryNHours implements RemindingModel {

    public static final int FOUR = 4;
    public static final int SIX = 6;
    public static final int EIGHT = 8;
    public static final int TWELVE = 12;
    public static final List<Integer> VALID_VALUES = Arrays.asList(FOUR, SIX, EIGHT, TWELVE);
    private final TimedDosage firstDose;
    private final int n;

    public EveryNHours(TimedDosage firstDose, int n) {
        if (!VALID_VALUES.contains(n)
                || firstDose.getTimeOfDay().minusHours(n).isBefore(firstDose.getTimeOfDay()))
            throw new IllegalArgumentException(format("Illogical values for %s model: firstDosage=[%s], n=[%d].",
                    EveryNHours.class.getSimpleName(), firstDose, n));

        this.firstDose = firstDose;
        this.n = n;
    }

    @NonNull
    @Override
    public Set<Reminder> getRemindersThroughDay(Medication medication, LocalDate date) {
        List<Integer> hours = calculateHoursTillMidnight(firstDose.getTimeOfDay().getHourOfDay(), n, Collections.emptyList());
        return hours.stream()
                .map(h -> new Reminder(date, firstDose.getTimeOfDay().withHourOfDay(h), medication.getId(), firstDose.getAmount()))
                .collect(toSet());
    }

    private List<Integer> calculateHoursTillMidnight(int since, int interval, List<Integer> accumulator) {
        List<Integer> hours = new ArrayList<>(accumulator);
        hours.add(since);
        int next = since + interval;
        if (next > 24) return hours;
        else return calculateHoursTillMidnight(next, interval, hours);
    }

    public TimedDosage getFirstDose() {
        return firstDose;
    }

    public int getN() {
        return n;
    }

    @Override
    public String toString(Context context) {
        String doses = context.getResources().getQuantityString(R.plurals.var_doses, firstDose.getAmount(), firstDose.getAmount());
        String everyNHours = context.getResources().getQuantityString(R.plurals.to_string_every_n_hours_every_n_hours, n, n);
        return context.getString(R.string.to_string_every_n_hours, doses, everyNHours, toText(firstDose.getTimeOfDay()));
    }
}
