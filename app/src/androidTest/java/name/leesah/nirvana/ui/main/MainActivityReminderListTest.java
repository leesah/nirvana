package name.leesah.nirvana.ui.main;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Set;

import name.leesah.nirvana.LanternGenie;
import name.leesah.nirvana.R;
import name.leesah.nirvana.model.reminder.Reminder;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static name.leesah.nirvana.LanternGenie.everythingVanishes;
import static name.leesah.nirvana.ui.MoreMatchers.isCardContaining;
import static name.leesah.nirvana.ui.MoreMatchers.withAdaptedData;
import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static org.hamcrest.CoreMatchers.not;

/**
 * Created by sah on 2017-04-20.
 */
public class MainActivityReminderListTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class, false, false);

    private Set<Reminder> remindersToday;
    private Set<Reminder> remindersYesterday;

    @Before
    public void setUp() throws Exception {
        remindersToday = (LanternGenie.randomReminders(
                getTargetContext(),
                true, today()));
        remindersYesterday = (LanternGenie.randomReminders(
                getTargetContext(),
                true, today().minusDays(1)));

        mActivityRule.launchActivity(new Intent());
    }

    @After
    public void tearDown() throws Exception {
        everythingVanishes(getTargetContext());
    }

    @Test
    public void allRemindersOfTodayListed() throws Exception {
        onView(withId(R.id.navigation_reminders)).perform(click());
        remindersToday.forEach(reminder -> onView(withId(R.id.reminders))
                .check(matches(withAdaptedData(isCardContaining(reminder)))));
    }

    @Test
    public void allRemindersOfYesterdayNotListed() throws Exception {
        onView(withId(R.id.navigation_reminders)).perform(click());
        remindersYesterday.forEach(reminder -> onView(withId(R.id.reminders))
                .check(matches(not(withAdaptedData(isCardContaining(reminder))))));
    }

}