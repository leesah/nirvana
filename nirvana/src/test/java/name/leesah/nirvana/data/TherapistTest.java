package name.leesah.nirvana.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import name.leesah.nirvana.BuildConfig;
import name.leesah.nirvana.R;
import name.leesah.nirvana.model.treatment.EverlastingTreatment;
import name.leesah.nirvana.model.treatment.RecurringTreatment;
import name.leesah.nirvana.model.treatment.recurring.NTimes;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static name.leesah.nirvana.DateTimeRelatedTestHelper.randomDay;
import static name.leesah.nirvana.utils.DateTimeHelper.toText;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.joda.time.Period.months;
import static org.junit.Assert.assertThat;
import static org.robolectric.RuntimeEnvironment.application;

/**
 * Created by sah on 2016-12-14.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = {24, 25})
public class TherapistTest {
    private SharedPreferences preferences;
    private Therapist therapist;
    private Resources resources;
    private Context context;

    @Before
    public void setUp() throws Exception {
        context = application.getApplicationContext();
        resources = context.getResources();
        preferences = getDefaultSharedPreferences(context);
        therapist = Therapist.getInstance(context);
    }

    @After
    public void tearDown() throws Exception {
        preferences.edit().clear().apply();
        Therapist.reset();
    }

    @Test
    public void loadEverlastingFromEmptyCache() throws Exception {
        assertThat(therapist.isCycleSupportEnabled(), is(false));
        assertThat(therapist.getTreatment(), instanceOf(EverlastingTreatment.class));

        EverlastingTreatment treatment = (EverlastingTreatment) therapist.getTreatment();
        assertThat(treatment.getDayZero(), equalTo(new LocalDate(0)));
    }

    @Test
    public void loadRecurringFromCache() throws Exception {
        LocalDate dayZero = randomDay();
        Period length = months(6);
        int n = 4;

        preferences.edit().putBoolean(resources.getString(R.string.pref_key_treatment_enabled), true).apply();
        preferences.edit().putString(resources.getString(R.string.pref_key_treatment_first_day), toText(dayZero)).apply();
        preferences.edit().putString(resources.getString(R.string.pref_key_treatment_cycle_length), toText(length)).apply();
        preferences.edit().putString(resources.getString(R.string.pref_key_treatment_recurring), resources.getString(R.string.treatment_recurring_n_times)).apply();
        preferences.edit().putInt(resources.getString(R.string.pref_key_treatment_recurring_n_times), n).apply();

        assertThat(therapist.isCycleSupportEnabled(), is(true));
        assertThat(therapist.getTreatment(), instanceOf(RecurringTreatment.class));

        RecurringTreatment treatment = (RecurringTreatment) therapist.getTreatment();
        assertThat(treatment.getDayZero(), equalTo(dayZero));
        assertThat(treatment.getLength(), equalTo(length));
        assertThat(treatment.getRecurringStrategy(), instanceOf(NTimes.class));

        NTimes strategy = (NTimes) treatment.getRecurringStrategy();
        assertThat(strategy.getN(), equalTo(n));
    }
}