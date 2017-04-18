package name.leesah.nirvana.ui.reminder;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.joda.time.LocalTime;
import org.joda.time.Minutes;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.ArrayList;
import java.util.List;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.reminder.Reminder;
import name.leesah.nirvana.data.Nurse;
import name.leesah.nirvana.data.Pharmacist;
import name.leesah.nirvana.utils.DateTimeHelper;

import static android.view.View.MeasureSpec.UNSPECIFIED;
import static android.view.View.MeasureSpec.makeMeasureSpec;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static name.leesah.nirvana.utils.DateTimeHelper.today;

/**
 * Created by sah on 2017-01-06.
 */

public class RemindersOfDayFragment extends Fragment {
    public static final PeriodFormatter PERIOD_FORMATTER = new PeriodFormatterBuilder()
            .appendHours()
            .appendSuffix(" hour", " hours")
            .appendSeparator(" ")
            .appendMinutes()
            .appendSuffix(" minute", " minutes")
            .toFormatter();
    private List<Card> cards = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reminder_list, container, false);

        ArrayAdapter arrayAdapter = new GroupedReminderArrayAdapter(getContext(), cards);

        ListView listView = (ListView) view.findViewById(R.id.reminders);
        listView.setAdapter(arrayAdapter);
        listView.setEmptyView(view.findViewById(R.id.empty_view));

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cards = Nurse.getInstance(getContext())
                .getReminders(today()).stream()
                .collect(groupingBy(reminder -> reminder.getTime().withSecondOfMinute(0).getMillisOfDay(), toSet())).entrySet().stream()
                .map(entry -> new MultipleTitledCard(LocalTime.fromMillisOfDay(entry.getKey()),
                        entry.getValue().stream().map(this::buildTitle).sorted().collect(toList())))
                .sorted()
                .collect(toList());

        if (!cards.isEmpty()) {
            int indexOfNextCard = (int) cards.stream().filter(g -> g.time.isBefore(LocalTime.now())).count();
            if (indexOfNextCard == cards.size())
                cards.add(SingleTitledCard.doneAll());
            else
                cards.add(indexOfNextCard, SingleTitledCard.untilNext(cards.get(indexOfNextCard).time));
        }
    }

    private String buildTitle(Reminder reminder) {
        Medication medication = Pharmacist.getInstance(getContext()).getMedication(reminder.getMedicationId());
        return String.format("%s, %d %s.", medication.getName(), reminder.getDosageAmount(), medication.getForm().name());
    }

    private abstract static class Card implements Comparable<MultipleTitledCard> {
        protected final LocalTime time;

        Card(LocalTime time) {
            this.time = time;
        }

        @Override
        public int compareTo(@NonNull MultipleTitledCard that) {
            return this.time.compareTo(that.time);
        }
    }

    private class MultipleTitledCard extends Card {
        final private List<String> titles;

        private MultipleTitledCard(LocalTime time, List<String> titles) {
            super(time);
            this.titles = titles;
        }

    }

    private static class SingleTitledCard extends Card {
        private final String title;

        private SingleTitledCard(LocalTime time, String title) {
            super(time);
            this.title = title;
        }

        private static SingleTitledCard untilNext(LocalTime timeOfNextIntake) {
            return new SingleTitledCard(LocalTime.now(), String.format("%s until next intake.", new Period(LocalTime.now(), timeOfNextIntake).plus(Minutes.ONE).toString(PERIOD_FORMATTER)));
        }

        private static SingleTitledCard doneAll() {
            return new SingleTitledCard(LocalTime.now(), "No more intakes for the day.");
        }

    }

    private class GroupedReminderArrayAdapter extends ArrayAdapter<Card> {
        private static final int RESOURCE_MULTIPLE = R.layout.reminder_card;
        private static final int RESOURCE_SINGLE = R.layout.reminder_card_note;

        private GroupedReminderArrayAdapter(Context context, List<Card> objects) {
            super(context, RESOURCE_MULTIPLE, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view;

            Card item = getItem(position);
            assert item != null;

            if (item instanceof MultipleTitledCard) {
                MultipleTitledCard card = (MultipleTitledCard) item;
                view = inflater.inflate(RESOURCE_MULTIPLE, parent, false);

                ListView listViewTitles = (ListView) view.findViewById(R.id.listView_titles);
                listViewTitles.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, card.titles));
                refreshListViewHeight(listViewTitles);
            } else {
                SingleTitledCard card = (SingleTitledCard) item;
                view = inflater.inflate(RESOURCE_SINGLE, parent, false);

                TextView textViewTitle = (TextView) view.findViewById(R.id.textView_title);
                textViewTitle.setText(card.title);
            }

            ((TextView) view.findViewById(R.id.textView_time)).setText(DateTimeHelper.toText(item.time));
            return view;
        }

        private void refreshListViewHeight(ListView listView) {
            Adapter adapter = listView.getAdapter();
            int n = adapter.getCount();
            View row = adapter.getView(0, null, listView);
            row.measure(makeMeasureSpec(0, UNSPECIFIED), makeMeasureSpec(0, UNSPECIFIED));

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = row.getMeasuredHeight() * n + (listView.getDividerHeight() * (n - 1));

            listView.setLayoutParams(params);
            listView.requestLayout();

        }
    }

}
