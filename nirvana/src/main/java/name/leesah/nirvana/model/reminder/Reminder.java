package name.leesah.nirvana.model.reminder;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.Objects;

import static java.lang.String.format;
import static java.util.Locale.US;
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

    @Override
    public String toString() {
        return format(US,"[Reminder #%d (%s: %s %s, m#%d x %d)]", id, state.name(), toText(date), toText(time), medicationId, dosageAmount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reminder reminder = (Reminder) o;
        return medicationId == reminder.medicationId &&
                dosageAmount == reminder.dosageAmount &&
                Objects.equals(date, reminder.date) &&
                Objects.equals(time, reminder.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, time, medicationId, dosageAmount);
    }
}
