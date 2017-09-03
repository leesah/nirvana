package name.leesah.nirvana;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import name.leesah.nirvana.model.medication.DosageForm;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.medication.reminding.CertainHours;
import name.leesah.nirvana.model.medication.reminding.RemindingStrategy;
import name.leesah.nirvana.model.medication.repeating.Everyday;
import name.leesah.nirvana.model.medication.repeating.RepeatingStrategy;
import name.leesah.nirvana.model.medication.repeating.WithInterval;
import name.leesah.nirvana.model.medication.starting.Delayed;
import name.leesah.nirvana.model.medication.starting.ExactDate;
import name.leesah.nirvana.model.medication.starting.Immediately;
import name.leesah.nirvana.model.medication.starting.StartingStrategy;
import name.leesah.nirvana.model.medication.stopping.InPeriod;
import name.leesah.nirvana.model.medication.stopping.Never;
import name.leesah.nirvana.model.medication.stopping.StoppingStrategy;
import name.leesah.nirvana.model.reminder.Reminder;
import name.leesah.nirvana.model.reminder.ReminderMaker;
import name.leesah.nirvana.model.reminder.TimedDosage;
import name.leesah.nirvana.persistence.Nurse;
import name.leesah.nirvana.persistence.Pharmacist;
import name.leesah.nirvana.persistence.Therapist;
import name.leesah.nirvana.ui.reminder.AlarmSecretary;
import name.leesah.nirvana.ui.reminder.RemindingService;

import static android.content.Context.MODE_PRIVATE;
import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.google.common.collect.Sets.newHashSet;
import static java.lang.Integer.MAX_VALUE;
import static java.util.Collections.shuffle;
import static java.util.Collections.singletonList;
import static java.util.EnumSet.allOf;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.IntStream.range;
import static name.leesah.nirvana.PhoneBook.fireEveryone;
import static name.leesah.nirvana.PhoneBook.hireAlarmSecretary;
import static name.leesah.nirvana.PhoneBook.hireNurse;
import static name.leesah.nirvana.PhoneBook.hirePharmacist;
import static name.leesah.nirvana.PhoneBook.hireReminderMaker;
import static name.leesah.nirvana.PhoneBook.hireTherapist;
import static name.leesah.nirvana.PhoneBook.therapist;
import static name.leesah.nirvana.persistence.Nurse.PREFERENCE_KEY_REMINDERS;
import static name.leesah.nirvana.persistence.Pharmacist.PREFERENCE_KEY_MEDICATIONS;
import static name.leesah.nirvana.ui.medication.MedicationActivity.STAGING;
import static name.leesah.nirvana.ui.reminder.RemindingService.ACTION_SHOW_REMINDER;
import static name.leesah.nirvana.utils.AdaptedGsonFactory.getGson;
import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;
import static org.joda.time.LocalTime.now;
import static org.joda.time.Period.days;

public class LanternGenie {

    public static final Period YEAR = days(365);
    private static final Random random = new Random();

    public static void everythingVanishes(Context context) {
        getDefaultSharedPreferences(context)
                .edit().clear().apply();
        context.getSharedPreferences(STAGING, MODE_PRIVATE)
                .edit().clear().apply();
        fireEveryone();
        cancelAllAlarms(context);
    }

    public static void hire(Nurse nurse) {
        hireNurse(nurse);
    }

    public static void hire(Pharmacist pharmacist) {
        hirePharmacist(pharmacist);
    }

    public static void hire(Therapist therapist) {
        hireTherapist(therapist);
    }

    public static void hire(ReminderMaker reminderMaker) {
        hireReminderMaker(reminderMaker);
    }

    public static void hire(AlarmSecretary alarmSecretary) {
        hireAlarmSecretary(alarmSecretary);
    }

    public static void treatmentDisabled(Context context) {
        getDefaultSharedPreferences(context).edit()
                .putBoolean(context.getString(R.string.pref_key_treatment_enabled), false).apply();
    }

    public static Set<Medication> randomMedications(Context context, boolean handToPharmacist) {
        return range(0, randomAmount(128)).mapToObj(i -> randomMedication(context, handToPharmacist)).collect(toSet());
    }

    @NonNull
    public static Medication randomMedication(Context context, boolean handToPharmacist) {
        Medication medication = randomMedication(context);
        if (handToPharmacist)
            letPharmacistHave(context, medication);
        return medication;
    }

    public static void letPharmacistHave(Context context, @NonNull Medication medication) {
        Set<String> all = getDefaultSharedPreferences(context).getStringSet(PREFERENCE_KEY_MEDICATIONS, newHashSet());
        all.add(getGson().toJson(medication));
        getDefaultSharedPreferences(context).edit().putStringSet(PREFERENCE_KEY_MEDICATIONS, all).apply();
        hirePharmacist(null);
    }

    public static Set<Reminder> randomReminders(Context context, boolean handToNurse) {
        int medicationId = randomMedication(context, true).getId();
        return range(0, randomAmount(128))
                .mapToObj(i -> randomReminderOnDayForMedication(context, handToNurse, randomDay(), medicationId))
                .collect(toSet());
    }

    public static Set<Reminder> randomReminders(Context context, boolean handToNurse, LocalDate date) {
        int medicationId = randomMedication(context, true).getId();
        return range(0, randomAmount(128))
                .mapToObj(i -> randomReminderOnDayForMedication(context, handToNurse, date, medicationId))
                .collect(toSet());
    }

    @NonNull
    public static Reminder randomReminder(Context context, boolean handToNurse) {
        int medicationId = randomMedication(context, true).getId();
        return randomReminderOnDayForMedication(context, handToNurse, randomDay(), medicationId);
    }

    @NonNull
    public static Reminder randomReminder(Context context, boolean handToNurse, LocalDate date) {
        int medicationId = randomMedication(context, true).getId();
        return randomReminderOnDayForMedication(context, handToNurse, date, medicationId);
    }

    @NonNull
    private static Reminder randomReminderOnDayForMedication(Context context, boolean handToNurse, LocalDate date, int medicationId) {
        Reminder reminder = new Reminder(date, now(), medicationId, randomAmount(8));
        if (handToNurse)
            letNurseHave(context, reminder);
        return reminder;
    }

    public static void letNurseHave(Context context, Reminder reminder) {
        Set<String> all = getDefaultSharedPreferences(context).getStringSet(PREFERENCE_KEY_REMINDERS, newHashSet());
        all.add(getGson().toJson(reminder));
        getDefaultSharedPreferences(context).edit().putStringSet(PREFERENCE_KEY_REMINDERS, all).apply();
        hireNurse(null);
    }

    public static String randomName() {
        return range(0, random.nextInt(2) + 2)
                .mapToObj(i -> capitalizeFully(randomAlphabetic(2, 8)))
                .collect(joining(" "));
    }

    public static int randomAmount() {
        return randomAmount(1 << 8);
    }

    public static int randomAmount(int maximum) {
        return random.nextInt(maximum) + 1;
    }


    public static LocalDate randomDay() {
        return randomDayAfter(randomDayBefore(today()));
    }

    @NonNull
    public static LocalDate randomDayBefore(LocalDate date) {
        return date.minus(randomPeriod());
    }

    public static LocalDate randomDayAfter(LocalDate date) {
        return date.plus(randomPeriod());
    }

    @NonNull
    public static Period randomPeriod() {
        return randomPeriod(365);
    }

    @NonNull
    public static Period randomPeriod(Period maximum) {
        return randomPeriod(new Period(maximum, PeriodType.days()).getDays());
    }

    @NonNull
    private static Period randomPeriod(int maximumDays) {
        return days(randomAmount(maximumDays)).plus(Days.ONE);
    }

    @NonNull
    private static Medication randomMedication(Context context) {
        return new Medication.Builder()
                .setName(randomName())
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
        return therapist(context).isCycleSupportEnabled() ?
                takeAChance() < 20 ?
                        new Immediately() :
                        new Delayed(days(random.nextInt(365))) :
                new ExactDate(randomDay());
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
                new WithInterval(randomAmount(8));
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