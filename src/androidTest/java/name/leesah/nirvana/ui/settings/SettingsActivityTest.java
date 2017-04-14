package name.leesah.nirvana.ui.settings;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import name.leesah.nirvana.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Created by sah on 2016-12-14.
 */
public class SettingsActivityTest {

    @Rule
    public ActivityTestRule<SettingsActivity> activityTestRule = new ActivityTestRule<>(SettingsActivity.class);

    @Before
    public void setUp() throws Exception {

    }

    @Test
    @Ignore
    public void testTreatmentDefaultValues() {
        onView(withText(R.string.pref_key_treatment_first_day)).check(matches(not(isDisplayed())));
        onView(withId(R.xml.pref_treatment)).perform(click());
        onView(withText(R.string.pref_key_treatment_first_day)).check(matches(isDisplayed()));
    }

}