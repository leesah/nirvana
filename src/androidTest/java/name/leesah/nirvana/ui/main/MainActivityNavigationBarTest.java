package name.leesah.nirvana.ui.main;

import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.Medication;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.PreferenceMatchers.withTitle;
import static android.support.test.espresso.matcher.PreferenceMatchers.withTitleText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static name.leesah.nirvana.ui.LanternGenie.eraseEverything;
import static name.leesah.nirvana.ui.LanternGenie.oneRandomMedicationSilVousPlait;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Created by sah on 2017-04-20.
 */
public class MainActivityNavigationBarTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @BeforeClass
    public static void beforeClass() throws Exception {
        eraseEverything();
    }

    @After
    public void tearDown() throws Exception {
        eraseEverything();
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