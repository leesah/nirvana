package name.leesah.nirvana.ui.medication;

import android.support.design.widget.FloatingActionButton;
import android.support.test.espresso.PerformException;
import android.support.test.rule.ActivityTestRule;
import android.widget.EditText;

import org.joda.time.LocalTime;
import org.junit.BeforeClass;
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

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.PickerActions.setTime;
import static android.support.test.espresso.core.deps.guava.collect.Iterables.getOnlyElement;
import static android.support.test.espresso.core.deps.guava.collect.Sets.newHashSet;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility.GONE;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.util.stream.Collectors.toSet;
import static name.leesah.nirvana.model.medication.DosageForm.TABLET;
import static name.leesah.nirvana.ui.LanternGenie.everythingVanishesSilVousPlait;
import static name.leesah.nirvana.ui.MoreViewActions.setNumber;
import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;

/**
 * Created by sah on 2017-04-20.
 */

abstract class MedicationActivityDataOperationsTest {

    @Rule
    public ActivityTestRule<MedicationActivity> mActivityRule = new ActivityTestRule<>(MedicationActivity.class, false, false);

    @BeforeClass
    public static void beforeClass() throws Exception {
        everythingVanishesSilVousPlait();
    }

    @Test
    public void saveButtonIsPresent() throws Exception {
        onView(withId(R.id.save_button)).check(matches(withEffectiveVisibility(VISIBLE)));
    }

    @Test
    public void deleteButtonIsAbsentInRemindingModel() throws Exception {
        onView(withText(R.string.pref_title_medication_reminding)).perform(click());
        onView(withId(R.id.delete_button)).check(matches(withEffectiveVisibility(GONE)));
    }

    @Test
    public void deleteButtonIsAbsentInRepeatingModel() throws Exception {
        onView(withText(R.string.pref_title_medication_repeating)).perform(click());
        onView(withId(R.id.delete_button)).check(matches(withEffectiveVisibility(GONE)));
    }

    @Test
    public void everydayCertainHours() throws Exception {
        { // Input name: Valaciclovir
            onView(withText(R.string.pref_title_medication_name)).perform(click());
            onView(withClassName(is(EditText.class.getName()))).perform(clearText());
            onView(withClassName(is(EditText.class.getName()))).perform(typeText("Valaciclovir"));
            onView(withText(android.R.string.ok)).perform(click());
        }
        { // Input manufacturer: Bluefish
            onView(withText(R.string.pref_title_medication_manufacturer)).perform(click());
            onView(withClassName(is(EditText.class.getName()))).perform(clearText());
            onView(withClassName(is(EditText.class.getName()))).perform(typeText("Bluefish"));
            onView(withText(android.R.string.ok)).perform(click());
        }
        { // Select form: Tablets
            onView(withText(R.string.pref_title_dosage_form)).perform(click());
            onView(withText(TABLET.getName(getTargetContext()))).perform(click());
        }
        { // Edit repeating model
            onView(withText(R.string.pref_title_medication_repeating)).perform(click());
            { // Select repeating model: Everyday
                onView(withText(R.string.pref_title_medication_repeating)).perform(click());
                onView(withText(R.string.medication_repeating_everyday)).perform(click());
                onView(allOf(withId(R.id.save_button), withClassName(endsWith(FloatingActionButton.class.getSimpleName())))).perform(click());
            }
        }
        { // Edit reminding model
            onView(withText(R.string.pref_title_medication_reminding)).perform(click());
            { // Select reminding model: At certain hours
                onView(withText(R.string.pref_title_medication_reminding)).perform(click());
                onView(withText(R.string.medication_reminding_certain_hours)).perform(click());
            }
            { // Delete existing doses, if any
                while (true) {
                    try {
                        onData(anything()).inAdapterView(withId(R.id.dosages)).atPosition(0).perform(click());
                        onView(allOf(withText(R.string.delete), withEffectiveVisibility(VISIBLE))).perform(click());
                    } catch (PerformException e) {
                        break;
                    }
                }
            }
            { // Add 1 dose at 9:15
                onView(allOf(withId(R.id.numberPicker), withEffectiveVisibility(VISIBLE))).perform(setNumber(1));
                onView(allOf(withId(R.id.timePicker), withEffectiveVisibility(VISIBLE))).perform(setTime(9, 15));
                onView(allOf(withText(R.string.add), withEffectiveVisibility(VISIBLE))).perform(click());
            }
            { // Add 1 dose at 21:15
                onView(allOf(withId(R.id.numberPicker), withEffectiveVisibility(VISIBLE))).perform(setNumber(1));
                onView(allOf(withId(R.id.timePicker), withEffectiveVisibility(VISIBLE))).perform(setTime(21, 15));
                onView(allOf(withText(R.string.add), withEffectiveVisibility(VISIBLE))).perform(click());
            }
            onView(allOf(withId(R.id.save_button), withClassName(endsWith(FloatingActionButton.class.getSimpleName())))).perform(click());
        }
        // Save
        onView(allOf(withId(R.id.save_button), withClassName(endsWith(FloatingActionButton.class.getSimpleName())))).perform(click());

        // Verify: 1 medication found at pharmacist's
        Set<Medication> medications = Pharmacist.getInstance(getTargetContext()).getMedications();
        assertThat(medications.size(), is(1));

        // Verify: medication information
        Medication medication = getOnlyElement(medications);
        assertThat(medication.getName(), equalTo("Valaciclovir"));
        assertThat(medication.getManufacturer(), equalTo("Bluefish"));
        assertThat(medication.getForm(), equalTo(TABLET));
        assertThat(medication.getRepeatingStrategy(), instanceOf(Everyday.class));
        assertThat(medication.getRemindingStrategy(), instanceOf(AtCertainHours.class));

        // Verify: 2 reminders found at nurse's
        Set<Reminder> reminders = Nurse.getInstance(getTargetContext()).getReminders(today());
        assertThat(reminders.size(), is(2));

        // Verify: reminders information
        reminders.forEach(r -> assertThat(r.getMedicationId(), is(medication.getId())));
        Set<TimedDosage> dosages = reminders.stream().map(r -> new TimedDosage(r.getTime(), r.getDosageAmount())).collect(toSet());
        assertThat(dosages, equalTo(newHashSet(
                new TimedDosage(new LocalTime(0).withHourOfDay(9).withMinuteOfHour(15), 1),
                new TimedDosage(new LocalTime(0).withHourOfDay(21).withMinuteOfHour(15), 1))));
    }
}
