package name.leesah.nirvana.ui.reminder;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import static name.leesah.nirvana.ui.reminder.ReminderCardData.NoteAmongReminders;
import static name.leesah.nirvana.ui.reminder.ReminderCardData.ReminderCardArrayAdapter;
import static name.leesah.nirvana.ui.reminder.ReminderCardData.TiledReminders;
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
    private List<ReminderCardData> cards = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reminder_list, container, false);

        ArrayAdapter arrayAdapter = new ReminderCardArrayAdapter(getContext(), cards);

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
                .collect(groupingBy(Reminder::getTime, toList()))
                .entrySet().stream()
                .map(entry -> new TiledReminders(entry.getKey(), entry.getValue()))
                .sorted()
                .collect(toList());

        if (!cards.isEmpty()) {
            int indexOfFirstCardInTheFuture = (int) cards.stream().filter(card -> card.time.isBefore(now())).count();
            if (indexOfFirstCardInTheFuture == cards.size())
                cards.add(new NoteAmongReminders(now(), getString(R.string.note_no_more_intakes)));
            else
                cards.add(indexOfFirstCardInTheFuture, new NoteAmongReminders(now(), getString(R.string.note_time_until_next_intake, new Period(now(), cards.get(indexOfFirstCardInTheFuture).time).plus(Minutes.ONE).toString(PERIOD_FORMATTER))));
        }
    }

}
