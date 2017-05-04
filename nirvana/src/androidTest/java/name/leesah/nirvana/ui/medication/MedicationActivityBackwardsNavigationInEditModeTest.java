package name.leesah.nirvana.ui.medication;

import android.content.Intent;

import org.junit.After;
import org.junit.Before;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static name.leesah.nirvana.LanternGenie.everythingVanishesSilVousPlait;
import static name.leesah.nirvana.LanternGenie.oneRandomMedicationSilVousPlait;
import static name.leesah.nirvana.ui.medication.MedicationActivity.ACTION_EDIT_MEDICATION;

/**
 * Created by sah on 2017-04-20.
 */
public class MedicationActivityBackwardsNavigationInEditModeTest extends MedicationActivityBackwardsNavigationTest{

    @Before
    public void setUp() throws Exception {
        everythingVanishesSilVousPlait(getTargetContext());
        int id = oneRandomMedicationSilVousPlait(getTargetContext(), true).getId();
        mActivityRule.launchActivity(new Intent(ACTION_EDIT_MEDICATION));
    }

    @After
    public void tearDown() throws Exception {
        everythingVanishesSilVousPlait(getTargetContext());
    }

}