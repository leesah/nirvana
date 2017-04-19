package name.leesah.nirvana.ui.reminder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.joda.time.LocalTime;

import java.util.List;

import name.leesah.nirvana.R;
import name.leesah.nirvana.data.Pharmacist;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.reminder.Reminder;
import name.leesah.nirvana.utils.DateTimeHelper;

import static android.view.View.MeasureSpec.UNSPECIFIED;
import static android.view.View.MeasureSpec.makeMeasureSpec;
import static java.lang.String.format;
import static name.leesah.nirvana.model.reminder.Reminder.State.DONE;

/**
 * Created by sah on 2017-04-19.
 */
class ReminderCard implements Comparable<ReminderCard> {
    private static final String TAG = ReminderCard.class.getSimpleName();

    protected final LocalTime time;

    ReminderCard(LocalTime time) {
        this.time = time;
    }

    @Override
    public int compareTo(@NonNull ReminderCard that) {
        return this.time.compareTo(that.time);
    }

    public static class TiledRemindersCard extends ReminderCard {
        final private List<Reminder> reminders;

        public TiledRemindersCard(LocalTime time, List<Reminder> reminders) {
            super(time);
            this.reminders = reminders;
        }

    }

    public static class NoteCard extends ReminderCard {
        private final String title;

        public NoteCard(LocalTime time, String title) {
            super(time);
            this.title = title;
        }
    }

    static class ReminderCardArrayAdapter extends ArrayAdapter<ReminderCard> {
        private static final int REMINDERS_CARD_LAYOUT = R.layout.reminder_card;
        private static final int NOTE_CARD_LAYOUT = R.layout.reminder_card_note;

        public ReminderCardArrayAdapter(Context context, List<ReminderCard> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view;

            ReminderCard item = getItem(position);
            assert item != null;

            if (item instanceof TiledRemindersCard) {
                view = inflater.inflate(REMINDERS_CARD_LAYOUT, parent, false);

                TiledRemindersCard card = (TiledRemindersCard) item;
                ListView remindersListView = (ListView) view.findViewById(R.id.tiled_reminders);
                remindersListView.setAdapter(new ReminderArrayAdapter(getContext(), card.reminders));
                refreshListViewHeight(remindersListView);

            } else {
                view = inflater.inflate(NOTE_CARD_LAYOUT, parent, false);

                NoteCard card = (NoteCard) item;
                TextView textViewTitle = (TextView) view.findViewById(R.id.note);
                textViewTitle.setText(card.title);
            }

            ((TextView) view.findViewById(R.id.time)).setText(DateTimeHelper.toText(item.time));
            return view;
        }

        @Override
        public int getItemViewType(int position) {
            return getItem(position) instanceof TiledRemindersCard ? 0 : 1;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
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

    private static class ReminderArrayAdapter extends ArrayAdapter<Reminder> {

        private final Pharmacist pharmacist;

        public ReminderArrayAdapter(Context context, List<Reminder> reminders) {
            super(context, 0, reminders);
            pharmacist = Pharmacist.getInstance(getContext());
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView == null ? LayoutInflater.from(getContext()).inflate(R.layout.reminder_card_inner_list_item, parent, false) : convertView;

            Reminder reminder = getItem(position);
            Medication medication = pharmacist.getMedication(reminder.getMedicationId());

            ((TextView) view.findViewById(R.id.medication)).setText(medication == null ? getContext().getString(R.string.unknown_medication) : medication.getName());
            ((TextView) view.findViewById(R.id.dosage)).setText(getContext().getResources().getQuantityString(R.plurals.var_doses, reminder.getDosageAmount(), reminder.getDosageAmount()));
            view.findViewById(R.id.check).setEnabled(reminder.getState() == DONE);

            view.setOnClickListener(v -> viewDetails(reminder));

            return view;
        }

        private void viewDetails(Reminder reminder) {
            Intent intent = new Intent(getContext(), ReminderDetailsActivity.class)
                    .putExtra(ReminderDetailsActivity.EXTRA_REMINDER_ID, reminder.getId());
            getContext().startActivity(intent);
        }
    }
}
