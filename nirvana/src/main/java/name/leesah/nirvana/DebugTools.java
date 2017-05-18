package name.leesah.nirvana;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import org.joda.time.LocalTime;

import java.util.Arrays;
import java.util.Collections;

import name.leesah.nirvana.model.medication.DosageForm;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.medication.reminding.CertainHours;
import name.leesah.nirvana.model.medication.repeating.Everyday;
import name.leesah.nirvana.model.medication.starting.Immediately;
import name.leesah.nirvana.model.medication.stopping.Never;
import name.leesah.nirvana.model.reminder.TimedDosage;
import name.leesah.nirvana.ui.reminder.SchedulingService;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static name.leesah.nirvana.PhoneBook.pharmacist;

/**
 * Created by sah on 2017-04-18.
 */

public class DebugTools {

    public static void clearAllData(Context context) {
        getDefaultSharedPreferences(context).edit().clear().apply();
        PhoneBook.fireEveryone();
        ((Activity)context).getFragmentManager().popBackStackImmediate();
    }

    public static void injectTestData(Context context) {
        clearAllData(context);

        LocalTime NineAM = new LocalTime(0).withHourOfDay(9);
        LocalTime NinePM = new LocalTime(0).withHourOfDay(21);

        pharmacist(context).save(new Medication.Builder().
                setName("Valaciclovir").
                setManufacturer("Teva").
                setForm(DosageForm.TABLET).
                setRemindingStrategy(new CertainHours(Arrays.asList(new TimedDosage(NineAM, 1), new TimedDosage(NinePM, 1)))).
                setRepeatingStrategy(new Everyday()).
                setStartingStrategy(new Immediately()).
                setStoppingStrategy(new Never()).
                build());

        pharmacist(context).save(new Medication.Builder().
                setName("Folacin").
                setManufacturer("folsyra").
                setForm(DosageForm.TABLET).
                setRemindingStrategy(new CertainHours(Collections.singletonList(new TimedDosage(NinePM, 1)))).
                setRepeatingStrategy(new Everyday()).
                setStartingStrategy(new Immediately()).
                setStoppingStrategy(new Never()).
                build());

        pharmacist(context).save(new Medication.Builder().
                setName("Man tabletter").
                setManufacturer("apoteket").
                setForm(DosageForm.TABLET).
                setRemindingStrategy(new CertainHours(Collections.singletonList(new TimedDosage(NinePM, 1)))).
                setRepeatingStrategy(new Everyday()).
                setStartingStrategy(new Immediately()).
                setStoppingStrategy(new Never()).
                build());

        SchedulingService.inCaseNotRunToday(context);

        Toast.makeText(context, "Done.", Toast.LENGTH_SHORT).show();

    }
}
