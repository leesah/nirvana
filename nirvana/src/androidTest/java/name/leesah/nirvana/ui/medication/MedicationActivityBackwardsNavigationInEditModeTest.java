package name.leesah.nirvana.ui.medication;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static name.leesah.nirvana.LanternGenie.everythingVanishesSilVousPlait;
import static name.leesah.nirvana.LanternGenie.oneRandomMedicationSilVousPlait;
import static name.leesah.nirvana.ui.medication.MedicationActivity.ACTION_EDIT_MEDICATION;
import static name.leesah.nirvana.ui.medication.MedicationActivity.EXTRA_MEDICATION_ID;

/**
 * Created by sah on 2017-04-20.
 */
public class MedicationActivityBackwardsNavigationInEditModeTest {

    @Rule
    public ActivityTestRule<MedicationActivity> mActivityRule = new ActivityTestRule<>(MedicationActivity.class, false, false);

    @Before
    public void setUp() throws Exception {
        everythingVanishesSilVousPlait(getTargetContext());
        int id = oneRandomMedicationSilVousPlait(getTargetContext(), true).getId();
        mActivityRule.launchActivity(new Intent(ACTION_EDIT_MEDICATION).putExtra(EXTRA_MEDICATION_ID, id));
    }

    @After
    public void tearDown() throws Exception {
        everythingVanishesSilVousPlait(getTargetContext());
    }

}