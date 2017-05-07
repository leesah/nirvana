package name.leesah.nirvana.ui.medication;

import android.content.Intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import name.leesah.nirvana.R;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static name.leesah.nirvana.LanternGenie.everythingVanishes;
import static name.leesah.nirvana.ui.medication.MedicationActivity.ACTION_ADD_MEDICATION;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by sah on 2017-04-06.
 */
public class MedicationActivityDataAddTest extends MedicationActivityDataOperationsTest {

    @Before
    public void setUp() throws Exception {
        mActivityRule.launchActivity(new Intent(ACTION_ADD_MEDICATION));
    }

    @After
    public void tearDown() throws Exception {
        everythingVanishes(getTargetContext());
    }

    @Test
    public void titleSaysAdd() throws Exception {
        assertThat(mActivityRule.getActivity().getTitle(), equalTo(getTargetContext().getString(R.string.add_medication)));
    }

}