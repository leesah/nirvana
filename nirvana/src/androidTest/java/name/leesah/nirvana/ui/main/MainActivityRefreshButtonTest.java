package name.leesah.nirvana.ui.main;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import name.leesah.nirvana.LanternGenie;
import name.leesah.nirvana.R;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static name.leesah.nirvana.LanternGenie.everythingVanishesSilVousPlait;
import static name.leesah.nirvana.LanternGenie.oneRandomMedicationSilVousPlait;
import static name.leesah.nirvana.LanternGenie.oneRandomReminderOnThisDaySilVousPlait;
import static name.leesah.nirvana.LanternGenie.severalRandomMedicationsSilVousPlait;
import static name.leesah.nirvana.LanternGenie.severalRandomRemindersOnThisDaySilVousPlait;
import static name.leesah.nirvana.ui.MoreMatchers.withAdaptedData;
import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

/**
 * Created by sah on 2017-05-06.
 */
public class MainActivityRefreshButtonTest {

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
    public void refreshMedicationListBySwipingDown() throws Exception {
        onView(withId(R.id.navigation_medications)).perform(click());

        oneRandomMedicationSilVousPlait(getTargetContext(), true);
        onView(withId(R.id.medications)).check(matches(not(withAdaptedData(anything()))));

        onView(withId(R.id.content_main)).perform(swipeDown());
        onData(anything()).inAdapterView(withId(R.id.medications)).atPosition(0).check(matches(isDisplayed()));
    }

    @Test
    public void refreshReminderListBySwipingDown() throws Exception {
        onView(withId(R.id.navigation_reminders)).perform(click());

        oneRandomReminderOnThisDaySilVousPlait(getTargetContext(), today(), true);
        onView(withId(R.id.reminders)).check(matches(not(withAdaptedData(anything()))));

        onView(withId(R.id.content_main)).perform(swipeDown());
        onData(anything()).inAdapterView(withId(R.id.reminders)).atPosition(1).check(matches(isDisplayed()));
    }

    @Test
    public void refreshMedicationListFromMenu() throws Exception {
        onView(withId(R.id.navigation_medications)).perform(click());

        oneRandomMedicationSilVousPlait(getTargetContext(), true);
        onView(withId(R.id.medications)).check(matches(not(withAdaptedData(anything()))));

        openActionBarOverflowOrOptionsMenu(getTargetContext());
        onView(withText(R.string.refresh)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.medications)).atPosition(0).check(matches(isDisplayed()));
    }

    @Test
    public void refreshReminderListFromMenu() throws Exception {
        onView(withId(R.id.navigation_reminders)).perform(click());

        oneRandomReminderOnThisDaySilVousPlait(getTargetContext(), today(), true);
        onView(withId(R.id.reminders)).check(matches(not(withAdaptedData(anything()))));

        openActionBarOverflowOrOptionsMenu(getTargetContext());
        onView(withText(R.string.refresh)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.reminders)).atPosition(1).check(matches(isDisplayed()));
    }

}