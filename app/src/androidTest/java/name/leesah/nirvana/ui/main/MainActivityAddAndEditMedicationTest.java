package name.leesah.nirvana.ui.main;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.Medication;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static name.leesah.nirvana.LanternGenie.everythingVanishes;
import static name.leesah.nirvana.LanternGenie.randomMedication;

/**
 * Created by sah on 2017-04-20.
 */
public class MainActivityAddAndEditMedicationTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class, false, false);

    private Medication existing;

    @Before
    public void setUp() throws Exception {
        existing = randomMedication(getTargetContext(), true);
        mActivityRule.launchActivity(new Intent());
    }

    @After
    public void tearDown() throws Exception {
        everythingVanishes(getTargetContext());
    }

    @Test
    public void addMedicationAndBack() throws Exception {
        onView(withId(R.id.navigation_medications)).perform(click());

        onView(withId(R.id.add_button)).perform(click());
        onView(withText(R.string.pref_title_medication_name)).check(matches(isDisplayed()));

        pressBack();
        onView(withText(android.R.string.ok)).perform(click());
        onView(withText(R.string.pref_title_medication_name)).check(doesNotExist());
    }

    @Test
    public void editMedicationAndBack() throws Exception {
        onView(withId(R.id.navigation_medications)).perform(click());

        onView(withText(existing.getName())).perform(click());
        onView(withText(R.string.edit)).perform(click());
        onView(withText(R.string.pref_title_medication_name)).check(matches(isDisplayed()));

        pressBack();
        onView(withText(android.R.string.ok)).perform(click());
        onView(withText(R.string.pref_title_medication_name)).check(doesNotExist());
    }
}