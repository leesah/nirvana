package name.leesah.nirvana.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.ArrayMap;
import android.util.Log;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import name.leesah.nirvana.model.medication.Medication;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

/**
 * Created by sah on 2016-12-11.
 */

public class Pharmacist extends DataHolder {

    private static final String TAG = Pharmacist.class.getSimpleName();
    public static final String PREFERENCE_KEY_MEDICATIONS = "name.leesah.nirvana.preferences.key.MEDICATIONS";

    private static Pharmacist instance;
    private ArrayMap<Integer, Medication> cache;

    Pharmacist(Context context) {
        super(context);
    }

    public static Pharmacist getInstance(Context context) {
        if (instance == null)
            instance = new Pharmacist(context);
        return instance;
    }

    @NonNull
    public Set<Medication> getMedications() {
        loadMedicationsCacheIfNeeded();
        return new HashSet<>(cache.values());
    }

    @Nullable
    public Medication getMedication(int id) {
        loadMedicationsCacheIfNeeded();
        return cache.get(id);
    }

    public void save(@NonNull Medication medication) {
        Log.d(TAG, String.format("Saving medication [%s].", medication.getName()));

        loadMedicationsCacheIfNeeded();
        cache.put(medication.getId(), medication);
        persistMedicationsCache();
    }

    public void removeMedication(int id) {
        loadMedicationsCacheIfNeeded();
        Log.d(TAG, String.format("Removing medication [%s].", cache.get(id).getName()));

        cache.remove(id);
        persistMedicationsCache();
    }

    private void loadMedicationsCacheIfNeeded() {
        if (cache != null) {
            Log.d(TAG, String.format("Cache is already loaded with [%d] medication(s): [%s].", cache.size(), itemsInCache()));
            return;
        }

        Set<String> stringSet = preferences.getStringSet(PREFERENCE_KEY_MEDICATIONS, new HashSet<>());
        Map<Integer, Medication> map = stringSet.stream()
                .map(json -> gson.fromJson(json, Medication.class))
                .collect(toMap(Medication::getId, Function.identity()));
        cache = new ArrayMap<>();
        cache.putAll(map);

        Log.i(TAG, String.format("%d medication(s) loaded.", cache.size()));
        Log.d(TAG, String.format("Medication(s) in cache: [%s].", itemsInCache()));
    }

    private void persistMedicationsCache() {
        Log.d(TAG, String.format("Medication(s) in cache: [%s].", itemsInCache()));

        Set<String> medicationsAsStringSet = cache.values().stream()
                .map(gson::toJson)
                .collect(toSet());
        preferences.edit().putStringSet(PREFERENCE_KEY_MEDICATIONS, medicationsAsStringSet).apply();
        Log.i(TAG, String.format("%d medication(s) persisted.", medicationsAsStringSet.size()));
    }

    private String itemsInCache() {
        return cache.values().stream().map(Medication::toString).collect(Collectors.joining(", "));
    }

    public static void reset() {
        instance = null;
    }
}
