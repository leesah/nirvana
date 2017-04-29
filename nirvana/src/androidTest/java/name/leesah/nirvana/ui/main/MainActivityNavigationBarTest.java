package name.leesah.nirvana.ui.main;

import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import name.leesah.nirvana.R;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static name.leesah.nirvana.LanternGenie.everythingVanishesSilVousPlait;

/**
 * Created by sah on 2017-04-20.
 */
public class MainActivityNavigationBarTest {

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

    @Test
    public void navigate_MedicationList_ReminderList() throws Exception {
        onView(withText(R.string.no_medication)).check(matches(isDisplayed()));
        onView(withText(R.string.no_reminders_for_today)).check(doesNotExist());

        onView(withId(R.id.navigation_reminders)).perform(click());

        onView(withText(R.string.no_medication)).check(doesNotExist());
        onView(withText(R.string.no_reminders_for_today)).check(matches(isDisplayed()));
    }

    @Test
    public void navigate_MedicationList_Settings() throws Exception {
        onView(withText(R.string.no_medication)).check(matches(isDisplayed()));
        onView(withText(R.string.pref_title_treatment)).check(doesNotExist());

        onView(withId(R.id.navigation_settings)).perform(click());

        onView(withText(R.string.no_medication)).check(doesNotExist());
        onView(withText(R.string.pref_title_treatment)).check(matches(isDisplayed()));
    }

    @Test
    public void navigate_MedicationList_ReminderList_Settings_ReminderList() throws Exception {
        onView(withText(R.string.no_medication)).check(matches(isDisplayed()));
        onView(withText(R.string.no_reminders_for_today)).check(doesNotExist());
        onView(withText(R.string.pref_title_treatment)).check(doesNotExist());

        onView(withId(R.id.navigation_reminders)).perform(click());

        onView(withText(R.string.no_medication)).check(doesNotExist());
        onView(withText(R.string.no_reminders_for_today)).check(matches(isDisplayed()));
        onView(withText(R.string.pref_title_treatment)).check(doesNotExist());

        onView(withId(R.id.navigation_settings)).perform(click());

        onView(withText(R.string.no_medication)).check(doesNotExist());
        onView(withText(R.string.no_medication)).check(doesNotExist());
        onView(withText(R.string.pref_title_treatment)).check(matches(isDisplayed()));
    }

}