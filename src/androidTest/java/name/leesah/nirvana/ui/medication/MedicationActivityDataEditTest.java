package name.leesah.nirvana.ui.medication;

import android.content.Intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.medication.reminding.AtCertainHours;
import name.leesah.nirvana.model.medication.reminding.EveryNHours;
import name.leesah.nirvana.model.medication.repeating.DaysOfWeek;
import name.leesah.nirvana.model.medication.repeating.EveryNDays;
import name.leesah.nirvana.model.medication.repeating.Everyday;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.PreferenceMatchers.withSummary;
import static android.support.test.espresso.matcher.PreferenceMatchers.withSummaryText;
import static android.support.test.espresso.matcher.PreferenceMatchers.withTitle;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static name.leesah.nirvana.ui.LanternGenie.everythingVanishesSilVousPlait;
import static name.leesah.nirvana.ui.LanternGenie.oneRandomMedicationSilVousPlait;
import static name.leesah.nirvana.ui.medication.MedicationActivity.ACTION_EDIT_MEDICATION;
import static name.leesah.nirvana.ui.medication.MedicationActivity.EXTRA_MEDICATION_ID;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.*;

/**
 * Created by sah on 2017-04-20.
 */
public class MedicationActivityDataEditTest extends MedicationActivityDataOperationsTest {

    private Medication existing;

    @Before
    public void setUp() throws Exception {
        everythingVanishesSilVousPlait();
        existing = oneRandomMedicationSilVousPlait();
        mActivityRule.launchActivity(new Intent(ACTION_EDIT_MEDICATION).putExtra(EXTRA_MEDICATION_ID, existing.getId()));
    }

    @After
    public void tearDown() throws Exception {
        everythingVanishesSilVousPlait();
    }

    @Test
    public void titleSaysEdit() throws Exception {
        assertThat(mActivityRule.getActivity().getTitle().toString(), containsString(existing.getName()));
    }

    @Test
    public void deleteButtonIsAbsent() throws Exception {
        onView(withId(R.id.delete_button)).check(matches(withEffectiveVisibility(VISIBLE)));
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
        { // Repeating model
            onData(withTitle(R.string.pref_title_medication_repeating)).perform(click());
            int expectedModelName = 0;
            if (existing.getRepeatingStrategy() instanceof Everyday)
                expectedModelName = R.string.medication_repeating_everyday;
            else if (existing.getRepeatingStrategy() instanceof EveryNDays)
                expectedModelName = R.string.medication_repeating_every_n_days;
            else if (existing.getRepeatingStrategy() instanceof DaysOfWeek)
                expectedModelName = R.string.medication_repeating_days_of_week;
            onData(allOf(
                    withTitle(R.string.pref_title_medication_repeating),
                    withSummary(expectedModelName))
            ).check(matches(isDisplayed()));
            pressBack();
        }
        { // Reminding model
            onData(withTitle(R.string.pref_title_medication_reminding)).perform(click());
            int expectedModelName = 0;
            if (existing.getRemindingStrategy() instanceof AtCertainHours)
                expectedModelName = R.string.medication_reminding_certain_hours;
            else if (existing.getRemindingStrategy() instanceof EveryNHours)
                expectedModelName = R.string.medication_reminding_every_n_hours;
            onData(allOf(
                    withTitle(R.string.pref_title_medication_reminding),
                    withSummary(expectedModelName))
            ).check(matches(isDisplayed()));
            pressBack();
        }
    }
}