package name.leesah.nirvana.ui.medication;

import android.content.Intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.Medication;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.PreferenceMatchers.withSummaryText;
import static android.support.test.espresso.matcher.PreferenceMatchers.withTitle;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static name.leesah.nirvana.LanternGenie.everythingVanishes;
import static name.leesah.nirvana.LanternGenie.oneRandomMedicationSilVousPlait;
import static name.leesah.nirvana.ui.medication.MedicationActivity.ACTION_EDIT_MEDICATION;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.assertThat;

/**
 * Created by sah on 2017-04-20.
 */
public class MedicationActivityDataEditTest extends MedicationActivityDataOperationsTest {

    private Medication existing;

    @Before
    public void setUp() throws Exception {
        everythingVanishes(getTargetContext());
        existing = oneRandomMedicationSilVousPlait(getTargetContext(), true);
        MedicationActivity.writeToStaged(getTargetContext(), existing);
        mActivityRule.launchActivity(new Intent(ACTION_EDIT_MEDICATION));
    }

    @After
    public void tearDown() throws Exception {
        everythingVanishes(getTargetContext());
    }

    @Test
    public void titleSaysEdit() throws Exception {
        assertThat(mActivityRule.getActivity().getTitle().toString(), containsString(existing.getName()));
    }

    @Test
    public void preferencesAreFilledWithExistingValues() throws Exception {
        { // Basics
            onData(allOf(
                    withTitle(R.string.pref_title_medication_name),
                    withSummaryText(existing.getName()))
            ).check(matches(isDisplayed()));
            onData(allOf(
                    withTitle(R.string.pref_title_medication_manufacturer),
                    withSummaryText(existing.getManufacturer() == null ? isEmptyString() : equalTo(existing.getManufacturer())))
            ).check(matches(isDisplayed()));
            onData(allOf(
                    withTitle(R.string.pref_title_dosage_form),
                    withSummaryText(containsString(existing.getForm().getName(getTargetContext()))))
            ).check(matches(isDisplayed()));
            onData(allOf(
                    withTitle(R.string.pref_title_medication_repeating),
                    withSummaryText(existing.getRepeatingStrategy().toString(getTargetContext())))
            ).check(matches(isDisplayed()));
            onData(allOf(
                    withTitle(R.string.pref_title_medication_reminding),
                    withSummaryText(existing.getRemindingStrategy().toString(getTargetContext()))
            )).check(matches(isDisplayed()));
        }
        // TODO: verify the values in the strategies.
    }

}