package dk.mifu.pmos.vegetablegardening.helpers.callbacks

import android.widget.Button
import android.widget.TextView
import androidx.databinding.ObservableMap
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.MyPlant

class UpdateGerminationInViewCallback(
        private val coordinate: Coordinate,
        private val textView: TextView,
        private val button: Button,
        private val textViewFunction: (Boolean?) -> String
    ) : BedPlantsChangedCallback() {

    override fun onMapChanged(sender: ObservableMap<Coordinate, MyPlant>?, key: Coordinate?) {
        if (key != coordinate) return

        val myPlant = sender!![key]!!
        textView.text = textViewFunction(myPlant.germinated)

        if(myPlant.germinated == null) button.setText(R.string.set_germination_status)
        else button.setText(R.string.edit_germination_status)
    }
}