package dk.mifu.pmos.vegetablegardening.helpers.grid

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.widget.GridLayout
import androidx.navigation.NavController
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.databinding.ListItemTileBinding
import dk.mifu.pmos.vegetablegardening.fragments.creategarden.CreateGridFragmentDirections
import dk.mifu.pmos.vegetablegardening.helpers.predicates.AllPlantsPredicate
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.MyPlant
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel

abstract class GridBuilder(
        protected val bedViewModel: BedViewModel,
        protected val layoutInflater: LayoutInflater,
        protected val grid: GridLayout,
        protected val navController: NavController) {

    fun updateGridSizeFromViewModel() {
        grid.columnCount = bedViewModel.columns
        grid.rowCount = bedViewModel.rows
    }

    fun insertTilesInView() {
        getOrderedList().forEach {
            val coordinate = it.first
            val plant = it.second
            val tileBinding = ListItemTileBinding.inflate(layoutInflater, grid, true)
            initializeTile(coordinate, plant, tileBinding)
            initializeIcon(coordinate, plant, tileBinding)
        }
    }

    private fun getOrderedList() : List<Pair<Coordinate, MyPlant?>> {
        val orderedList: MutableList<Pair<Coordinate, MyPlant?>> = mutableListOf()
        for(i in 0 until bedViewModel.rows){
            for(j in 0 until bedViewModel.columns){
                val coordinate = Coordinate(j,i)
                orderedList.add(Pair(coordinate, bedViewModel.plants?.get(coordinate)))
            }
        }
        return orderedList
    }

    abstract fun initializeIcon(coordinate: Coordinate, plant: MyPlant?, tileBinding: ListItemTileBinding)

    abstract fun initializeTile(coordinate: Coordinate, plant: MyPlant?, tileBinding: ListItemTileBinding)

    protected fun gridTileListener(coordinate: Coordinate): View.OnClickListener {
        return View.OnClickListener {
            navController.navigate(CreateGridFragmentDirections.toChoosePlantDialog(coordinate, AllPlantsPredicate()))
        }
    }

    companion object {
        private const val buttonSideLength = 200

        private fun toolBarSize(context: Context): Int {
            return context.resources.getDimension(R.dimen.toolbar).toInt()
        }

        fun getTileSideLength(): Int {
            val width = getWidthOfScreen() - buttonSideLength
            return width shr 2 //Divide by 4
        }

        private fun getWidthOfScreen(): Int {
            return Resources.getSystem().displayMetrics.widthPixels
        }

        private fun getHeightOfScreen(): Int {
            return Resources.getSystem().displayMetrics.heightPixels
        }

        fun remainingHeight(rows: Int, context: Context): Int {
            return (getHeightOfScreen()
                    -(getTileSideLength() *rows)
                    - toolBarSize(context)
                    - buttonSideLength)
        }
    }
}