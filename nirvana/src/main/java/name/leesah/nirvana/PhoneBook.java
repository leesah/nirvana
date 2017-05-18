package name.leesah.nirvana;

import android.content.Context;

import name.leesah.nirvana.data.Nurse;
import name.leesah.nirvana.data.Pharmacist;
import name.leesah.nirvana.data.Therapist;
import name.leesah.nirvana.model.reminder.ReminderMaker;
import name.leesah.nirvana.ui.reminder.AlarmSecretary;

/**
 * Created by sah on 2017-05-07.
 */

public class PhoneBook {

    private static Nurse nurse;
    private static Pharmacist pharmacist;
    private static Therapist therapist;
    private static ReminderMaker reminderMaker;
    private static AlarmSecretary alarmSecretary;

    public static Nurse nurse(Context context) {
        if (nurse == null)
            nurse = new Nurse(context);
        return nurse;
    }

    public static Pharmacist pharmacist(Context context) {
        if (pharmacist == null)
            pharmacist = new Pharmacist(context);
        return pharmacist;
    }

    public static Therapist therapist(Context context) {
        if (therapist == null)
            therapist = new Therapist(context);
        return therapist;
    }

    public static ReminderMaker reminderMaker(Context context) {
        if (reminderMaker == null)
            reminderMaker = new ReminderMaker(context);
        return reminderMaker;
    }

    public static AlarmSecretary alarmSecretary(Context context) {
        if (alarmSecretary == null)
            alarmSecretary = new AlarmSecretary(context);
        return alarmSecretary;
    }

    static void hireNurse(Nurse nurse) {
        PhoneBook.nurse = nurse;
    }

    static void hirePharmacist(Pharmacist pharmacist) {
        PhoneBook.pharmacist = pharmacist;
    }

    static void hireTherapist(Therapist therapist) {
        PhoneBook.therapist = therapist;
    }

    static void hireReminderMaker(ReminderMaker reminderMaker) {
        PhoneBook.reminderMaker = reminderMaker;
    }

    static void hireAlarmSecretary(AlarmSecretary alarmSecretary) {
        PhoneBook.alarmSecretary = alarmSecretary;
    }

    static void fireEveryone() {
        hireNurse(null);
        hirePharmacist(null);
        hireTherapist(null);
        hireTherapist(null);
        hireReminderMaker(null);
        hireAlarmSecretary(null);
    }
}
