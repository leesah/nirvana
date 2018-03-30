package name.leesah.nirvana.ui.medication;

import android.support.test.rule.ActivityTestRule;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import name.leesah.nirvana.R;
import name.leesah.nirvana.ui.MoreMatchers;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.PreferenceMatchers.withTitle;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.util.stream.IntStream.range;
import static name.leesah.nirvana.LanternGenie.everythingVanishes;
import static name.leesah.nirvana.ui.MoreActions.setChecked;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by sah on 2017-04-20.
 */
public abstract class MedicationActivityBackwardsNavigationTest {

    @Rule
    public ActivityTestRule<MedicationActivity> mActivityRule = new ActivityTestRule<>(MedicationActivity.class, false, false);

    @BeforeClass
    public static void beforeClass() throws Exception {
        everythingVanishes(getTargetContext());
    }

    @Test
    public void medication_BackImmediately() throws Exception {
        pressBack();
        onView(withText(android.R.string.ok)).perform(click());
        assertThat(mActivityRule.getActivity().isFinishing(), is(true));
    }

    @Test
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

                onView(MoreMatchers.switchWidgetBesidesTitle(R.string.pref_title_medication_repeating)).perform(setChecked(true));
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
        assertThat(mActivityRule.getActivity().isFinishing(), is(true));
    }

}
