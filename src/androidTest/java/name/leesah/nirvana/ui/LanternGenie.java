package name.leesah.nirvana.ui;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.core.deps.guava.collect.Sets;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import name.leesah.nirvana.data.Pharmacist;
import name.leesah.nirvana.model.medication.DosageForm;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.medication.MedicationBuilder;
import name.leesah.nirvana.model.medication.reminding.AtCertainHours;
import name.leesah.nirvana.model.medication.reminding.RemindingModel;
import name.leesah.nirvana.model.medication.repeating.Everyday;
import name.leesah.nirvana.model.medication.repeating.RepeatingModel;
import name.leesah.nirvana.model.reminder.TimedDosage;
import name.leesah.nirvana.utils.AdaptedGsonFactory;

import static android.preference.PreferenceManager.*;
import static android.support.test.InstrumentationRegistry.*;
import static android.support.test.InstrumentationRegistry.getContext;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.core.deps.guava.collect.Sets.*;
import static java.util.Collections.*;
import static java.util.Collections.emptySet;
import static java.util.Collections.singletonList;
import static java.util.EnumSet.*;
import static java.util.UUID.*;
import static java.util.stream.Collectors.toSet;
import static name.leesah.nirvana.data.Pharmacist.PREFERENCE_KEY_MEDICATIONS;
import static name.leesah.nirvana.data.Pharmacist.reset;
import static name.leesah.nirvana.utils.AdaptedGsonFactory.*;
import static org.joda.time.LocalTime.now;

public class LanternGenie {

    private static SharedPreferences defaultSharedPreferences;
    private static Gson gson;

    public static void eraseEverything() {
        preferences().edit().clear().apply();
    }

    private static SharedPreferences preferences() {
        if (defaultSharedPreferences == null)
            defaultSharedPreferences = getDefaultSharedPreferences(getTargetContext());
        return defaultSharedPreferences;
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

}