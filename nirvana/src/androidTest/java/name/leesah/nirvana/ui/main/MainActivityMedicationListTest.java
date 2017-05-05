package name.leesah.nirvana.ui.main;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.hamcrest.FeatureMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Set;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.Medication;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static name.leesah.nirvana.LanternGenie.everythingVanishesSilVousPlait;
import static name.leesah.nirvana.LanternGenie.randomPositiveIntSilVousPlait;
import static name.leesah.nirvana.LanternGenie.severalRandomMedicationsSilVousPlait;
import static name.leesah.nirvana.ui.MoreMatchers.ofMedicationWithId;
import static name.leesah.nirvana.ui.MoreMatchers.ofMedicationWithName;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;

/**
 * Created by sah on 2017-04-20.
 */
public class MainActivityMedicationListTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class, false, false);

    private Set<Medication> existing;

    @Before
    public void setUp() throws Exception {
        existing = severalRandomMedicationsSilVousPlait(
                getTargetContext(), randomPositiveIntSilVousPlait(128), true);
        mActivityRule.launchActivity(new Intent());
    }

    @After
    public void tearDown() throws Exception {
        everythingVanishesSilVousPlait(getTargetContext());
    }

    @Test
    public void allMedicationsListed() throws Exception {
        onView(withId(R.id.navigation_medications)).perform(click());
        existing.forEach(
                medication -> onData(allOf(
                        ofMedicationWithId(medication.getId()),
                        ofMedicationWithName(medication.getName())
                )).check(matches(isDisplayed())));
    }

}