package name.leesah.nirvana;

import android.content.Context;
import android.widget.Toast;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Arrays;
import java.util.Collections;

import name.leesah.nirvana.data.Nurse;
import name.leesah.nirvana.data.Pharmacist;
import name.leesah.nirvana.data.Therapist;
import name.leesah.nirvana.model.medication.DosageForm;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.medication.MedicationBuilder;
import name.leesah.nirvana.model.medication.reminding.AtCertainHours;
import name.leesah.nirvana.model.medication.repeating.Everyday;
import name.leesah.nirvana.model.medication.starting.Immediately;
import name.leesah.nirvana.model.medication.stopping.Never;
import name.leesah.nirvana.model.reminder.TimedDosage;
import name.leesah.nirvana.ui.reminder.SchedulingService;

import static android.preference.PreferenceManager.*;

/**
 * Created by sah on 2017-04-18.
 */

public class DebugTools {

    public static void clearAllData(Context context) {
        getDefaultSharedPreferences(context).edit().clear().apply();
        Pharmacist.reset();
        Nurse.reset();
        Therapist.reset();
    }

    public static void injectTestData(Context context) {
        clearAllData(context);

        Pharmacist pharmacist = Pharmacist.getInstance(context);

        DateTimeFormatter formatterHHmm = DateTimeFormat.forPattern("HH:mm");
        LocalTime NineAM = LocalTime.parse("09:00", formatterHHmm);
        LocalTime NinePM = LocalTime.parse("21:00", formatterHHmm);

        Medication valaciclovir = new MedicationBuilder().
                setName("Valaciclovir").
                setManufacturer("Teva").
                setForm(DosageForm.TABLET).
                setRepeatingStrategy(new Everyday()).
                setRemindingStrategy(new AtCertainHours(Arrays.asList(new TimedDosage(NineAM, 1), new TimedDosage(NinePM, 1)))).
                setStartingStrategy(new Immediately()).
                setStoppingStrategy(new Never()).
                build();

        Medication folacin = new MedicationBuilder().
                setName("Folacin").
                setManufacturer("folsyra").
                setForm(DosageForm.TABLET).
                setRepeatingStrategy(new Everyday()).
                setRemindingStrategy(new AtCertainHours(Collections.singletonList(new TimedDosage(NinePM, 1)))).
                setStartingStrategy(new Immediately()).
                setStoppingStrategy(new Never()).
                build();

        Medication manTabletter = new MedicationBuilder().
                setName("Man tabletter").
                setManufacturer("apoteket").
                setForm(DosageForm.TABLET).
                setRepeatingStrategy(new Everyday()).
                setRemindingStrategy(new AtCertainHours(Collections.singletonList(new TimedDosage(NinePM, 1)))).
                setStartingStrategy(new Immediately()).
                setStoppingStrategy(new Never()).
                build();

        pharmacist.addMedication(valaciclovir);
        pharmacist.addMedication(folacin);
        pharmacist.addMedication(manTabletter);
        Toast.makeText(context, "Medications injected.", Toast.LENGTH_SHORT).show();

        SchedulingService.inCaseNotRunToday(context);
        Toast.makeText(context, "SchedulingService triggered.", Toast.LENGTH_SHORT).show();

    }
}
