package name.leesah.nirvana.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.rule.ActivityTestRule;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import name.leesah.nirvana.LanternGenie;
import name.leesah.nirvana.R;
import name.leesah.nirvana.model.treatment.recurring.NTimes;
import name.leesah.nirvana.model.treatment.recurring.RecurringStrategy;
import name.leesah.nirvana.model.treatment.recurring.UntilDate;

import static android.preference.PreferenceManager.*;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.PickerActions.setDate;
import static android.support.test.espresso.matcher.PreferenceMatchers.isEnabled;
import static android.support.test.espresso.matcher.PreferenceMatchers.withSummary;
import static android.support.test.espresso.matcher.PreferenceMatchers.withSummaryText;
import static android.support.test.espresso.matcher.PreferenceMatchers.withTitle;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static name.leesah.nirvana.LanternGenie.*;
import static name.leesah.nirvana.LanternGenie.cycledTreatmentDisabledSilVousPlait;
import static name.leesah.nirvana.ui.MoreActions.setChecked;
import static name.leesah.nirvana.ui.MoreActions.setNumber;
import static name.leesah.nirvana.ui.MoreMatchers.switchWidgetBesidesTitle;
import static name.leesah.nirvana.utils.AdaptedGsonFactory.getGson;
import static name.leesah.nirvana.utils.DateTimeHelper.toText;
import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.joda.time.Period.*;
import static org.junit.Assert.assertThat;

/**
 * Created by sah on 2017-04-20.
 */
public class MainActivityTreatmentSettingsTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class, false, false);
    private SharedPreferences sharedPreferences;
    private Context context;

    @BeforeClass
    public static void beforeClass() throws Exception {
        everythingVanishesSilVousPlait(getTargetContext());
    }

    @Before
    public void setUp() throws Exception {
        context = getTargetContext();
        cycledTreatmentDisabledSilVousPlait(context);
        sharedPreferences = getDefaultSharedPreferences(context);

        mActivityRule.launchActivity(new Intent());
        onView(withId(R.id.navigation_settings)).perform(click());
        onView(withText(R.string.pref_title_treatment)).perform(click());
    }

    @After
    public void tearDown() throws Exception {
        everythingVanishesSilVousPlait(context);
    }

    @Test
    public void treatmentSupportDisabled() throws Exception {
        onView(switchWidgetBesidesTitle(R.string.pref_title_treatment_enabled))
                .check(matches(isNotChecked()));

        assertThat(
                sharedPreferences.getString(
                        context.getString(R.string.pref_key_treatment_first_day), null),
                is(nullValue()));

        assertThat(
                sharedPreferences.getString(
                        context.getString(R.string.pref_key_treatment_cycle_length), null),
                is(nullValue()));

        assertThat(
                sharedPreferences.getString(
                        context.getString(R.string.pref_key_treatment_recurring), null),
                is(nullValue()));

    }

    @Test
    public void defaultValuesAreValidWhenEnabledForTheFirstTime() throws Exception {
        onView(switchWidgetBesidesTitle(R.string.pref_title_treatment_enabled))
                .perform(setChecked(true));

        assertThat(
                sharedPreferences.getString(
                        context.getString(R.string.pref_key_treatment_first_day), null),
                equalTo(toText(today())));

        assertThat(
                sharedPreferences.getString(
                        context.getString(R.string.pref_key_treatment_cycle_length), null),
                equalTo(toText(weeks(2))));

        assertThat(
                getGson().fromJson(
                        sharedPreferences.getString(
                                context.getString(R.string.pref_key_treatment_recurring), null),
                        RecurringStrategy.class),
                equalTo(new NTimes(1)));

    }

    @Test
    public void treatmentRecursForNTimes() throws Exception {
        onView(switchWidgetBesidesTitle(R.string.pref_title_treatment_enabled))
                .perform(setChecked(true));

        onView(withText(R.string.pref_title_treatment_recurring)).perform(click());

        onView(withText(R.string.pref_title_treatment_recurring_n_times)).perform(click());
        onView(withId(R.id.number_picker)).perform(setNumber(6));
        onView(withText(android.R.string.ok)).perform(click());
        onView(withText(R.string.save)).perform(click());

        assertThat(
                getGson().fromJson(
                        sharedPreferences.getString(
                                context.getString(R.string.pref_key_treatment_recurring), null),
                        RecurringStrategy.class),
                equalTo(new NTimes(6)));

    }

    @Test
    public void treatmentRecursUntilDate() throws Exception {
        onView(switchWidgetBesidesTitle(R.string.pref_title_treatment_enabled))
                .perform(setChecked(true));

        onView(withText(R.string.pref_title_treatment_recurring)).perform(click());
        {
            onView(withText(R.string.pref_title_treatment_recurring)).perform(click());
            onView(withText(R.string.treatment_recurring_until_date)).perform(click());
        }
        onView(withText(R.string.pref_title_treatment_recurring_until_date)).perform(click());
        onView(withId(R.id.date_picker)).perform(setDate(2017, 5, 7));
        onView(withText(android.R.string.ok)).perform(click());
        onView(withText(R.string.save)).perform(click());

        assertThat(
                getGson().fromJson(
                        sharedPreferences.getString(
                                context.getString(R.string.pref_key_treatment_recurring), null),
                        RecurringStrategy.class),
                equalTo(new UntilDate(new LocalDate(0)
                        .withYear(2017)
                        .withMonthOfYear(5)
                        .withDayOfMonth(7))));

    }

}