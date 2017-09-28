package name.leesah.nirvana.ui.main;

import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.reminder.Reminder;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.util.stream.IntStream.range;
import static name.leesah.nirvana.LanternGenie.everythingVanishes;
import static name.leesah.nirvana.LanternGenie.randomMedication;
import static name.leesah.nirvana.LanternGenie.randomReminder;
import static name.leesah.nirvana.ui.MoreMatchers.isCardContaining;
import static name.leesah.nirvana.ui.MoreMatchers.withAdaptedData;
import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;

/**
 * Created by sah on 2017-05-06.
 */
public class MainActivitySwipeRefreshTest {

    public static final int ROUNDS_TO_TEST = 2;
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @BeforeClass
    public static void beforeClass() throws Exception {
        everythingVanishes(getTargetContext());
    }

    @After
    public void tearDown() throws Exception {
        everythingVanishes(getTargetContext());
    }

    @Test
    public void refreshMedicationListBySwipingDown() throws Exception {
        onView(withId(R.id.navigation_medications)).perform(click());

        range(0, ROUNDS_TO_TEST).forEach(i -> {
            Medication medication = randomMedication(getTargetContext(), true);
            onView(withId(R.id.medications))
                    .check(matches(not(withAdaptedData(equalTo(medication)))));

            onView(withId(R.id.content_main)).perform(swipeDown());
            onView(withId(R.id.medications))
                    .check(matches(withAdaptedData(equalTo(medication))));
        });
    }

    @Test
    public void refreshReminderListBySwipingDown() throws Exception {
        onView(withId(R.id.navigation_reminders)).perform(click());

        range(0, ROUNDS_TO_TEST).forEach(i -> {
            Reminder reminder = randomReminder(getTargetContext(), true, today());
            onView(withId(R.id.reminders))
                    .check(matches(not(withAdaptedData(isCardContaining(reminder)))));

            onView(withId(R.id.content_main)).perform(swipeDown());
            onView(withId(R.id.reminders))
                    .check(matches(withAdaptedData(isCardContaining(reminder))));
        });
    }

    @Test
    public void refreshMedicationListFromMenu() throws Exception {
        onView(withId(R.id.navigation_medications)).perform(click());

        range(0, ROUNDS_TO_TEST).forEach(i -> {
            Medication medication = randomMedication(getTargetContext(), true);
            onView(withId(R.id.medications))
                    .check(matches(not(withAdaptedData(equalTo(medication)))));

            openActionBarOverflowOrOptionsMenu(getTargetContext());
            onView(withText(R.string.refresh)).perform(click());
            onView(withId(R.id.medications))
                    .check(matches(withAdaptedData(equalTo(medication))));
        });
    }

    @Test
    public void refreshReminderListFromMenu() throws Exception {
        onView(withId(R.id.navigation_reminders)).perform(click());

        range(0, ROUNDS_TO_TEST).forEach(i -> {
            Reminder reminder = randomReminder(getTargetContext(), true, today());
            onView(withId(R.id.reminders))
                    .check(matches(not(withAdaptedData(isCardContaining(reminder)))));

            openActionBarOverflowOrOptionsMenu(getTargetContext());
            onView(withText(R.string.refresh)).perform(click());
            onView(withId(R.id.reminders))
                    .check(matches(withAdaptedData(isCardContaining(reminder))));
        });
    }

}