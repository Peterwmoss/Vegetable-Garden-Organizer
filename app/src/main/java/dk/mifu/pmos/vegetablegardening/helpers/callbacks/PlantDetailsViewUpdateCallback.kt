package dk.mifu.pmos.vegetablegardening.helpers.callbacks

import android.widget.Button
import android.widget.TextView
import androidx.databinding.ObservableMap
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.MyPlant

class PlantDetailsViewUpdateCallback(private val coordinate: Coordinate, private val textView: TextView, private val button: Button) : BedPlantsChangedCallback() {
    override fun onMapChanged(sender: ObservableMap<Coordinate, MyPlant>?, key: Coordinate?) {
        if (key != coordinate)
            return
        val value = sender!![key]!!
        textView.text = value.sort
        if (value.sort.isBlank())
            button.setText(R.string.add_sort_text)
        else
            button.setText(R.string.edit_sort_text)
    }
}