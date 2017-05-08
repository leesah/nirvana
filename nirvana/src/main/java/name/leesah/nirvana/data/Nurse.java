package name.leesah.nirvana.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.ArrayMap;
import android.util.Log;

import com.google.common.collect.MoreCollectors;

import org.joda.time.LocalDate;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import name.leesah.nirvana.model.reminder.Reminder;

import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.Locale.US;
import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

/**
 * Created by sah on 2016-12-11.
 */
public class Nurse extends DataHolder {

    private static final String TAG = Nurse.class.getSimpleName();
    public static final String PREFERENCE_KEY_REMINDERS = "name.leesah.nirvana.pref:key:REMINDERS";

    private Map<Integer, Reminder> cache = null;

    public Nurse(Context context) {
        super(context);
    }

    public void add(Reminder reminder) {
        replace(reminder::equals, singleton(reminder));
    }

    public void add(Set<Reminder> reminders) {
        replace(reminders::contains, reminders);
    }

    public Set<Reminder> replace(Predicate<Reminder> isDeprecated, Set<Reminder> reminders) {
        loadCacheIfNeeded();

        synchronized (this) {
            Set<Reminder> deprecated = deprecate(isDeprecated);

            Map<Integer, Reminder> newEntries = reminders.stream().collect(toMap(Reminder::getId, Function.identity()));
            Log.d(TAG, String.format("Putting [%d] reminder(s) to cache: [%s]", newEntries.size(), Arrays.toString(newEntries.keySet().toArray())));

            cache.putAll(newEntries);
            persistCache();

            return deprecated;
        }
    }

    private Set<Reminder> deprecate(Predicate<Reminder> isDeprecated) {
        if (cache.isEmpty())
            return emptySet();

        Map<Boolean, Set<Reminder>> partitioned = cache.values().stream()
                .collect(partitioningBy(isDeprecated, toSet()));

        Set<Reminder> deprecated = partitioned.get(true);
        if (deprecated != null && !deprecated.isEmpty()) {
            Log.d(TAG, String.format("Deprecating [%d] reminders.", deprecated.size()));
            updateCache(partitioned.get(false));
        }
        return deprecated;
    }

    private void updateCache(Set<Reminder> reminders) {
        cache.clear();
        Map<Integer, Reminder> replacement = reminders.stream().collect(toMap(Reminder::getId, Function.identity()));
        cache.putAll(replacement);
        Log.d(TAG, String.format("Cache updated: [%s]", itemsInCache()));
    }

    public void setNotified(int id, int notificationId) {
        Reminder reminder = reminderBy(id);
        if (reminder == null) {
            onReminderMissing(id);
            return;
        }
        reminder.setNotified(notificationId);
        persistCache();
        Log.d(TAG, String.format("Reminder [%d] set to [NOTIFIED], with notificationId=[%d].", id, notificationId));
    }

    public void setDone(int id) {
        Reminder reminder = reminderBy(id);
        if (reminder == null) {
            onReminderMissing(id);
            return;
        }
        reminder.setDone();
        persistCache();
        Log.d(TAG, String.format("Reminder [%d] set to [DONE].", id));
    }

    @Nullable
    private Reminder reminderBy(int id) {
        loadCacheIfNeeded();
        return cache.get(id);
    }

    public Set<Reminder> getReminders(LocalDate date) {
        loadCacheIfNeeded();

        return cache.values().stream()
                .filter(reminder -> reminder.getDate().equals(date))
                .collect(toSet());
    }

    private void loadCacheIfNeeded() {
        if (cache != null) {
            Log.d(TAG, String.format("Cache is already loaded with [%d] reminder(s): [%s]", cache.size(), itemsInCache()));
            return;
        }

        Set<String> stringSet = preferences.getStringSet(PREFERENCE_KEY_REMINDERS, emptySet());
        if (stringSet.isEmpty()) {
            cache = new ArrayMap<>();
            Log.d(TAG, "Nothing loaded from Shared Preferences.");
            return;
        }

        Map<Integer, Reminder> map = stringSet.stream()
                .map(json -> gson.fromJson(json, Reminder.class))
                .collect(toMap(Reminder::getId, Function.identity()));
        cache = new ArrayMap<>();
        cache.putAll(map);
        Log.i(TAG, String.format("%d reminder(s) loaded.", cache.size()));
        Log.d(TAG, String.format("Reminder(s) in cache: [%s].", itemsInCache()));
    }

    private void persistCache() {
        if (cache == null)
            return;

        Log.d(TAG, String.format("Reminder(s) in cache: [%s].", itemsInCache()));
        Set<String> stringSet = cache.values().stream()
                .map(gson::toJson)
                .collect(toSet());
        preferences.edit().putStringSet(PREFERENCE_KEY_REMINDERS, stringSet).apply();
        Log.i(TAG, String.format("%d reminder(s) persisted.", cache.size()));
    }

    @Nullable
    public Reminder getReminder(int id) {
        Reminder cached = reminderBy(id);
        if (cached == null) {
            onReminderMissing(id);
            return null;
        } else
            return new Reminder(cached);
    }

    @Nullable
    public boolean hasReminder(@NonNull Reminder target) {
        loadCacheIfNeeded();
        return cache.containsValue(target);
    }

    private void onReminderMissing(int id) {
        Log.wtf(TAG, String.format(US, "Reminder not found by ID [%d].", id));
        Log.d(TAG, String.format(US, "Reminder(s) in cache: [%s]", itemsInCache()));
    }

    private String itemsInCache() {
        return cache.isEmpty() ? "EMPTY" : cache.values().stream()
                .map(Reminder::toString)
                .collect(joining(", "));
    }

}
