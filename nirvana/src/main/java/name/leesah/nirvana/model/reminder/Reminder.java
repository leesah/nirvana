package name.leesah.nirvana.model.reminder;

import android.content.Context;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import name.leesah.nirvana.R;

import static name.leesah.nirvana.model.reminder.Reminder.State.NOTIFIED;
import static name.leesah.nirvana.model.reminder.Reminder.State.PLANNED;
import static name.leesah.nirvana.model.reminder.Reminder.State.SNOOZED;
import static name.leesah.nirvana.utils.DateTimeHelper.toText;
import static name.leesah.nirvana.utils.IdentityHelper.uniqueInt;

/**
 * Created by sah on 2016-12-07.
 */

public class Reminder {

    public enum State {
        PLANNED,
        NOTIFIED,
        SNOOZED,
        DONE
    }

    private final int id;
    private final LocalDate date;
    private final LocalTime time;
    private final int medicationId;
    private final int dosageAmount;
    private State state;
    private Integer notificationId;

    private Reminder(int id, LocalDate date, LocalTime time, int medicationId, int dosageAmount, State state, Integer notificationId) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.medicationId = medicationId;
        this.dosageAmount = dosageAmount;
        this.state = state;
        this.notificationId = notificationId;
    }

    public Reminder(LocalDate date, LocalTime time, int medicationId, int dosageAmount) {
        this(uniqueInt(), date, time, medicationId, dosageAmount, PLANNED, null);
    }

    public Reminder(final Reminder that) {
        this(that.id, that.date, that.time, that.medicationId, that.dosageAmount, that.state, that.notificationId);
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public int getMedicationId() {
        return medicationId;
    }

    public int getDosageAmount() {
        return dosageAmount;
    }

    public int getId() {
        return id;
    }

    public State getState() {
        return state;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotified(int notificationId) {
        this.state = NOTIFIED;
        this.notificationId = notificationId;
    }

    public void setDone() {
        this.state = State.DONE;
    }

    public Reminder snooze(int minutes) {
        LocalTime now = LocalTime.now();
        LocalTime time = now.plusMinutes(minutes);
        LocalDate date = time.isAfter(now) ? this.date : this.date.plusDays(1);
        return new Reminder(uniqueInt(), date, time, this.medicationId, this.dosageAmount, SNOOZED, null);
    }

    public String toString() {
        return String.format("Reminder {id=[%d], mid=[%d], d=[%s], t=[%s]}", id, medicationId, toText(date), toText(time));
    }

}
