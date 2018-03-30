package name.leesah.nirvana.model.medication.reminding

import android.content.Context
import name.leesah.nirvana.R
import name.leesah.nirvana.model.medication.Medication
import name.leesah.nirvana.model.reminder.Reminder
import name.leesah.nirvana.model.reminder.TimedDosage
import org.joda.time.LocalDate
import java.util.*
import java.util.stream.Collectors.joining
import java.util.stream.Collectors.toSet

/**
 * Created by sah on 2016-12-11.
 */
class CertainHours(dosages: List<TimedDosage>) : RemindingStrategy {
    private val dosages = ArrayList<TimedDosage>()

    init {
        this.dosages.addAll(dosages)
    }

    override fun getRemindersThroughDay(medication: Medication, date: LocalDate): Set<Reminder> {
        return dosages.stream()
                .map { d -> Reminder(date, d.timeOfDay, medication.id, d.amount) }
                .collect(toSet())
    }

    fun getDosages(): List<TimedDosage> {
        return ArrayList(dosages)
    }

    override fun toString(context: Context): String {
        return dosages.stream()
                .map { dosage -> dosage.toString(context) }
                .collect(joining(context.getString(R.string.comma_equivalent)))
    }

}
