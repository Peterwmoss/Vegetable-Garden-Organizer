package dk.mifu.pmos.vegetablegardening.data

import androidx.lifecycle.ViewModel
import dk.mifu.pmos.vegetablegardening.R
import kotlinx.android.synthetic.main.fragment_create_grid.view.*

class GardenViewModel : ViewModel() {
    val gardens: MutableList<Garden> by lazy {
        loadGardens()
    }

    private fun loadGardens(): MutableList<Garden> {
        val list = ArrayList<Garden>()
        val tileIds = HashMap<Pair<Int,Int>, Int>()
        tileIds[Pair(0,0)] = R.id.tile1_image_view
        tileIds[Pair(1,0)] = R.id.tile2_image_view
        tileIds[Pair(0,1)] = R.id.tile3_image_view
        tileIds[Pair(1,1)] = R.id.tile4_image_view
        list.add(Garden(Location.Outdoors, null, HashMap(), tileIds))
        return list
    }
}