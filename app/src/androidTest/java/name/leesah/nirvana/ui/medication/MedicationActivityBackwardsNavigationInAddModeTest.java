package name.leesah.nirvana.ui.medication;

import android.content.Intent;

import org.junit.After;
import org.junit.Before;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static name.leesah.nirvana.LanternGenie.everythingVanishes;
import static name.leesah.nirvana.ui.medication.MedicationActivity.ACTION_ADD_MEDICATION;

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
        everythingVanishes(getTargetContext());
    }

}