package name.leesah.nirvana.ui.medication;

import android.content.Intent;
import android.support.test.espresso.NoActivityResumedException;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import name.leesah.nirvana.R;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.PickerActions.setTime;
import static android.support.test.espresso.matcher.PreferenceMatchers.withTitle;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility.GONE;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static name.leesah.nirvana.ui.LanternGenie.everythingVanishesSilVousPlait;
import static name.leesah.nirvana.ui.MoreViewActions.setNumber;
import static name.leesah.nirvana.ui.medication.MedicationActivity.ACTION_ADD_MEDICATION;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.fail;

/**
 * Created by sah on 2017-04-20.
 */
public class MedicationActivityBackwardsNavigationInAddModeTest extends MedicationActivityBackwardsNavigationTest {

    @Before
    public void setUp() throws Exception {
        mActivityRule.launchActivity(new Intent(ACTION_ADD_MEDICATION));
    }

    @After
    public void tearDown() throws Exception {
        everythingVanishesSilVousPlait();
    }

}