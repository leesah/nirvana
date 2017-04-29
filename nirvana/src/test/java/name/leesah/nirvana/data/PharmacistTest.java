package name.leesah.nirvana.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.ArraySet;

import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import name.leesah.nirvana.BuildConfig;
import name.leesah.nirvana.model.medication.Medication;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;
import static name.leesah.nirvana.LanternGenie.oneRandomMedicationSilVousPlait;
import static name.leesah.nirvana.LanternGenie.severalRandomMedicationsSilVousPlait;
import static name.leesah.nirvana.data.Pharmacist.PREFERENCE_KEY_MEDICATIONS;
import static name.leesah.nirvana.utils.AdaptedGsonFactory.getGson;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.robolectric.RuntimeEnvironment.*;

/**
 * Created by sah on 2017-04-06.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = {24, 25})
public class PharmacistTest {
    public static final int ZERO = 0;
    public static final int RANDOM_INT_MAX_VALUE = 8;
    private final Random random = new Random();

    private Pharmacist pharmacist;
    private SharedPreferences preferences;
    private Gson gson;
    private Context context;

    @Before
    public void setUp() throws Exception {
        context = application.getApplicationContext();
        preferences = getDefaultSharedPreferences(context);
        gson = getGson();
        pharmacist = new Pharmacist(context);
    }

    @After
    public void tearDown() throws Exception {
        preferences.edit().remove(PREFERENCE_KEY_MEDICATIONS).apply();
    }

    @Test
    public void getMedications() throws Exception {
        Set<Medication> givenExisting = givenExistingMedications(randomNonZeroInteger());

        Set<Medication> retrieved = pharmacist.getMedications();

        thenMedicationsAreTheSameAsGiven(givenExisting, retrieved);
    }

    @Test
    public void getMedication() throws Exception {
        Set<Medication> givenExisting = givenExistingMedications(randomNonZeroInteger());

        Medication picked = pickOneRandomly(givenExisting);
        Medication retrieved = pharmacist.getMedication(picked.getId());

        assertThat(retrieved.getName(), equalTo(picked.getName()));
    }

    @Test
    public void addMedication() throws Exception {
        Set<Medication> givenExisting = givenExistingMedications(randomNonZeroInteger());

        Medication newMedication = oneRandomMedicationSilVousPlait(context, false);
        pharmacist.addMedication(newMedication);

        Set<Medication> all = new ArraySet<>();
        all.addAll(givenExisting);
        all.add(newMedication);

        thenSavedMedicationsAre(all);
    }

    @Test
    public void removeMedication() throws Exception {
        Set<Medication> givenExisting = givenExistingMedications(randomNonZeroInteger());

        int badId = pickOneRandomly(givenExisting).getId();
        pharmacist.removeMedication(badId);

        Set<Medication> all = givenExisting.stream()
                .filter(medication -> medication.getId() != badId)
                .collect(toSet());

        thenSavedMedicationsAre(all);
    }

    private Set<Medication> givenExistingMedications(int n) {
        return severalRandomMedicationsSilVousPlait(context, n, true);
    }

    private void thenMedicationsAreTheSameAsGiven(Set<Medication> expected, Set<Medication> actual) {
        Set<Integer> expectedIds = expected.stream().map(Medication::getId).collect(toSet());
        Set<Integer> actualIds = actual.stream().map(Medication::getId).collect(toSet());
        assertThat(actualIds, equalTo(expectedIds));
    }

    private void thenSavedMedicationsAre(Set<Medication> medications) {
        Set<Integer> allSavedIds = preferences.getStringSet(PREFERENCE_KEY_MEDICATIONS, emptySet()).stream()
                .map(s -> gson.fromJson(s, Medication.class).getId())
                .collect(toSet());
        assertThat(medications.stream().map(Medication::getId).collect(toSet()), equalTo(allSavedIds));
    }


    private int randomNonZeroInteger() {
        return random.nextInt(RANDOM_INT_MAX_VALUE) + 1;
    }

    private Medication pickOneRandomly(Set<Medication> medications) {
        int i = random.nextInt(medications.size());
        return new ArrayList<>(medications).get(i);
    }

}