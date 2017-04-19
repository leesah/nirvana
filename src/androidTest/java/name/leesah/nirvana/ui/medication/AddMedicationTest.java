package name.leesah.nirvana.ui.medication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.test.espresso.core.deps.guava.collect.Sets;
import android.support.test.rule.ActivityTestRule;
import android.widget.EditText;

import org.joda.time.LocalTime;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Set;

import name.leesah.nirvana.R;
import name.leesah.nirvana.data.Nurse;
import name.leesah.nirvana.data.Pharmacist;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.medication.reminding.AtCertainHours;
import name.leesah.nirvana.model.medication.reminding.EveryNHours;
import name.leesah.nirvana.model.medication.reminding.RemindingModel;
import name.leesah.nirvana.model.medication.repeating.Everyday;
import name.leesah.nirvana.model.medication.repeating.RepeatingModel;
import name.leesah.nirvana.model.reminder.Reminder;
import name.leesah.nirvana.model.reminder.TimedDosage;
import name.leesah.nirvana.ui.MainActivity;
import name.leesah.nirvana.ui.reminder.RemindingService;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.contrib.PickerActions.setTime;
import static android.support.test.espresso.core.deps.guava.collect.Iterables.getOnlyElement;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.util.stream.Collectors.toSet;
import static name.leesah.nirvana.model.medication.DosageForm.TABLET;
import static name.leesah.nirvana.model.medication.reminding.EveryNHours.TWELVE;
import static name.leesah.nirvana.model.medication.reminding.EveryNHours.VALID_VALUES;
import static name.leesah.nirvana.ui.MoreViewActions.setNumber;
import static name.leesah.nirvana.ui.reminder.RemindingService.ACTION_SHOW_REMINDER;
import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by sah on 2017-04-06.
 */
public class AddMedicationTest {
    private static final TimedDosage MORNING_DOSE = new TimedDosage(new LocalTime(0).withHourOfDay(9).withMinuteOfHour(15), 1);
    private static final TimedDosage EVENING_DOSE = new TimedDosage(new LocalTime(0).withHourOfDay(21).withMinuteOfHour(15), 1);
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);
    private Pharmacist pharmacist;
    private Nurse nurse;

    @BeforeClass
    public static void beforeClass() throws Exception {
        purgePreferences();
    }

    @Before
    public void setUp() throws Exception {
        pharmacist = Pharmacist.getInstance(getTargetContext());
        nurse = Nurse.getInstance(getTargetContext());
    }

    @After
    public void tearDown() throws Exception {
        cancelAllAlarms();
        Pharmacist.reset();
        Nurse.reset();
        purgePreferences();
    }

    @Test
    public void everydayCertainHours() throws Exception {
        startAddingMedication();
        inputBasics();
        selectEveryday();
        selectCertainHours();
        clickSaveFab();

        Medication valaciclovir = verifyOneMedicationCreated(Everyday.class, AtCertainHours.class);
        verifyTwoRemindersCreated(valaciclovir);
    }

    @Test
    public void everydayEvery8Hours() throws Exception {
        startAddingMedication();
        inputBasics();
        selectEveryday();
        selectEveryNHours();
        clickSaveFab();

        Medication valaciclovir = verifyOneMedicationCreated(Everyday.class, EveryNHours.class);
        verifyTwoRemindersCreated(valaciclovir);
    }

    private void startAddingMedication() {
        onView(withId(R.id.navigation_medications)).perform(click());
        onView(withId(R.id.add_button)).perform(click());
    }

    private void inputBasics() {
        typeIntoEditTextPreference(R.string.pref_title_medication_name, "Valaciclovir");
        typeIntoEditTextPreference(R.string.pref_title_medication_manufacturer, "Bluefish");

        onView(withText(R.string.pref_title_dosage_form)).perform(click());
        onView(withText(R.string.dosage_form_tablets)).perform(click());
    }

    private void typeIntoEditTextPreference(int prefTitle, String text) {
        onView(withText(prefTitle)).perform(click());
        onView(withClassName(is(EditText.class.getName()))).perform(typeText(text));
        clickOk();
    }

    private void selectEveryday() {
        onView(withText(R.string.pref_title_medication_repeating_model)).perform(click());
        onView(withText(R.string.pref_title_medication_repeating_model)).perform(click());
        onView(withText(R.string.medication_repeating_everyday)).perform(click());
        clickSaveFab();
    }

    private void selectCertainHours() {
        onView(withText(R.string.pref_title_medication_reminding_model)).perform(click());
        onView(withText(R.string.pref_title_medication_reminding_model)).perform(click());
        onView(withText(R.string.medication_reminding_certain_hours)).perform(click());
        addDosage(MORNING_DOSE);
        addDosage(EVENING_DOSE);
        clickSaveFab();
    }

    private void selectEveryNHours() {
        onView(withText(R.string.pref_title_medication_reminding_model)).perform(click());
        onView(withText(R.string.pref_title_medication_reminding_model)).perform(click());
        onView(withText(R.string.medication_reminding_every_n_hours)).perform(click());

        onView(withId(R.id.amount)).perform(setNumber(1));
        onView(withId(R.id.every_n)).perform(setNumber(Arrays.asList(VALID_VALUES).indexOf(TWELVE)));
        onView(withId(R.id.first_dose_time)).perform(setTime(9, 15));
        clickSaveFab();
    }

    private void addDosage(TimedDosage dose) {
        onView(allOf(withId(R.id.numberPicker), withEffectiveVisibility(VISIBLE))).perform(setNumber(dose.getAmount()));
        onView(allOf(withId(R.id.timePicker), withEffectiveVisibility(VISIBLE))).perform(setTime(dose.getTimeOfDay().getHourOfDay(), dose.getTimeOfDay().getMinuteOfHour()));
        onView(allOf(withText(R.string.add), withEffectiveVisibility(VISIBLE))).perform(click());
    }

    private void clickSaveFab() {
        onView(allOf(withId(R.id.save_button), withClassName(endsWith(FloatingActionButton.class.getSimpleName())))).perform(click());
    }

    private void clickOk() {
        onView(withText(android.R.string.ok)).perform(click());
    }

    private void verifyTwoRemindersCreated(Medication valaciclovir) {
        Set<Reminder> reminders = nurse.getReminders(today());
        assertThat(reminders.size(), is(2));

        reminders.forEach(r -> assertThat(r.getMedicationId(), is(valaciclovir.getId())));
        Set<TimedDosage> dosages = reminders.stream().map(r -> new TimedDosage(r.getTime(), r.getDosageAmount())).collect(toSet());
        assertThat(dosages, equalTo(Sets.newHashSet(MORNING_DOSE, EVENING_DOSE)));
    }

    @NonNull
    private Medication verifyOneMedicationCreated(Class repeatingModelClass, Class remindingModelClass) {
        Set<Medication> medications = pharmacist.getMedications();
        assertThat(medications.size(), is(1));

        Medication valaciclovir = getOnlyElement(medications);
        assertThat(valaciclovir.getName(), equalTo("Valaciclovir"));
        assertThat(valaciclovir.getManufacturer(), equalTo("Bluefish"));
        assertThat(valaciclovir.getForm(), equalTo(TABLET));
        assertThat(valaciclovir.getRepeatingModel(), instanceOf(repeatingModelClass));
        assertThat(valaciclovir.getRemindingModel(), instanceOf(remindingModelClass));
        return valaciclovir;
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