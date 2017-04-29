package name.leesah.nirvana;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

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
import name.leesah.nirvana.model.medication.MedicationBuilder;
import name.leesah.nirvana.model.medication.reminding.AtCertainHours;
import name.leesah.nirvana.model.medication.reminding.RemindingStrategy;
import name.leesah.nirvana.model.medication.repeating.Everyday;
import name.leesah.nirvana.model.medication.repeating.RepeatingStrategy;
import name.leesah.nirvana.model.medication.starting.Delayed;
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
import static name.leesah.nirvana.DateTimeRelatedTestHelper.randomDay;
import static name.leesah.nirvana.data.Nurse.PREFERENCE_KEY_REMINDERS;
import static name.leesah.nirvana.data.Pharmacist.PREFERENCE_KEY_MEDICATIONS;
import static name.leesah.nirvana.ui.reminder.RemindingService.ACTION_SHOW_REMINDER;
import static name.leesah.nirvana.utils.AdaptedGsonFactory.getGson;
import static name.leesah.nirvana.utils.IdentityHelper.uniqueInt;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;
import static org.joda.time.LocalTime.now;
import static org.joda.time.Period.*;

public class LanternGenie {

    private static final Random random = new Random();

    public static void everythingVanishesSilVousPlait(Context context) {
        preferences(context).edit().clear().apply();
        Pharmacist.reset();
        Nurse.reset();
        Therapist.reset();
        cancelAllAlarms(context);
    }

    public static Set<Medication> severalRandomMedicationsSilVousPlait(Context context, int count, boolean handToPharmacist) {
        return range(0, count).mapToObj(i -> oneRandomMedicationSilVousPlait(context, handToPharmacist)).collect(toSet());
    }

    @NonNull
    public static Medication oneRandomMedicationSilVousPlait(Context context, boolean handToPharmacist) {
        Medication medication = randomMedication();
        if (handToPharmacist)
            handThisToPharmacistSilVousPlait(context, medication);
        return medication;
    }

    public static void handThisToPharmacistSilVousPlait(Context context, @NonNull Medication medication) {
        Set<String> all = preferences(context).getStringSet(PREFERENCE_KEY_MEDICATIONS, newHashSet());
        all.add(gson().toJson(medication));
        preferences(context).edit().putStringSet(PREFERENCE_KEY_MEDICATIONS, all).apply();
        Pharmacist.reset();
    }

    public static Set<Reminder> severalRandomRemindersSilVousPlait(Context context, int count, boolean handToPharmacist) {
        return range(0, count).mapToObj(i -> oneRandomReminderSilVousPlait(context, handToPharmacist)).collect(toSet());
    }

    public static Reminder oneRandomReminderSilVousPlait(Context context, boolean handToNurse) {
        Reminder reminder = new Reminder(randomDay(), now(), uniqueInt(), randomPositiveInt(8));
        if (handToNurse)
            handThisToNurseSilVousPlait(context, reminder);
        return reminder;
    }

    public static void handThisToNurseSilVousPlait(Context context, Reminder reminder) {
        Set<String> all = preferences(context).getStringSet(PREFERENCE_KEY_REMINDERS, newHashSet());
        all.add(gson().toJson(reminder));
        preferences(context).edit().putStringSet(PREFERENCE_KEY_REMINDERS, all).apply();
        Nurse.reset();
    }


    @NonNull
    private static Medication randomMedication() {
        return new MedicationBuilder()
                .setName(randomString())
                .setForm(randomForm())
                .setRemindingStrategy(randomRemindingStrategy())
                .setRepeatingStrategy(randomRepeatingStrategy())
                .setStartingStrategy(randomStartingStrategy())
                .setStoppingStrategy(randomStoppingStrategy())
                .build();
    }

    private static StoppingStrategy randomStoppingStrategy() {
        return random.nextInt() % 100 < 20 ? new Never() : new InPeriod(days(random.nextInt(365)));
    }

    private static StartingStrategy randomStartingStrategy() {
        return random.nextInt() % 100 < 20 ? new Immediately() : new Delayed(days(random.nextInt(365)));
    }

    private static String randomString() {
        return range(0, random.nextInt(3))
                .mapToObj(i -> capitalizeFully(randomAlphabetic(2, 8)))
                .collect(Collectors.joining(" "));
    }

    public static int randomPositiveInt() {
        return randomPositiveInt(MAX_VALUE);
    }

    public static int randomPositiveInt(int maximum) {
        return random.nextInt(maximum) + 1;
    }

    @NonNull
    private static DosageForm randomForm() {
        List<DosageForm> forms = new ArrayList<>(allOf(DosageForm.class));
        shuffle(forms);
        return forms.get(0);
    }

    @NonNull
    private static RemindingStrategy randomRemindingStrategy() {
        return new AtCertainHours(singletonList(new TimedDosage(now(), 1)));
    }

    @NonNull
    private static RepeatingStrategy randomRepeatingStrategy() {
        return new Everyday();
    }

    private static SharedPreferences preferences(Context context) {
        return getDefaultSharedPreferences(context);
    }

    private static AlarmManager alarmManager(Context context) {
        return context.getSystemService(AlarmManager.class);
    }

    private static Gson gson() {
        return getGson();
    }

    private static void cancelAllAlarms(Context context) {
        Intent intent = new Intent(context, RemindingService.class)
                .setAction(ACTION_SHOW_REMINDER);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        alarmManager(context).cancel(pendingIntent);
    }


}