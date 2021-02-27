package dk.mifu.pmos.vegetablegardening.viewgarden

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.databinding.FragmentBedOverviewBinding
import dk.mifu.pmos.vegetablegardening.databinding.ListItemTileBinding
import dk.mifu.pmos.vegetablegardening.helpers.GridHelper
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.Plant
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel

class BedOverviewFragment: Fragment() {
    private lateinit var binding: FragmentBedOverviewBinding
    private val bedViewModel: BedViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentBedOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bedTextView.text = bedViewModel.name

        val gridSize = sizeOfBed()
        val columns = gridSize.first
        val rows = gridSize.second

        binding.gridlayout.columnCount = columns
        binding.gridlayout.rowCount = rows

        val orderedArrayList: ArrayList<Plant?> = ArrayList()
        for(i in 0 until columns){
            for(j in 0 until rows){
                orderedArrayList.add(bedViewModel.plants?.get(Coordinate(i,j)))
            }
        }

        orderedArrayList.forEach {
            val tileSideLength = GridHelper.getTileSideLength()
            val itemTileBinding = ListItemTileBinding.inflate(layoutInflater, binding.gridlayout, true)
            itemTileBinding.plantButton.text = it?.name ?: ""
            itemTileBinding.plantButton.width = tileSideLength
            itemTileBinding.plantButton.height = tileSideLength
        }
    }

    private fun sizeOfBed(): Pair<Int,Int> {
        var column = 0
        var row = 0

        val map = bedViewModel.plants?.toMap()
        map?.keys?.forEach {
            val plantPosCol = it.col
            val plantPosRow = it.row
            if(plantPosCol > column) column = plantPosCol
            if(plantPosRow > row) row = plantPosRow
        }

        return Pair(column+1, row+1)
    }
}