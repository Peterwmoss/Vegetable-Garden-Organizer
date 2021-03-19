package dk.mifu.pmos.vegetablegardening.helpers.callbacks

import android.content.Context
import android.widget.Button
import android.widget.TextView
import androidx.databinding.ObservableMap
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.MyPlant

class UpdateSortInViewCallback(
        private val coordinate: Coordinate,
        private val textView: TextView,
        private val button: Button,
        private val context: Context
) : BedPlantsChangedCallback() {

    override fun onMapChanged(sender: ObservableMap<Coordinate, MyPlant>?, key: Coordinate?) {
        if (key != coordinate) return

        val myPlant = sender!![key]!!
        textView.text = myPlant.sort ?: context.getString(R.string.missing_info)

        if (myPlant.sort == null) button.setText(R.string.add_sort)
        else button.setText(R.string.edit_sort)
    }
}