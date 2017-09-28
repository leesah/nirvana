package name.leesah.nirvana.model.reminder;

import com.google.common.base.MoreObjects;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.Objects;

import static name.leesah.nirvana.model.reminder.Reminder.State.NOTIFIED;
import static name.leesah.nirvana.model.reminder.Reminder.State.PLANNED;
import static name.leesah.nirvana.utils.IdentityHelper.uniqueInt;

/**
 * Created by sah on 2016-12-07.
 */

public class Reminder {

    public enum State {
        PLANNED,
        NOTIFIED,
        DONE
    }

    private final int id;
    private final DateTime plannedTime;
    private final DateTime actualTime;
    private final int medicationId;
    private final int dosageAmount;
    private State state;
    private Integer notificationId;

    private Reminder(int id, DateTime plannedTime, DateTime actualTime, int medicationId, int dosageAmount, State state, Integer notificationId) {
        this.id = id;
        this.plannedTime = new DateTime(plannedTime);
        this.actualTime = new DateTime(actualTime == null ? plannedTime : actualTime);
        this.medicationId = medicationId;
        this.dosageAmount = dosageAmount;
        this.state = state;
        this.notificationId = notificationId;
    }

    public Reminder(LocalDate date, LocalTime time, int medicationId, int dosageAmount) {
        this(uniqueInt(), date.toDateTime(time), null, medicationId, dosageAmount, PLANNED, null);
    }

    public Reminder(final Reminder that) {
        this(that.id, that.plannedTime, that.actualTime, that.medicationId, that.dosageAmount, that.state, that.notificationId);
    }

    @Deprecated
    public LocalDate getDate() {
        return actualTime.toLocalDate();
    }

    @Deprecated
    public LocalTime getTime() {
        return actualTime.toLocalTime();
    }

    public DateTime getPlannedTime() {
        return plannedTime;
    }

    public DateTime getActualTime() {
        return actualTime;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reminder reminder = (Reminder) o;
        return medicationId == reminder.medicationId &&
                Objects.equals(plannedTime, reminder.plannedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plannedTime, medicationId);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("plannedTime", plannedTime)
                .add("actualTime", actualTime)
                .add("medicationId", medicationId)
                .add("dosageAmount", dosageAmount)
                .add("state", state)
                .add("notificationId", notificationId)
                .toString();
    }
}
