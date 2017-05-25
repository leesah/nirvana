package name.leesah.nirvana.ui.main;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.joda.time.LocalTime;
import org.joda.time.Minutes;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.ArrayList;
import java.util.List;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.reminder.Reminder;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static name.leesah.nirvana.PhoneBook.nurse;
import static name.leesah.nirvana.ui.main.TiledRemindersCard.Data;
import static name.leesah.nirvana.utils.DateTimeHelper.toText;
import static name.leesah.nirvana.utils.DateTimeHelper.today;
import static org.joda.time.LocalTime.now;

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
    private final List<Object> cards = new ArrayList<>();
    private TiledRemindersCardArrayAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private FirebaseAnalytics analytics;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        analytics = FirebaseAnalytics.getInstance(getContext());

        if (cards.isEmpty()) cards.addAll(buildCards());
        if (adapter == null) adapter = new TiledRemindersCardArrayAdapter(getContext(), cards);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reminder_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ListView listView = (ListView) view.findViewById(R.id.reminders);
        listView.setAdapter(adapter);
        listView.setEmptyView(view.findViewById(R.id.empty_view));

        refreshLayout = ((SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh));
        refreshLayout.setOnRefreshListener(() -> {
            analytics.logEvent("reminder_list_refresh_swipe", null);
            new RefreshTask().execute();
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.refresh_button, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_button:
                analytics.logEvent("reminder_list_refresh_menu", null);
                refreshLayout.setRefreshing(true);
                new RefreshTask().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private List<Object> buildCards() {
        List<Object> cards = nurse(getContext()).getReminders(today()).stream()
                .collect(groupingBy(Reminder::getTime, toList())).entrySet().stream()
                .map(entry -> new Data(entry.getKey(), entry.getValue()))
                .sorted(comparing(card -> card.time))
                .collect(toList());

        if (!cards.isEmpty()) {
            int index = (int) cards.stream()
                    .filter(card -> ((Data) card).time.isBefore(now()))
                    .count();

            cards.add(index, new Note(now(), index == cards.size() ?
                    getString(R.string.note_no_more_intakes) :
                    getString(R.string.note_time_until_next_intake,
                            new Period(now(), ((Data) cards.get(index)).time).plus(Minutes.ONE)
                                    .toString(PERIOD_FORMATTER))));
        }

        return cards;
    }

    private void onRefreshDone(List<Object> result) {
        cards.clear();
        cards.addAll(result);
        if (adapter != null) adapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
    }

    private static class Note {
        public final LocalTime time;
        public final String title;

        public Note(LocalTime time, String title) {
            this.time = time;
            this.title = title;
        }
    }

    private static class TiledRemindersCardArrayAdapter extends ArrayAdapter<Object> {

        TiledRemindersCardArrayAdapter(Context context, List<Object> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());

            Object card = getItem(position);
            if (card instanceof Data) {
                TiledRemindersCard view = convertView instanceof TiledRemindersCard ?
                        (TiledRemindersCard) convertView :
                        new TiledRemindersCard(getContext(), null);
                view.fillWith((Data) card);
                return view;
            } else {
                View view = inflater.inflate(R.layout.reminder_card_note, parent, false);
                Note note = (Note) card;
                ((TextView) view.findViewById(R.id.time)).setText(toText(note.time));
                ((TextView) view.findViewById(R.id.note)).setText(note.title);
                return view;
            }
        }

        @Override
        public int getItemViewType(int position) {
            return getItem(position) instanceof Data ? 0 : 1;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }
    }

    private class RefreshTask extends AsyncTask<Void, Void, List<Object>> {
        @Override
        protected List<Object> doInBackground(Void... voids) {
            return buildCards();
        }

        @Override
        protected void onPostExecute(List<Object> result) {
            super.onPostExecute(result);
            onRefreshDone(result);
        }

    }

}
