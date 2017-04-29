package name.leesah.nirvana.ui.medication;

import android.support.test.espresso.NoActivityResumedException;
import android.support.test.rule.ActivityTestRule;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import name.leesah.nirvana.R;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.PickerActions.setTime;
import static android.support.test.espresso.matcher.PreferenceMatchers.withTitle;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static name.leesah.nirvana.LanternGenie.everythingVanishesSilVousPlait;
import static name.leesah.nirvana.ui.NumberPickerActions.setNumber;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.fail;

/**
 * Created by sah on 2017-04-20.
 */
//TODO: Find a way to make pressBack() work on EditTextPreferences.
public abstract class MedicationActivityBackwardsNavigationTest {

    @Rule
    public ActivityTestRule<MedicationActivity> mActivityRule = new ActivityTestRule<>(MedicationActivity.class, false, false);

    @BeforeClass
    public static void beforeClass() throws Exception {
        everythingVanishesSilVousPlait(getTargetContext());
    }

    @Ignore
    @Test(expected = NoActivityResumedException.class)
    public void medication_BackImmediately() throws Exception {
        pressBack();
        fail("Activity should be closed.");
    }

    @Ignore
    @Test(expected = NoActivityResumedException.class)
    public void medication_EditName_AllTheWayBack() throws Exception {
        {
            onData(withTitle(R.string.pref_title_medication_name)).perform(click());
            onView(withText(android.R.string.cancel)).perform(click());
        }
        pressBack();
        fail("Activity should be closed.");
    }

    @Ignore
    @Test(expected = NoActivityResumedException.class)
    public void medication_EditName_Back_EditManufacturer_AllTheWayBack() throws Exception {
        {
            onData(withTitle(R.string.pref_title_medication_manufacturer)).perform(click());
            onView(withText(android.R.string.cancel)).perform(click());
        }
        pressBack();
        fail("Activity should be closed.");
    }

    @Test(expected = NoActivityResumedException.class)
    @Ignore("Not yet implemented.")
    public void medication_EditName_Back_EditManufacturer_Back_EditForm_AllTheWayBack() throws Exception {
        {
            onData(withTitle(R.string.pref_title_medication_name)).perform(click());
            onView(withText(android.R.string.cancel)).perform(click());
        }
        {
            onData(withTitle(R.string.pref_title_medication_manufacturer)).perform(click());
            onView(withText(android.R.string.cancel)).perform(click());
        }
        pressBack();
        fail("Activity should be closed.");
    }

    @Ignore
    @Test(expected = NoActivityResumedException.class)
    public void medication_EditName_Back_EditManufacturer_Back_EditForm_Back_EditRepeating_Back_EditReminding_AllTheWayBack() throws Exception {
        {
            onData(withTitle(R.string.pref_title_medication_name)).perform(click());
            onView(withText(android.R.string.cancel)).perform(click());
        }
        {
            onData(withTitle(R.string.pref_title_medication_manufacturer)).perform(click());
            onView(withText(android.R.string.cancel)).perform(click());
        }
        {
            onData(withTitle(R.string.pref_title_medication_repeating)).perform(click());
            pressBack();
        }
        {
            onData(withTitle(R.string.pref_title_medication_reminding)).perform(click());
            pressBack();
        }
        pressBack();
        fail("Activity should be closed.");
    }

    @Test(expected = NoActivityResumedException.class)
    public void medication_EditName_Back_EditManufacturer_Back_EditForm_Back_EditRepeating_Back_EditReminding_EverythingAgain_AllTheWayBack() throws Exception {
        { // First round
            {
                onData(withTitle(R.string.pref_title_medication_name)).perform(click());
                onView(withText(android.R.string.cancel)).perform(click());
            }
            {
                onData(withTitle(R.string.pref_title_medication_manufacturer)).perform(click());
                onView(withText(android.R.string.cancel)).perform(click());
            }
            {
                onData(withTitle(R.string.pref_title_medication_repeating)).perform(click());
                pressBack();
            }
            {
                onData(withTitle(R.string.pref_title_medication_reminding)).perform(click());
                pressBack();
            }
        }
        { // All over again
            {
                onData(withTitle(R.string.pref_title_medication_name)).perform(click());
                onView(withText(android.R.string.cancel)).perform(click());
            }
            {
                onData(withTitle(R.string.pref_title_medication_manufacturer)).perform(click());
                onView(withText(android.R.string.cancel)).perform(click());
            }
            {
                onData(withTitle(R.string.pref_title_medication_repeating)).perform(click());
                pressBack();
            }
            {
                onData(withTitle(R.string.pref_title_medication_reminding)).perform(click());
                pressBack();
            }
        }
        pressBack();
        fail("Activity should be closed.");
    }

    @Ignore
    @Test(expected = NoActivityResumedException.class)
    public void medication_EditRepeatingModel_SelectModel_AllTheWayBack() throws Exception {
        {
            onData(withTitle(R.string.pref_title_medication_repeating)).perform(click());
            {
                onData(withTitle(R.string.pref_title_medication_repeating)).perform(click());
                pressBack();
            }
            pressBack();
        }
        pressBack();
        fail("Activity should be closed.");
    }

    @Test(expected = NoActivityResumedException.class)
    @Ignore("Not yet implemented.")
    public void medication_EditRepeatingModel_SelectModel_InputDetails_AllTheWayBack() throws Exception {
        {
            onData(withTitle(R.string.pref_title_medication_repeating)).perform(click());
            {
                onData(withTitle(R.string.pref_title_medication_repeating)).perform(click());
                onData((withText(R.string.medication_repeating_every_n_days))).perform(click());
                {
                    onData(withTitle(R.string.pref_title_medication_reminding)).perform(click());
                    pressBack();
                }
            }
            pressBack();
        }
        pressBack();
        fail("Activity should be closed.");
    }

    @Ignore
    @Test(expected = NoActivityResumedException.class)
    public void medication_EditRemindingModel_SelectModel_AllTheWayBack() throws Exception {
        {
            onData(withTitle(R.string.pref_title_medication_reminding)).perform(click());
            {
                onData(withTitle(R.string.pref_title_medication_reminding)).perform(click());
                pressBack();
            }
            pressBack();
        }
        pressBack();
        fail("Activity should be closed.");
    }

    @Ignore
    @Test(expected = NoActivityResumedException.class)
    public void medication_EditRemindingModel_SelectModel_InputDetails_AllTheWayBack() throws Exception {
        {
            onData(withTitle(R.string.pref_title_medication_reminding)).perform(click());
            {
                onData(withTitle(R.string.pref_title_medication_reminding)).perform(click());
                onView(withText(R.string.medication_reminding_certain_hours)).perform(click());
                onView(allOf(withId(R.id.numberPicker), withEffectiveVisibility(VISIBLE))).perform(setNumber(1));
                onView(allOf(withId(R.id.timePicker), withEffectiveVisibility(VISIBLE))).perform(setTime(9, 0));
                onView(allOf(withText(R.string.add), withEffectiveVisibility(VISIBLE))).perform(click());
                {
                    onData(withTitle(R.string.pref_title_medication_reminding)).inAdapterView(withId(android.R.id.list)).perform(click());
                    pressBack();
                }
            }
            pressBack();
        }
        pressBack();
        fail("Activity should be closed.");
    }
}
