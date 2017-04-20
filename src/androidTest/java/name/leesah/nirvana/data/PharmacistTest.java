package name.leesah.nirvana.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.ArraySet;

import com.google.gson.Gson;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

import name.leesah.nirvana.model.medication.DosageForm;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.medication.MedicationBuilder;
import name.leesah.nirvana.utils.AdaptedGsonFactory;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.IntStream.range;
import static name.leesah.nirvana.data.Pharmacist.PREFERENCE_KEY_MEDICATIONS;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by sah on 2017-04-06.
 */
public class PharmacistTest {
    public static final int ZERO = 0;
    public static final int RANDOM_INT_MAX_VALUE = 8;
    private final Random random = new Random();

    private Pharmacist pharmacist;
    private SharedPreferences preferences;
    private Gson gson;

    @Before
    public void setUp() throws Exception {
        Context context = getTargetContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        gson = AdaptedGsonFactory.getGson();
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

        Medication newMedication = randomMedication();
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
        Set<Medication> medications = randomMedications(n);
        preferences.edit().putStringSet(PREFERENCE_KEY_MEDICATIONS, medications.stream().map(m -> gson.toJson(m)).collect(toSet())).apply();
        return medications;
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

    private Set<Medication> randomMedications(int n) {
        return range(0, n).mapToObj(x -> randomMedication()).collect(toSet());
    }

    private Medication randomMedication() {
        return new MedicationBuilder()
                .setName(randomString())
                .setManufacturer(randomString())
                .setForm(randomDosageForm())
                .setRemindingStrategy(null)
                .setRepeatingStrategy(null)
                .build();
    }

    private DosageForm randomDosageForm() {
        DosageForm[] dosageForms = EnumSet.allOf(DosageForm.class).toArray(new DosageForm[]{});
        return dosageForms[random.nextInt(dosageForms.length)];
    }

    private int randomNonZeroInteger() {
        return random.nextInt(RANDOM_INT_MAX_VALUE) + 1;
    }

    private String randomString() {
        return RandomStringUtils.random(randomNonZeroInteger());
    }

    private Medication pickOneRandomly(Set<Medication> medications) {
        int i = random.nextInt(medications.size());
        return new ArrayList<>(medications).get(i);
    }

}