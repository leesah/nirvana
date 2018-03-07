package name.leesah.nirvana.model.medication

import android.content.Context
import android.content.res.Resources
import android.support.annotation.Keep
import android.util.Log

import java.util.ArrayList

import java.lang.String.format
import java.util.EnumSet.allOf

/**
 * Created by sah on 2016-12-07.
 */
@Keep
enum class DosageForm {
    TABLET, CAPSULE, INJECTION, DROP;

    fun getName(context: Context): String {
        val resources = context.resources
        val resId = resources.getIdentifier(name.toLowerCase(), "string", context.packageName)
        if (resId == 0) {
            val msg = format("Missing resource [R.string.%s].", name.toLowerCase())
            Log.wtf(name, msg)
            throw IllegalStateException(msg)
        }
        return resources.getString(resId)
    }

    fun getDosageString(context: Context, quantity: Int): String {
        val resources = context.resources
        val resId = resources.getIdentifier(name.toLowerCase() + "_dosage_formatter", "plurals", context.packageName)
        if (resId == 0) {
            val msg = format("Missing resource [R.plurals.%s].", name.toLowerCase() + "_dosage_formatter")
            Log.wtf(name, msg)
            throw IllegalStateException(msg)
        }
        return format(resources.getQuantityString(resId, quantity), quantity)
    }

    companion object {

        fun withName(context: Context, name: String): DosageForm {
            return ArrayList(allOf(DosageForm::class.java)).stream()
                    .filter { form -> form.getName(context) == name }
                    .reduce { a, b -> throw IllegalStateException(format("[%s] matches more than one dosage forms: [%s, %s].", name, a, b)) }
                    .get()
        }
    }
}
