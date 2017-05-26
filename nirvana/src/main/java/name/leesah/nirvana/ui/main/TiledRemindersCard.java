package name.leesah.nirvana.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import name.leesah.nirvana.R;
import name.leesah.nirvana.model.medication.Medication;
import name.leesah.nirvana.model.reminder.Reminder;
import name.leesah.nirvana.ui.reminder.ReminderDetailsActivity;

import static java.util.Comparator.comparing;
import static name.leesah.nirvana.PhoneBook.pharmacist;
import static name.leesah.nirvana.utils.DateTimeHelper.toText;
import static name.leesah.nirvana.utils.ListViewHeightOptimizer.optimize;

/**
 * Created by sah on 2017-05-06.
 */

public class TiledRemindersCard extends FrameLayout {

    private final ListView tiledItems;
    private final TextView time;

    public TiledRemindersCard(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.reminder_card, this);
        time = ((TextView) findViewById(R.id.time));
        tiledItems = (ListView) findViewById(R.id.tiled_reminders);

    }

    public void fillWith(Data data) {
        time.setText(toText(data.time));

        List<Reminder> reminders = data.reminders.stream()
                .filter(reminder -> pharmacist(getContext())
                        .getMedication(reminder.getMedicationId()) != null)
                .sorted(comparing(reminder -> pharmacist(getContext())
                        .getMedication(reminder.getMedicationId()).getName()))
                .collect(Collectors.toList());

        tiledItems.setAdapter(new ItemArrayAdapter(getContext(), reminders));
        optimize(tiledItems);
    }

    public static class Data {
        public final LocalTime time;
        public final List<Reminder> reminders;

        public Data(LocalTime time, List<Reminder> reminders) {
            this.time = time;
            this.reminders = reminders;
        }
    }

    private class TiledItem extends LinearLayout {

        private final TextView medicationName;
        private final TextView dosage;
        private final ImageView status;
        private Medication medication;

        public TiledItem(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            inflate(context, R.layout.reminder_card_inner_list_item, this);
            medicationName = ((TextView) findViewById(R.id.medication));
            dosage = ((TextView) findViewById(R.id.dosage));
            status = (ImageView) findViewById(R.id.check);
        }

        public void setReminder(Reminder reminder) {
            medication = pharmacist(getContext())
                    .getMedication(reminder.getMedicationId());

            medicationName.setText(medication == null ?
                    getContext().getString(R.string.unknown_medication) :
                    medication.getName());

            dosage.setText(getContext().getResources()
                    .getQuantityString(R.plurals.var_doses, reminder.getDosageAmount(), reminder.getDosageAmount()));

            switch (reminder.getState()) {
                case NOTIFIED:
                    status.setImageResource(R.drawable.ic_reminder_status_active);
                    break;
                case SNOOZED:
                    status.setImageResource(R.drawable.ic_reminder_status_snoozed);
                    break;
                case DONE:
                    status.setImageResource(R.drawable.ic_reminder_status_done);
                    break;
            }

            setOnClickListener(v ->
                    viewReminderDetails(reminder));
        }

        private void viewReminderDetails(Reminder reminder) {
            Bundle params = new Bundle();
            params.putInt("reminder_id", reminder.getId());
            params.putString("reminder_state", reminder.getState().name());
            params.putString("reminder_time_planned", reminder.getPlannedTime().toString());
            params.putString("reminder_time_actual", reminder.getActualTime().toString());
            params.putInt("medication_id", medication.getId());
            params.putString("medication_name", medication.getName());
            FirebaseAnalytics.getInstance(getContext()).logEvent("reminder_view_details", params);

            getContext().startActivity(new Intent(getContext(), ReminderDetailsActivity.class)
                    .putExtra(ReminderDetailsActivity.EXTRA_REMINDER_ID, reminder.getId()));
        }

    }

    private class ItemArrayAdapter extends ArrayAdapter<Reminder> {

        private ItemArrayAdapter(Context context, List<Reminder> reminders) {
            super(context, 0, reminders);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            TiledItem view = convertView instanceof TiledItem ?
                    (TiledItem) convertView : new TiledItem(getContext(), null);

            view.setReminder(getItem(position));
            return view;
        }

    }
}
