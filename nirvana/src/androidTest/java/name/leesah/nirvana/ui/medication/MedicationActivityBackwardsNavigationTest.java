package name.leesah.nirvana.ui.medication;

import android.support.test.espresso.NoActivityResumedException;
import android.support.test.rule.ActivityTestRule;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import name.leesah.nirvana.R;
import name.leesah.nirvana.ui.MoreViewMatchers;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.PreferenceMatchers.withTitle;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.util.stream.IntStream.range;
import static name.leesah.nirvana.LanternGenie.everythingVanishesSilVousPlait;
import static name.leesah.nirvana.ui.MoreViewActions.setChecked;
import static org.junit.Assert.fail;

/**
 * Created by sah on 2017-04-20.
 */
public abstract class MedicationActivityBackwardsNavigationTest {

    @Rule
    public ActivityTestRule<MedicationActivity> mActivityRule = new ActivityTestRule<>(MedicationActivity.class, false, false);

    @BeforeClass
    public static void beforeClass() throws Exception {
        everythingVanishesSilVousPlait(getTargetContext());
    }

    @Test(expected = NoActivityResumedException.class)
    public void medication_BackImmediately() throws Exception {
        pressBack();
        onView(withText(android.R.string.ok)).perform(click());
        fail("Activity should be closed.");
    }

    @Test(expected = NoActivityResumedException.class)
    public void medication_NavigationAmongFragments() throws Exception {
        range(0, 2).forEach(x ->
        { //
            { // In basics fragment
                onData(withTitle(R.string.pref_title_medication_reminding)).perform(click());
                { // In reminding strategy selecting fragment
                    onView(withText(R.string.pref_title_medication_reminding)).perform(click());
                    onView(withText(R.string.medication_reminding_certain_hours)).perform(click());
                    { // In certain hours strategy editing fragment
                        // onView(withClassName(equalTo(TimedDosageEditorCard.class.getCanonicalName()))).check(matches(isDisplayed()));
                    }
                    pressBack();
                }

                onView(MoreViewMatchers.switchWidgetBesidesTitle(R.string.pref_title_medication_repeating)).perform(setChecked(true));
                onData(withTitle(R.string.pref_title_medication_repeating)).perform(click());
                { // In repeating strategy selecting fragment
                    onView(withText(R.string.pref_title_medication_repeating)).perform(click());
                    onView(withText(R.string.medication_repeating_every_n_days)).perform(click());
                    { // In every n days strategy editing fragment
                        // onData(withKey(getTargetContext().getString(R.string.pref_key_medication_repeating_every_n_days))).check(matches(isDisplayed()));
                    }
                    pressBack();
                }
            }
        });
        pressBack();
        onView(withText(android.R.string.ok)).perform(click());
        fail("Activity should be closed.");
    }

}
