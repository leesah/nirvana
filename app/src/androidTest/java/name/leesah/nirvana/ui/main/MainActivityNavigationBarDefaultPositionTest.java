package name.leesah.nirvana.ui.main;

import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import name.leesah.nirvana.R;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
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

public class MainActivityNavigationBarDefaultPositionTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class, false, false);

    @BeforeClass
    public static void beforeClass() throws Exception {
        everythingVanishes(getTargetContext());
    }

    @After
    public void tearDown() throws Exception {
        everythingVanishes(getTargetContext());
    }

    @Test
    public void defaultToMedicationListWhenZeroMedicationExists() throws Exception {
        mActivityRule.launchActivity(null);

        onView(withText(R.string.no_medication)).check(matches(isDisplayed()));
        onView(withText(R.string.no_reminders_for_today)).check(doesNotExist());
        onView(withId(R.id.reminders)).check(doesNotExist());
        onView(withText(R.string.pref_title_treatment)).check(doesNotExist());
    }

    @Test
    public void defaultToReminderListOnWhenOneMedicationExists() throws Exception {
        randomMedication(getTargetContext(), true);

        mActivityRule.launchActivity(null);

        onView(withText(R.string.no_reminders_for_today)).check(matches(isDisplayed()));
        onView(withId(R.id.medications)).check(doesNotExist());
        onView(withText(R.string.pref_title_treatment)).check(doesNotExist());
    }

}
