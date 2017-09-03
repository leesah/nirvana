package name.leesah.nirvana.ui.medication;

import android.support.test.espresso.PerformException;
import android.support.test.rule.ActivityTestRule;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;

import org.joda.time.LocalDate;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.Set;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.medication.reminding.CertainHours;
import name.leesah.nirvana.model.medication.repeating.WithInterval;
import name.leesah.nirvana.model.medication.starting.ExactDate;
import name.leesah.nirvana.model.medication.stopping.InPeriod;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.PickerActions.setDate;
import static android.support.test.espresso.contrib.PickerActions.setTime;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.google.common.collect.Iterables.getOnlyElement;
import static name.leesah.nirvana.LanternGenie.everythingVanishes;
import static name.leesah.nirvana.LanternGenie.treatmentDisabled;
import static name.leesah.nirvana.PhoneBook.pharmacist;
import static name.leesah.nirvana.model.PeriodUnit.MONTH;
import static name.leesah.nirvana.model.medication.DosageForm.TABLET;
import static name.leesah.nirvana.ui.MoreActions.setChecked;
import static name.leesah.nirvana.ui.MoreActions.setNumber;
import static name.leesah.nirvana.ui.MoreMatchers.switchWidgetBesidesTitle;
import static name.leesah.nirvana.ui.widget.PeriodPicker.PERIOD_UNITS;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.joda.time.Period.months;
import static org.junit.Assert.assertThat;

/**
 * Created by sah on 2017-04-20.
 */
public abstract class MedicationActivityDataOperationsTest {

    @Rule
    public ActivityTestRule<MedicationActivity> mActivityRule = new ActivityTestRule<>(MedicationActivity.class, false, false);

    @BeforeClass
    public static void beforeClass() throws Exception {
        everythingVanishes(getTargetContext());
    }

    @Test
    public void saveButtonIsPresent() throws Exception {
        onView(withText(R.string.save)).check(matches(withEffectiveVisibility(VISIBLE)));
    }

    @Test
    public void everydayCertainHours() throws Exception {
        // Cycled treatment disabled
        treatmentDisabled(getTargetContext());

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
            onView(withText(R.string.tablet)).perform(click());
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
                onView(allOf(withId(R.id.number_picker), withEffectiveVisibility(VISIBLE)))
                        .perform(setNumber(1), closeSoftKeyboard());
                onView(allOf(withId(R.id.timePicker), withEffectiveVisibility(VISIBLE)))
                        .perform(setTime(9, 15), closeSoftKeyboard());
                onView(allOf(withText(R.string.add), withEffectiveVisibility(VISIBLE))).perform(click());
            }
            { // Add 1 dose at 21:15
                onView(allOf(withId(R.id.number_picker), withEffectiveVisibility(VISIBLE)))
                        .perform(setNumber(1), closeSoftKeyboard());
                onView(allOf(withId(R.id.timePicker), withEffectiveVisibility(VISIBLE)))
                        .perform(setTime(21, 15), closeSoftKeyboard());
                onView(allOf(withText(R.string.add), withEffectiveVisibility(VISIBLE))).perform(click());
            }
            onView(withText(R.string.save)).perform(click());
        }
        { // Edit repeating model
            onView(switchWidgetBesidesTitle(R.string.pref_title_medication_repeating)).perform(setChecked(true));
            onView(withText(R.string.pref_title_medication_repeating)).perform(click());
            { // Select repeating model: Every n days
                onView(withText(R.string.pref_title_medication_repeating)).perform(click());
                onView(withText(R.string.medication_repeating_every_n_days)).perform(click());
                { // Set every 4 days
                    onView(withText(R.string.pref_title_medication_repeating_every_n_days)).perform(click());
                    onView(withClassName(equalTo(NumberPicker.class.getCanonicalName())))
                            .perform(setNumber(4), closeSoftKeyboard());
                    onView(withText(android.R.string.ok)).perform(click());
                }
                onView(withText(R.string.save)).perform(click());
            }
        }
        { // Edit starting model
            onView(switchWidgetBesidesTitle(R.string.pref_title_medication_starting)).perform(setChecked(true));
            onView(withText(R.string.pref_title_medication_starting)).perform(click());
            { // Set start on 2016-08-10
                onView(withClassName(endsWith(DatePicker.class.getSimpleName()))).perform(setDate(2016, 8, 10));
                onView(withText(android.R.string.ok)).perform(click());
            }
        }
        { // Edit stopping model
            onView(switchWidgetBesidesTitle(R.string.pref_title_medication_stopping)).perform(setChecked(true));
            onView(withText(R.string.pref_title_medication_stopping)).perform(click());
            onView(withId(R.id.number)).perform(setNumber(6), closeSoftKeyboard());
            onView(withId(R.id.unit)).perform(setNumber(PERIOD_UNITS.indexOf(MONTH)));
            onView(withText(android.R.string.ok)).perform(click());
        }
        // Save
        onView(withText(R.string.save)).perform(click());

        // Verify: 1 medication found at pharmacist's
        Set<Medication> medications = pharmacist(getTargetContext()).getMedications();
        assertThat(medications.size(), is(1));

        // Verify: medication information
        Medication medication = getOnlyElement(medications);
        assertThat(medication.getName(), equalTo("Valaciclovir"));
        assertThat(medication.getManufacturer(), equalTo("Bluefish"));
        assertThat(medication.getForm(), equalTo(TABLET));
        assertThat(medication.getRepeatingStrategy(), equalTo(new WithInterval(4)));
        assertThat(medication.getRemindingStrategy(), instanceOf(CertainHours.class));
        assertThat(medication.getStartingStrategy(), equalTo(new ExactDate(new LocalDate(0).withYear(2016).withMonthOfYear(8).withDayOfMonth(10))));
        assertThat(medication.getStoppingStrategy(), equalTo(new InPeriod(months(6))));
    }

}
