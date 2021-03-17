package dk.mifu.pmos.vegetablegardening.helpers.callbacks

import android.view.View
import android.widget.Button
import androidx.databinding.ObservableMap
import androidx.navigation.findNavController
import dk.mifu.pmos.vegetablegardening.fragments.viewgarden.BedOverviewFragmentDirections
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.MyPlant
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel

class BedCallback(
        private val view: View,
        private val bedViewModel: BedViewModel): BedPlantsChangedCallback() {

    override fun onMapChanged(sender: ObservableMap<Coordinate, MyPlant>?, key: Coordinate?) {
        val id = bedViewModel.tileIds?.get(key)!!
        val button = view.findViewById<Button>(id)
        if(button != null){
            val plant = bedViewModel.plants?.get(key)
            button.text = plant?.name
            button.setOnClickListener { _ ->
                view.findNavController().navigate(BedOverviewFragmentDirections.showPlantInfo(key!!, plant!!))
            }
        }
    }
}