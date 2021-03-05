package dk.mifu.pmos.vegetablegardening.helpers.callbacks

import android.view.View
import android.widget.Button
import androidx.databinding.ObservableMap
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.Plant
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel

class BedCallback(private val view: View, private val bedViewModel: BedViewModel): BedPlantsChangedCallback() {
    override fun onMapChanged(sender: ObservableMap<Coordinate, Plant>?, key: Coordinate?) {
        val id = bedViewModel.tileIds?.get(key)!!
        view.findViewById<Button>(id).text = bedViewModel.plants?.get(key)?.name
    }
}