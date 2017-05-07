package name.leesah.nirvana.ui.main;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Set;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.reminder.Reminder;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static name.leesah.nirvana.LanternGenie.everythingVanishes;
import static name.leesah.nirvana.LanternGenie.randomPositiveIntSilVousPlait;
import static name.leesah.nirvana.LanternGenie.severalRandomReminders;
import static name.leesah.nirvana.ui.MoreMatchers.ofReminderWithId;
import static name.leesah.nirvana.ui.MoreMatchers.ofReminderWithMedicationId;
import static name.leesah.nirvana.ui.MoreMatchers.withAdaptedData;
import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anyOf;
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
        remindersToday = (severalRandomReminders(
                getTargetContext(), randomPositiveIntSilVousPlait(8),
                today(), true));
        remindersYesterday = (severalRandomReminders(
                getTargetContext(), randomPositiveIntSilVousPlait(8),
                today().minusDays(1), true));

        mActivityRule.launchActivity(new Intent());
    }

    @After
    public void tearDown() throws Exception {
        everythingVanishes(getTargetContext());
    }

    @Test
    public void allRemindersOfTodayListed() throws Exception {
        onView(withId(R.id.navigation_reminders)).perform(click());
        remindersToday.forEach(
                reminder -> onData(allOf(
                        ofReminderWithId(reminder.getId()),
                        ofReminderWithMedicationId(reminder.getMedicationId())
                )).inAdapterView(withId(R.id.reminders)).check(matches(isDisplayed())));
    }

    @Test
    public void allRemindersOfYesterdayNotListed() throws Exception {
        onView(withId(R.id.navigation_reminders)).perform(click());

        remindersYesterday.forEach(
                reminder -> onView(withId(R.id.reminders))
                        .check(matches(not(
                                withAdaptedData(ofReminderWithId(reminder.getId()))))));
    }

}