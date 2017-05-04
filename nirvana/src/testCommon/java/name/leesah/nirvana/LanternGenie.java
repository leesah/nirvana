package name.leesah.nirvana;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Period;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import name.leesah.nirvana.data.Nurse;
import name.leesah.nirvana.data.Pharmacist;
import name.leesah.nirvana.data.Therapist;
import name.leesah.nirvana.model.medication.DosageForm;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.medication.reminding.CertainHours;
import name.leesah.nirvana.model.medication.reminding.RemindingStrategy;
import name.leesah.nirvana.model.medication.repeating.EveryNDays;
import name.leesah.nirvana.model.medication.repeating.Everyday;
import name.leesah.nirvana.model.medication.repeating.RepeatingStrategy;
import name.leesah.nirvana.model.medication.starting.Delayed;
import name.leesah.nirvana.model.medication.starting.ExactDate;
import name.leesah.nirvana.model.medication.starting.Immediately;
import name.leesah.nirvana.model.medication.starting.StartingStrategy;
import name.leesah.nirvana.model.medication.stopping.InPeriod;
import name.leesah.nirvana.model.medication.stopping.Never;
import name.leesah.nirvana.model.medication.stopping.StoppingStrategy;
import name.leesah.nirvana.model.reminder.Reminder;
import name.leesah.nirvana.model.reminder.TimedDosage;
import name.leesah.nirvana.ui.reminder.RemindingService;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.google.common.collect.Sets.newHashSet;
import static java.lang.Integer.*;
import static java.util.Collections.shuffle;
import static java.util.Collections.singletonList;
import static java.util.EnumSet.allOf;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.IntStream.range;
import static name.leesah.nirvana.data.Nurse.PREFERENCE_KEY_REMINDERS;
import static name.leesah.nirvana.data.Pharmacist.PREFERENCE_KEY_MEDICATIONS;
import static name.leesah.nirvana.ui.reminder.RemindingService.ACTION_SHOW_REMINDER;
import static name.leesah.nirvana.utils.AdaptedGsonFactory.getGson;
import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static name.leesah.nirvana.utils.IdentityHelper.uniqueInt;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;
import static org.joda.time.LocalTime.now;
import static org.joda.time.Period.*;

public class LanternGenie {

    public static final Period YEAR = days(365);
    private static final Random random = new Random();

    public static void everythingVanishesSilVousPlait(Context context) {
        getDefaultSharedPreferences(context).edit().clear().apply();
        Pharmacist.reset();
        Nurse.reset();
        Therapist.reset();
        cancelAllAlarms(context);
    }

    public static void cycledTreatmentDisabledSilVousPlait(Context context) {
        getDefaultSharedPreferences(context).edit()
                .putBoolean(context.getString(R.string.pref_key_treatment_enabled), false).apply();
    }

    public static Set<Medication> severalRandomMedicationsSilVousPlait(Context context, int count, boolean handToPharmacist) {
        return range(0, count).mapToObj(i -> oneRandomMedicationSilVousPlait(context, handToPharmacist)).collect(toSet());
    }

    @NonNull
    public static Medication oneRandomMedicationSilVousPlait(Context context, boolean handToPharmacist) {
        Medication medication = randomMedication(context);
        if (handToPharmacist)
            handThisToPharmacistSilVousPlait(context, medication);
        return medication;
    }

    public static void handThisToPharmacistSilVousPlait(Context context, @NonNull Medication medication) {
        Set<String> all = getDefaultSharedPreferences(context).getStringSet(PREFERENCE_KEY_MEDICATIONS, newHashSet());
        all.add(getGson().toJson(medication));
        getDefaultSharedPreferences(context).edit().putStringSet(PREFERENCE_KEY_MEDICATIONS, all).apply();
        Pharmacist.reset();
    }

    public static Set<Reminder> severalRandomRemindersSilVousPlait(Context context, int count, boolean handToPharmacist) {
        return range(0, count).mapToObj(i -> oneRandomReminderSilVousPlait(context, handToPharmacist)).collect(toSet());
    }

    public static Reminder oneRandomReminderSilVousPlait(Context context, boolean handToNurse) {
        Reminder reminder = new Reminder(randomDaySilVousPlait(), now(), uniqueInt(), randomPositiveIntSilVousPlait(8));
        if (handToNurse)
            handThisToNurseSilVousPlait(context, reminder);
        return reminder;
    }

    public static void handThisToNurseSilVousPlait(Context context, Reminder reminder) {
        Set<String> all = getDefaultSharedPreferences(context).getStringSet(PREFERENCE_KEY_REMINDERS, newHashSet());
        all.add(getGson().toJson(reminder));
        getDefaultSharedPreferences(context).edit().putStringSet(PREFERENCE_KEY_REMINDERS, all).apply();
        Nurse.reset();
    }

    public static String randomStringSilVousPlait() {
        return range(0, random.nextInt(3))
                .mapToObj(i -> capitalizeFully(randomAlphabetic(2, 8)))
                .collect(Collectors.joining(" "));
    }

    public static int randomPositiveIntSilVousPlait() {
        return randomPositiveIntSilVousPlait(1 << 10);
    }

    public static int randomPositiveIntSilVousPlait(int maximum) {
        return random.nextInt(maximum) + 1;
    }


    public static LocalDate randomDaySilVousPlait() {
        return randomDaySilVousPlaitAfter(randomDaySilVousPlaitBefore(today()));
    }

    @NonNull
    public static LocalDate randomDaySilVousPlaitBefore(LocalDate date) {
        return date.minus(randomPeriodSilVousPlait());
    }

    public static LocalDate randomDaySilVousPlaitAfter(LocalDate date) {
        return date.plus(randomPeriodSilVousPlait());
    }

    @NonNull
    public static Period randomPeriodSilVousPlait() {
        return randomPeriodSilVousPlait(YEAR);
    }

    @NonNull
    public static Period randomPeriodSilVousPlait(Period maximum) {
        return days(random.nextInt(maximum.getDays())).plus(Days.ONE);
    }
    @NonNull
    private static Medication randomMedication(Context context) {
        return new Medication.Builder()
                .setName(randomStringSilVousPlait())
                .setForm(randomForm())
                .setRemindingStrategy(randomRemindingStrategy())
                .setRepeatingStrategy(randomRepeatingStrategy())
                .setStartingStrategy(randomStartingStrategy(context))
                .setStoppingStrategy(randomStoppingStrategy())
                .build();
    }

    private static StoppingStrategy randomStoppingStrategy() {
        return random.nextInt() % 100 < 20 ? new Never() : new InPeriod(days(random.nextInt(365)));
    }

    private static StartingStrategy randomStartingStrategy(Context context) {
        return Therapist.getInstance(context).isCycleSupportEnabled() ?
                takeAChance() < 20 ?
                        new Immediately() :
                        new Delayed(days(random.nextInt(365))) :
                new ExactDate(randomDaySilVousPlait());
    }

    @NonNull
    private static DosageForm randomForm() {
        List<DosageForm> forms = new ArrayList<>(allOf(DosageForm.class));
        shuffle(forms);
        return forms.get(0);
    }

    @NonNull
    private static RemindingStrategy randomRemindingStrategy() {
        return new CertainHours(singletonList(new TimedDosage(now(), 1)));
    }

    @NonNull
    private static RepeatingStrategy randomRepeatingStrategy() {
        return takeAChance() < 20 ?
                new Everyday() :
                new EveryNDays(randomPositiveIntSilVousPlait(8));
    }

    private static void cancelAllAlarms(Context context) {
        Intent intent = new Intent(context, RemindingService.class)
                .setAction(ACTION_SHOW_REMINDER);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        context.getSystemService(AlarmManager.class).cancel(pendingIntent);
    }

    private static int takeAChance() {
        return random.nextInt(MAX_VALUE) % 100;
    }

}