package dk.mifu.pmos.vegetablegardening.data

import androidx.lifecycle.ViewModel
import dk.mifu.pmos.vegetablegardening.R

class GardenViewModel : ViewModel() {
    val gardens: MutableList<Garden> by lazy {
        loadGardens()
    }

    private fun loadGardens(): MutableList<Garden> {
        val list = ArrayList<Garden>()
        val tileIds = HashMap<Pair<Int,Int>, Int>()
        list.add(Garden(Location.Outdoors, null, HashMap(), tileIds))
        return list
    }
}