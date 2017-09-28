package name.leesah.nirvana.ui;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import java.util.Set;

import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.reminder.Reminder;
import name.leesah.nirvana.ui.main.TiledRemindersCard;

import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.IntStream.range;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by sah on 2017-04-30.
 */

public class MoreMatchers {

    public static Matcher<View> withAdaptedData(final Matcher<?> dataMatcher) {
        return new BaseMatcher<View>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("AdapterView with data entry: ");
                dataMatcher.describeTo(description);
            }

            @Override
            public boolean matches(Object item) {
                return item instanceof AdapterView &&
                        range(0, ((AdapterView) item).getAdapter().getCount())
                        .anyMatch(i -> dataMatcher.matches(((AdapterView) item).getAdapter().getItem(i)));
            }
        };
    }

    public static Matcher<TiledRemindersCard.Data> isCardContaining(Reminder reminder) {
        return new BaseMatcher<TiledRemindersCard.Data>() {
            @Override
            public boolean matches(Object item) {
                return item instanceof TiledRemindersCard.Data &&
                        ((TiledRemindersCard.Data)item).reminders.contains(reminder);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("card containing reminder: ")
                        .appendText(reminder.toString());
            }
        };
    }


    @NonNull
    public static Matcher<View> switchWidgetBesidesTitle(int prefTitleResId) {
        return allOf(
                withId(android.R.id.switch_widget),
                withParent(allOf(
                        withId(android.R.id.widget_frame),
                        withParent(withChild(
                                withChild(allOf(
                                        withId(android.R.id.title),
                                        withText(prefTitleResId)))))))
        );
    }

    public static FeatureMatcher<Medication, Integer> ofMedicationWithId(final int id) {
        return new FeatureMatcher<Medication, Integer>(equalTo(id), "medication with id", "medication id") {
            @Override
            protected Integer featureValueOf(Medication actual) {
                return actual.getId();
            }
        };
    }

    public static FeatureMatcher<Medication, String> ofMedicationWithName(final String name) {
        return new FeatureMatcher<Medication, String>(equalTo(name), "medication with name", "medication name") {
            @Override
            protected String featureValueOf(Medication actual) {
                return actual.getName();
            }
        };
    }

    public static FeatureMatcher<TiledRemindersCard.Data, Set<Integer>> ofReminderWithId(final int id) {
        return new FeatureMatcher<TiledRemindersCard.Data, Set<Integer>>(hasItem(id), "card with tiled reminders the ids of which being", "reminder id") {
            @Override
            protected Set<Integer> featureValueOf(TiledRemindersCard.Data actual) {
                return actual.reminders.stream().map(Reminder::getId).collect(toSet());
            }
        };
    }

    public static FeatureMatcher<TiledRemindersCard.Data, Set<Integer>> ofReminderWithMedicationId(final int medicationId) {
        return new FeatureMatcher<TiledRemindersCard.Data, Set<Integer>>(hasItem(medicationId), "card with tiled reminders the medication ids of which being", "medication id in reminder") {
            @Override
            protected Set<Integer> featureValueOf(TiledRemindersCard.Data actual) {
                return actual.reminders.stream().map(Reminder::getMedicationId).collect(toSet());
            }
        };
    }

}
