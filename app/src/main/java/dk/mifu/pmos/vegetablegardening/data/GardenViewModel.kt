package dk.mifu.pmos.vegetablegardening.data

import androidx.lifecycle.ViewModel
import dk.mifu.pmos.vegetablegardening.R

class GardenViewModel : ViewModel() {
    val gardens: MutableList<Garden> by lazy {
        loadGardens()
    }

    private fun loadGardens(): MutableList<Garden> {
        val list = ArrayList<Garden>()
        val tileIds = HashMap<Coordinate, Int>()
        tileIds[Coordinate(0,0)] = R.id.tile1_image_view
        tileIds[Coordinate(1,0)] = R.id.tile2_image_view
        tileIds[Coordinate(0,1)] = R.id.tile3_image_view
        tileIds[Coordinate(1,1)] = R.id.tile4_image_view
        list.add(Garden(Location.Outdoors, null, HashMap(), tileIds))
        return list
    }
}