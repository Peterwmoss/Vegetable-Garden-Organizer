package dk.mifu.pmos.vegetablegardening.helpers

import android.content.Context
import dk.mifu.pmos.vegetablegardening.R
import java.text.SimpleDateFormat
import java.util.*

class Formatter(private val context: Context) {
    fun formatDate(date: Date?): String {
        val pattern = "dd. MMMM"
        val simpleDateFormat = SimpleDateFormat(pattern, Locale("da", "DK"))
        return if (date != null) simpleDateFormat.format(date)
        else context.getString(R.string.missing_info)
    }

    fun formatString(string: String?): String {
        return string ?: context.getString(R.string.missing_info)
    }

    fun formatSowingBoolean(sowing: Boolean?): String {
        return if(sowing != null) {
            if(sowing) context.getString(R.string.sow)
            else context.getString(R.string.plant)
        } else {
            context.getString(R.string.missing_info)
        }
    }
}