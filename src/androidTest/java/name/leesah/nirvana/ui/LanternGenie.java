package name.leesah.nirvana.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import name.leesah.nirvana.data.Nurse;
import name.leesah.nirvana.data.Pharmacist;
import name.leesah.nirvana.data.Therapist;
import name.leesah.nirvana.model.medication.DosageForm;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.medication.MedicationBuilder;
import name.leesah.nirvana.model.medication.reminding.AtCertainHours;
import name.leesah.nirvana.model.medication.reminding.RemindingModel;
import name.leesah.nirvana.model.medication.repeating.Everyday;
import name.leesah.nirvana.model.medication.repeating.RepeatingModel;
import name.leesah.nirvana.model.reminder.TimedDosage;
import name.leesah.nirvana.ui.reminder.RemindingService;

import static android.preference.PreferenceManager.*;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.core.deps.guava.collect.Sets.*;
import static java.util.Collections.*;
import static java.util.Collections.singletonList;
import static java.util.EnumSet.*;
import static java.util.UUID.*;
import static name.leesah.nirvana.data.Pharmacist.PREFERENCE_KEY_MEDICATIONS;
import static name.leesah.nirvana.ui.reminder.RemindingService.ACTION_SHOW_REMINDER;
import static name.leesah.nirvana.utils.AdaptedGsonFactory.*;
import static org.joda.time.LocalTime.now;

public class LanternGenie {

    private static SharedPreferences defaultSharedPreferences;
    private static Gson gson;
    private static AlarmManager alarmManager;

    private static SharedPreferences preferences() {
        if (defaultSharedPreferences == null)
            defaultSharedPreferences = getDefaultSharedPreferences(context());
        return defaultSharedPreferences;
    }

    private static AlarmManager alarmManager() {
        if (alarmManager == null)
            alarmManager = context().getSystemService(AlarmManager.class);
        return alarmManager;
    }

    private static Context context() {
        return getTargetContext();
    }

    private static Gson gson() {
        if (gson == null)
            gson = getGson();
        return gson;
    }

    @NonNull
    public static Medication oneRandomMedicationSilVousPlait() {
        Medication medication = randomMedication();
        thisExactMedicationSilVousPlait(medication);
        return medication;
    }

    public static void thisExactMedicationSilVousPlait(@NonNull Medication medication) {
        Set<String> all = preferences().getStringSet(PREFERENCE_KEY_MEDICATIONS, newHashSet());
        all.add(gson().toJson(medication));
        preferences().edit().putStringSet(PREFERENCE_KEY_MEDICATIONS, all).apply();
        Pharmacist.reset();
    }

    @NonNull
    private static Medication randomMedication() {
        return new MedicationBuilder()
                .setName(randomString())
                .setForm(randomForm())
                .setRemindingModel(randomRemindingModel())
                .setRepeatingModel(randomRepeatingModel())
                .build();
    }

    private static String randomString() {
        return randomUUID().toString();
    }

    @NonNull
    private static DosageForm randomForm() {
        List<DosageForm> forms = new ArrayList<>(allOf(DosageForm.class));
        shuffle(forms);
        return forms.get(0);
    }

    @NonNull
    private static RemindingModel randomRemindingModel() {
        return new AtCertainHours(singletonList(new TimedDosage(now(), 1)));
    }

    @NonNull
    private static RepeatingModel randomRepeatingModel() {
        return new Everyday();
    }

    public static void everythingVanishesSilVousPlait() {
        preferences().edit().clear().apply();
        Pharmacist.reset();
        Nurse.reset();
        Therapist.reset();
        cancelAllAlarms();
    }

    private static void cancelAllAlarms() {
        Intent intent = new Intent(context(), RemindingService.class)
                .setAction(ACTION_SHOW_REMINDER);
        PendingIntent pendingIntent = PendingIntent.getService(context(), 0, intent, 0);
        alarmManager().cancel(pendingIntent);
    }


}