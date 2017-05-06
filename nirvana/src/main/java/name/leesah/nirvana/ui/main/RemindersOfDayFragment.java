package name.leesah.nirvana.ui.main;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.joda.time.Minutes;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.ArrayList;
import java.util.List;

import name.leesah.nirvana.R;
import name.leesah.nirvana.data.Nurse;
import name.leesah.nirvana.model.reminder.Reminder;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static name.leesah.nirvana.ui.main.ReminderCardData.Note;
import static name.leesah.nirvana.ui.main.ReminderCardData.ReminderCardArrayAdapter;
import static name.leesah.nirvana.ui.main.ReminderCardData.TiledReminders;
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
    private final List<ReminderCardData> cards = new ArrayList<>();
    private ReminderCardArrayAdapter adapter;
    private SwipeRefreshLayout refreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (cards.isEmpty()) cards.addAll(buildCards());
        if (adapter == null) adapter = new ReminderCardArrayAdapter(getContext(), cards);
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
        refreshLayout.setOnRefreshListener(() -> new RefreshTask().execute());

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
                refreshLayout.setRefreshing(true);
                new RefreshTask().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private List<ReminderCardData> buildCards() {
        List<ReminderCardData> cards = Nurse.getInstance(getContext())
                .getReminders(today()).stream()
                .collect(groupingBy(Reminder::getTime, toList()))
                .entrySet().stream()
                .map(entry -> new TiledReminders(entry.getKey(), entry.getValue()))
                .sorted()
                .collect(toList());

        if (!cards.isEmpty()) {
            int index = (int) cards.stream()
                    .filter(card -> card.time.isBefore(now()))
                    .count();

            cards.add(index, new Note(now(), index == cards.size() ?
                    getString(R.string.note_no_more_intakes) :
                    getString(R.string.note_time_until_next_intake,
                            new Period(now(), cards.get(index).time).plus(Minutes.ONE)
                                    .toString(PERIOD_FORMATTER))));
        }

        return cards;
    }

    private void onRefreshDone(List<ReminderCardData> result) {
        cards.clear();
        cards.addAll(result);
        if (adapter != null) adapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
    }

    private class RefreshTask extends AsyncTask<Void, Void, List<ReminderCardData>> {
        @Override
        protected List<ReminderCardData> doInBackground(Void... voids) {
            return buildCards();
        }

        @Override
        protected void onPostExecute(List<ReminderCardData> result) {
            super.onPostExecute(result);
            onRefreshDone(result);
        }

    }

}
