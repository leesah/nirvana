package name.leesah.nirvana;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.ArraySet;
import android.widget.Toast;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import name.leesah.nirvana.data.Nurse;
import name.leesah.nirvana.data.Pharmacist;
import name.leesah.nirvana.model.medication.DosageForm;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.medication.MedicationBuilder;
import name.leesah.nirvana.model.medication.reminding.AtCertainHours;
import name.leesah.nirvana.model.medication.repeating.Everyday;
import name.leesah.nirvana.model.reminder.Reminder;
import name.leesah.nirvana.model.reminder.TimedDosage;
import name.leesah.nirvana.ui.reminder.NotificationSecretary;
import name.leesah.nirvana.ui.reminder.SchedulingService;

import static name.leesah.nirvana.model.reminder.Reminder.State.NOTIFIED;

/**
 * Created by sah on 2017-04-18.
 */

public class DebugTools {

    private final Context context;
    private final Pharmacist pharmacist;
    private final Nurse nurse;
    private final NotificationSecretary notificationSecretary;
    private final SharedPreferences defaultSharedPreferences;

    public DebugTools(Context context) {
        this.context = context;
        pharmacist = Pharmacist.getInstance(context);
        nurse = Nurse.getInstance(context);
        notificationSecretary = new NotificationSecretary(context);
        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void clearAllData() {

    }

    public void injectTestData() {
        Set<Reminder> old = nurse.replace(reminder -> true, new ArraySet<>());
        old.stream()
                .filter(r -> r.getState().equals(NOTIFIED))
                .forEach(r -> notificationSecretary.dismiss(r.getNotificationId()));
        Toast.makeText(context, "Reminders cleared.", Toast.LENGTH_SHORT).show();

        Set<Integer> medicationIds = pharmacist.getMedications().stream().map(Medication::getId).collect(Collectors.toSet());
        medicationIds.forEach(pharmacist::removeMedication);
        Toast.makeText(context, "Medications cleared.", Toast.LENGTH_SHORT).show();

        defaultSharedPreferences.edit().clear().apply();
        Toast.makeText(context, "Other preferences cleared.", Toast.LENGTH_SHORT).show();

        DateTimeFormatter formatterHHmm = DateTimeFormat.forPattern("HH:mm");
        LocalTime NineAM = LocalTime.parse("09:00", formatterHHmm);
        LocalTime NinePM = LocalTime.parse("21:00", formatterHHmm);

        Medication valaciclovir = new MedicationBuilder().
                setName("Valaciclovir").
                setManufacturer("Teva").
                setForm(DosageForm.TABLET).
                setRepeatingStrategy(new Everyday()).
                setRemindingStrategy(new AtCertainHours(Arrays.asList(new TimedDosage(NineAM, 1), new TimedDosage(NinePM, 1)))).
                build();

        Medication folacin = new MedicationBuilder().
                setName("Folacin").
                setManufacturer("folsyra").
                setForm(DosageForm.TABLET).
                setRepeatingStrategy(new Everyday()).
                setRemindingStrategy(new AtCertainHours(Collections.singletonList(new TimedDosage(NinePM, 1)))).
                build();

        Medication probiMage = new MedicationBuilder().
                setName("Probi Mage").
                setManufacturer("Probi").
                setForm(DosageForm.CAPSULE).
                setRepeatingStrategy(new Everyday()).
                setRemindingStrategy(new AtCertainHours(Collections.singletonList(new TimedDosage(NinePM, 1)))).
                build();

        Medication manTabletter = new MedicationBuilder().
                setName("Man tabletter").
                setManufacturer("apoteket").
                setForm(DosageForm.TABLET).
                setRepeatingStrategy(new Everyday()).
                setRemindingStrategy(new AtCertainHours(Collections.singletonList(new TimedDosage(NinePM, 1)))).
                build();

        pharmacist.addMedication(valaciclovir);
        pharmacist.addMedication(folacin);
        pharmacist.addMedication(probiMage);
        pharmacist.addMedication(manTabletter);
        Toast.makeText(context, "Medications injected.", Toast.LENGTH_SHORT).show();

        SchedulingService.inCaseNotRunToday(context);
        Toast.makeText(context, "SchedulingService triggered.", Toast.LENGTH_SHORT).show();

    }
}
