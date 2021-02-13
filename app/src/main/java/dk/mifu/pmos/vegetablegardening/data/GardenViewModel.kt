package dk.mifu.pmos.vegetablegardening.data

import androidx.lifecycle.ViewModel
import dk.mifu.pmos.vegetablegardening.R

class GardenViewModel : ViewModel() {
    val gardens: MutableList<Garden> by lazy {
        loadGardens()
    }

    private fun loadGardens(): MutableList<Garden> {
        return mutableListOf(
            Garden(Location.Outdoors, "Outdoors garden"),
            Garden(Location.Indoors, "Indoors garden"),
            Garden(Location.Greenhouse, "Greenhouse garden"),
        )
    }
}