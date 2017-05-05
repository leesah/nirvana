package name.leesah.nirvana.ui.main;

import android.support.test.espresso.NoActivityResumedException;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import name.leesah.nirvana.R;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.PreferenceMatchers.withTitle;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static name.leesah.nirvana.LanternGenie.everythingVanishesSilVousPlait;
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
        everythingVanishesSilVousPlait(getTargetContext());
    }

    @After
    public void tearDown() throws Exception {
        everythingVanishesSilVousPlait(getTargetContext());
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
    public void main_Settings_TreatmentSettings_Recurring_AllTheWayBack() throws Exception {
        onView(withId(R.id.navigation_settings)).perform(click());
        {
            onData(withTitle(R.string.pref_title_treatment)).perform(click());
            onData(withTitle(R.string.pref_title_treatment_enabled)).perform(click());
            {
                onData(withTitle(R.string.pref_title_treatment_recurring)).perform(click());
                onView(withId(R.id.details_container)).check(matches(not(doesNotExist())));
                pressBack();
            }
            pressBack();
        }
        pressBack();
        fail("Activity should be closed.");
    }

}