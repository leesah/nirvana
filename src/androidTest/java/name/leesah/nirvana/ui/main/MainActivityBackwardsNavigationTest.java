package name.leesah.nirvana.ui.main;

import android.support.test.espresso.NoActivityResumedException;
import android.support.test.rule.ActivityTestRule;
import android.widget.DatePicker;
import android.widget.NumberPicker;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import name.leesah.nirvana.R;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.PreferenceMatchers.withTitle;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static name.leesah.nirvana.ui.LanternGenie.everythingVanishesSilVousPlait;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.fail;

/**
 * Created by sah on 2017-04-20.
 */
public class MainActivityBackwardsNavigationTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @BeforeClass
    public static void beforeClass() throws Exception {
        everythingVanishesSilVousPlait();
    }

    @After
    public void tearDown() throws Exception {
        everythingVanishesSilVousPlait();
    }

    @Test(expected = NoActivityResumedException.class)
    public void main_Reminders_BackOnceToExit() throws Exception {
        onView(withId(R.id.navigation_reminders)).perform(click());

        pressBack();
        fail("Activity should be closed.");
    }

    @Test(expected = NoActivityResumedException.class)
    public void main_Settings_BackOnceToExit() throws Exception {
        onView(withId(R.id.navigation_settings)).perform(click());

        pressBack();
        fail("Activity should be closed.");
    }

    @Test(expected = NoActivityResumedException.class)
    public void main_Settings_TreatmentSettings_AllTheWayBack() throws Exception {
        onView(withId(R.id.navigation_settings)).perform(click());
        {
            onData(withTitle(R.string.pref_title_treatment)).perform(click());
            pressBack();
        }
        pressBack();
        fail("Activity should be closed.");
    }

    @Test(expected = NoActivityResumedException.class)
    public void main_Settings_TreatmentSettings_FirstDate_AllTheWayBack() throws Exception {
        onView(withId(R.id.navigation_settings)).perform(click());
        {
            onData(withTitle(R.string.pref_title_treatment)).perform(click());
            onData(withTitle(R.string.pref_title_treatment_enabled)).perform(click());
            {
                onData(withTitle(R.string.pref_title_treatment_first_day)).perform(click());
                onView(withClassName(is(DatePicker.class.getCanonicalName()))).check(matches(isDisplayed()));
                pressBack();
            }
            pressBack();
        }
        pressBack();
        fail("Activity should be closed.");
    }

    @Test(expected = NoActivityResumedException.class)
    public void main_Settings_TreatmentSettings_CycleLength_AllTheWayBack() throws Exception {
        onView(withId(R.id.navigation_settings)).perform(click());
        {
            onData(withTitle(R.string.pref_title_treatment)).perform(click());
            onData(withTitle(R.string.pref_title_treatment_enabled)).perform(click());
            {
                onData(withTitle(R.string.pref_title_treatment_cycle_length)).perform(click());
                onView(allOf(withClassName(is(NumberPicker.class.getCanonicalName())), withId(R.id.number))).check(matches(isDisplayed()));
                onView(allOf(withClassName(is(NumberPicker.class.getCanonicalName())), withId(R.id.unit))).check(matches(isDisplayed()));
                pressBack();
            }
            pressBack();
        }
        pressBack();
        fail("Activity should be closed.");
    }

    @Test(expected = NoActivityResumedException.class)
    public void main_Settings_TreatmentSettings_RepeatingModels_AllTheWayBack() throws Exception {
        onView(withId(R.id.navigation_settings)).perform(click());
        {
            onData(withTitle(R.string.pref_title_treatment)).perform(click());
            onData(withTitle(R.string.pref_title_treatment_enabled)).perform(click());
            {
                onData(withTitle(R.string.pref_title_treatment_repeating)).perform(click());
                onView(withId(R.id.details_container)).check(matches(not(doesNotExist())));
                pressBack();
            }
            pressBack();
        }
        pressBack();
        fail("Activity should be closed.");
    }

    @Test(expected = NoActivityResumedException.class)
    public void main_Reminders_Settings_FirstDate_Back_CycleLength_Back_RepeatingModels_AllTheWayBack_EverythingAgain() throws Exception {
        onView(withId(R.id.navigation_reminders)).perform(click());
        {
            onView(withId(R.id.navigation_settings)).perform(click());
            { // First round
                onData(withTitle(R.string.pref_title_treatment)).perform(click());
                onData(withTitle(R.string.pref_title_treatment_enabled)).perform(click());
                {
                    onData(withTitle(R.string.pref_title_treatment_first_day)).perform(click());
                    onView(withClassName(is(DatePicker.class.getCanonicalName()))).check(matches(isDisplayed()));
                    pressBack();
                }
                {
                    onData(withTitle(R.string.pref_title_treatment_cycle_length)).perform(click());
                    onView(allOf(withClassName(is(NumberPicker.class.getCanonicalName())), withId(R.id.number))).check(matches(isDisplayed()));
                    onView(allOf(withClassName(is(NumberPicker.class.getCanonicalName())), withId(R.id.unit))).check(matches(isDisplayed()));
                    pressBack();
                }
                {
                    onData(withTitle(R.string.pref_title_treatment_repeating)).perform(click());
                    onView(withId(R.id.details_container)).check(matches(not(doesNotExist())));
                    pressBack();
                }
                pressBack();
            }

            { // All over again
                onView(withId(R.id.navigation_reminders)).perform(click());
                onView(withId(R.id.navigation_settings)).perform(click());
                onData(withTitle(R.string.pref_title_treatment)).perform(click());
                // Except this of course
                // onData(withTitle(R.string.pref_title_treatment_switch)).perform(click());
                {
                    onData(withTitle(R.string.pref_title_treatment_first_day)).perform(click());
                    onView(withClassName(is(DatePicker.class.getCanonicalName()))).check(matches(isDisplayed()));
                    pressBack();
                }
                {
                    onData(withTitle(R.string.pref_title_treatment_cycle_length)).perform(click());
                    onView(allOf(withClassName(is(NumberPicker.class.getCanonicalName())), withId(R.id.number))).check(matches(isDisplayed()));
                    onView(allOf(withClassName(is(NumberPicker.class.getCanonicalName())), withId(R.id.unit))).check(matches(isDisplayed()));
                    pressBack();
                }
                {
                    onData(withTitle(R.string.pref_title_treatment_repeating)).perform(click());
                    onView(withId(R.id.details_container)).check(matches(not(doesNotExist())));
                    pressBack();
                }
                pressBack();
            }
        }
        pressBack();
        fail("Activity should be closed.");
    }

}