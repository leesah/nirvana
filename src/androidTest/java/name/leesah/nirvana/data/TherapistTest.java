package name.leesah.nirvana.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.treatment.Treatment;
import name.leesah.nirvana.model.treatment.TreatmentCycle;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by sah on 2016-12-14.
 */
public class TherapistTest {
    private Context targetContext;
    private SharedPreferences preferences;
    private Therapist therapist;

    @Before
    public void setUp() throws Exception {
        targetContext = getTargetContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(targetContext);
        therapist = Therapist.getInstance(targetContext);
    }

    @Test
    public void getEverlastingTreatment() throws Exception {
        preferences.edit().putBoolean(targetContext.getResources().getString(R.string.pref_key_treatment_enabled), false).apply();
        Treatment treatment = therapist.getTreatment();
        assertThat(treatment, is(not(nullValue())));

        LocalDate randomFutureDate = today().plusDays(new Random().nextInt(365));
        TreatmentCycle cycle = treatment.getRepeatingModel().getCycleForDate(treatment.getFirstCycle(), randomFutureDate);
        assertThat(cycle, is(not(nullValue())));
        assertThat(cycle.contains(randomFutureDate), is(true));
    }

}