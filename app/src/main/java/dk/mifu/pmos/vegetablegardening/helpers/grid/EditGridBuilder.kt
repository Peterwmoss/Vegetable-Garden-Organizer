package dk.mifu.pmos.vegetablegardening.helpers.grid

import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.GridLayout
import androidx.navigation.NavController
import dk.mifu.pmos.vegetablegardening.databinding.ListItemTileBinding
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.MyPlant
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel

open class EditGridBuilder(
        bedViewModel: BedViewModel,
        layoutInflater: LayoutInflater,
        grid: GridLayout,
        navController: NavController
): GridBuilder(bedViewModel, layoutInflater, grid, navController) {

    override fun initializeTile(coordinate: Coordinate, plant: MyPlant?, tileBinding: ListItemTileBinding) {
        tileBinding.plantButton.text = plant?.name ?: ""
        val tileSideLength = getTileSideLength()
        val params = FrameLayout.LayoutParams(tileSideLength, tileSideLength)
        tileBinding.plantButton.layoutParams = params
        tileBinding.plantButton.id = View.generateViewId()
        tileBinding.plantButton.setOnClickListener(gridTileListener(coordinate))

        bedViewModel.tileIds?.put(coordinate, tileBinding.plantButton.id)
    }

    override fun initializeIcon(coordinate: Coordinate, plant: MyPlant?, tileBinding: ListItemTileBinding) { }
}