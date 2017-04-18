package name.leesah.nirvana.ui.medication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.core.deps.guava.collect.Iterables;
import android.support.test.espresso.core.deps.guava.collect.Sets;
import android.support.test.rule.ActivityTestRule;
import android.widget.EditText;

import org.joda.time.LocalTime;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.util.Set;

import name.leesah.nirvana.R;
import name.leesah.nirvana.data.Nurse;
import name.leesah.nirvana.data.Pharmacist;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.medication.reminding.AtCertainHours;
import name.leesah.nirvana.model.medication.repeating.Everyday;
import name.leesah.nirvana.model.reminder.Reminder;
import name.leesah.nirvana.model.reminder.TimedDosage;
import name.leesah.nirvana.ui.reminder.RemindingService;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.util.stream.Collectors.toSet;
import static name.leesah.nirvana.model.medication.DosageForm.TABLET;
import static name.leesah.nirvana.ui.MoreViewActions.setNumber;
import static name.leesah.nirvana.ui.reminder.RemindingService.ACTION_SHOW_REMINDER;
import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by sah on 2017-04-06.
 */
public class MedicationEditActivityTest {
    private static final TimedDosage MORNING_DOSE = new TimedDosage(new LocalTime(0).withHourOfDay(9).withMinuteOfHour(15), 1);
    private static final TimedDosage EVENING_DOSE = new TimedDosage(new LocalTime(0).withHourOfDay(21).withMinuteOfHour(15), 1);
    @Rule
    public ActivityTestRule<MedicationEditActivity> mActivityRule = new ActivityTestRule<>(MedicationEditActivity.class);

    @BeforeClass
    public static void beforeClass() throws Exception {
        purgePreferences();
    }

    @After
    public void tearDown() throws Exception {
        cancelAllAlarms();
        purgePreferences();
    }

    @Test
    public void certainHoursEveryday() throws Exception {
        typeIntoEditTextPreference(R.string.pref_title_medication_name, "Valaciclovir");
        typeIntoEditTextPreference(R.string.pref_title_medication_manufacturer, "Bluefish");

        onView(withText(R.string.pref_title_dosage_form)).perform(click());
        onView(withText(R.string.dosage_form_tablets)).perform(click());

        onView(withText(R.string.pref_title_medication_repeating_model)).perform(click());
        onView(withText(R.string.pref_title_medication_repeating_model)).perform(click());
        onView(withText(R.string.medication_repeating_everyday)).perform(click());
        clickSaveFab();

        onView(withText(R.string.pref_title_medication_reminding_model)).perform(click());
        onView(withText(R.string.pref_title_medication_reminding_model)).perform(click());
        onView(withText(R.string.medication_reminding_certain_hours)).perform(click());

        addDosage(MORNING_DOSE);
        addDosage(EVENING_DOSE);
        clickSaveFab();

        clickSaveFab();

        Set<Medication> medications = Pharmacist.getInstance(getTargetContext()).getMedications();
        assertThat(medications.size(), is(1));

        Medication valaciclovir = Iterables.getOnlyElement(medications);
        assertThat(valaciclovir.getName(), equalTo("Valaciclovir"));
        assertThat(valaciclovir.getManufacturer(), equalTo("Bluefish"));
        assertThat(valaciclovir.getForm(), equalTo(TABLET));
        assertThat(valaciclovir.getRepeatingModel(), instanceOf(Everyday.class));
        assertThat(valaciclovir.getRemindingModel(), instanceOf(AtCertainHours.class));

        Set<Reminder> reminders = Nurse.getInstance(getTargetContext()).getReminders(today());
        assertThat(reminders.size(), is(2));

        reminders.forEach(r -> assertThat(r.getMedicationId(), is(valaciclovir.getId())));
        Set<TimedDosage> dosages = reminders.stream().map(r -> new TimedDosage(r.getTime(), r.getDosageAmount())).collect(toSet());
        assertThat(dosages, equalTo(Sets.newHashSet(MORNING_DOSE, EVENING_DOSE)));
    }

    @Test @Ignore
    public void everyNHoursEveryday() throws Exception {
        typeIntoEditTextPreference(R.string.pref_title_medication_name, "Valaciclovir");
        typeIntoEditTextPreference(R.string.pref_title_medication_manufacturer, "Bluefish");

        onView(withText(R.string.pref_title_dosage_form)).perform(click());
        onView(withText(R.string.dosage_form_tablets)).perform(click());

        onView(withText(R.string.pref_title_medication_repeating_model)).perform(click());
        onView(withText(R.string.pref_title_medication_repeating_model)).perform(click());
        onView(withText(R.string.medication_repeating_everyday)).perform(click());
        clickSaveFab();

        onView(withText(R.string.pref_title_medication_reminding_model)).perform(click());
        onView(withText(R.string.pref_title_medication_reminding_model)).perform(click());
        onView(withText(R.string.medication_reminding_every_n_hours)).perform(click());

        onView(withText(R.id.every_n)).perform(click());
        onView(withClassName(is(EditText.class.getName()))).perform(typeText("6"));
        clickSaveFab();

        clickSaveFab();

        Set<Medication> medications = Pharmacist.getInstance(getTargetContext()).getMedications();
        assertThat(medications.size(), is(1));

        Medication valaciclovir = Iterables.getOnlyElement(medications);
        assertThat(valaciclovir.getName(), equalTo("Valaciclovir"));
        assertThat(valaciclovir.getManufacturer(), equalTo("Bluefish"));
        assertThat(valaciclovir.getForm(), equalTo(TABLET));
        assertThat(valaciclovir.getRepeatingModel(), instanceOf(Everyday.class));
        assertThat(valaciclovir.getRemindingModel(), instanceOf(AtCertainHours.class));

        Set<Reminder> reminders = Nurse.getInstance(getTargetContext()).getReminders(today());
        assertThat(reminders.size(), is(2));

        reminders.forEach(r -> assertThat(r.getMedicationId(), is(valaciclovir.getId())));
        Set<TimedDosage> dosages = reminders.stream().map(r -> new TimedDosage(r.getTime(), r.getDosageAmount())).collect(toSet());
        assertThat(dosages, equalTo(Sets.newHashSet(MORNING_DOSE, EVENING_DOSE)));
    }

    private void typeIntoEditTextPreference(int resIdOfPrefTitle, String text) {
        onView(withText(resIdOfPrefTitle)).perform(click());
        onView(withClassName(is(EditText.class.getName()))).perform(typeText(text));
        clickOk();
    }

    private void addDosage(TimedDosage dose) {
        onView(allOf(withId(R.id.numberPicker), withEffectiveVisibility(VISIBLE))).perform(setNumber(dose.getAmount()));
        onView(allOf(withId(R.id.timePicker), withEffectiveVisibility(VISIBLE))).perform(PickerActions.setTime(dose.getTimeOfDay().getHourOfDay(), dose.getTimeOfDay().getMinuteOfHour()));
        onView(allOf(withText(R.string.add), withEffectiveVisibility(VISIBLE))).perform(click());
    }

    private void clickSaveFab() {
        onView(allOf(withId(R.id.save_button), withClassName(endsWith(FloatingActionButton.class.getSimpleName())))).perform(click());
    }

    private void clickOk() {
        onView(withText(android.R.string.ok)).perform(click());
    }

    private void cancelAllAlarms() {
        Intent intent = new Intent(getTargetContext(), RemindingService.class)
                .setAction(ACTION_SHOW_REMINDER);
        PendingIntent pendingIntent = PendingIntent.getService(getTargetContext(), 0, intent, 0);
        getTargetContext().getSystemService(AlarmManager.class).cancel(pendingIntent);
    }

    private static void purgePreferences() {
        PreferenceManager.getDefaultSharedPreferences(getTargetContext()).edit().clear().apply();
    }

}