package name.leesah.nirvana.model;

import android.support.annotation.IdRes;

import org.joda.time.DateTimeConstants;

import name.leesah.nirvana.R;

/**
 * Created by sah on 2016-12-07.
 */
public enum DayOfWeek {
    MONDAY(DateTimeConstants.MONDAY, R.id.checkBox_monday, "MONDAY"),
    TUESDAY(DateTimeConstants.TUESDAY, R.id.checkBox_tuesday, "TUESDAY"),
    WEDNESDAY(DateTimeConstants.WEDNESDAY, R.id.checkBox_wednesday, "WEDNESDAY"),
    THURSDAY(DateTimeConstants.THURSDAY, R.id.checkBox_thursday, "THURSDAY"),
    FRIDAY(DateTimeConstants.FRIDAY, R.id.checkBox_friday, "FRIDAY"),
    SATURDAY(DateTimeConstants.SATURDAY, R.id.checkBox_saturday, "SATURDAY"),
    SUNDAY(DateTimeConstants.SUNDAY, R.id.checkBox_sunday, "SUNDAY");

    private final int day;
    private final int checkBoxId;
    private final String name;

    DayOfWeek(int day, @IdRes int id, String name) {
        this.day = day;
        this.checkBoxId = id;
        this.name = name;
    }

    public int getDay() {
        return day;
    }

    public int getCheckBoxId() {
        return checkBoxId;
    }

    public String getName() {
        return name;
    }
}
