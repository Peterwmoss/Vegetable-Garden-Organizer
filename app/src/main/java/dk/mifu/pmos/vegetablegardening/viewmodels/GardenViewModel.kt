package dk.mifu.pmos.vegetablegardening.viewmodels

import androidx.lifecycle.ViewModel
import dk.mifu.pmos.vegetablegardening.enums.Location
import dk.mifu.pmos.vegetablegardening.models.Garden

class GardenViewModel : ViewModel() {
    val gardens: MutableList<Garden> by lazy {
        loadGardens()
    }

    private fun loadGardens(): MutableList<Garden> {
        return mutableListOf(Garden(Location.Outdoors))
    }
}