package dk.mifu.pmos.vegetablegardening.helpers.grid

import android.view.LayoutInflater
import android.widget.GridLayout
import androidx.navigation.NavController
import dk.mifu.pmos.vegetablegardening.databinding.ListItemTileBinding
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel

class EmptyGridBuilder(
        bedViewModel: BedViewModel,
        layoutInflater: LayoutInflater,
        grid: GridLayout,
        navController: NavController
) : EditGridBuilder(bedViewModel, layoutInflater, grid, navController) {

    fun createEmptyGrid(size: Int = 2) {
        bedViewModel.rows = size
        bedViewModel.columns = size
        for (i in 0 until size) {
            for (j in 0 until size) {
                val coordinate = Coordinate(j,i)
                val tileBinding = ListItemTileBinding.inflate(layoutInflater, grid, true)
                initializeTile(coordinate, null, tileBinding)
            }
        }
    }
}