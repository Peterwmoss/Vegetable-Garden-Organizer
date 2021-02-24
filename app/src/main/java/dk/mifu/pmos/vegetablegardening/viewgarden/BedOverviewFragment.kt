package dk.mifu.pmos.vegetablegardening.viewgarden

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.databinding.FragmentBedOverviewBinding
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

        binding.gridViewComponent.numColumns = columns

        val orderedArrayList: ArrayList<Plant?> = ArrayList()
        for(i in 0 until columns){
            for(j in 0 until rows){
                orderedArrayList.add(bedViewModel.plants?.get(Coordinate(i,j)))
            }
        }

        val adapter = ArrayAdapter<Plant>(requireContext(), R.layout.list_item_tile, orderedArrayList)
        binding.gridViewComponent.adapter = adapter
    }

    private fun sizeOfBed(): Pair<Int,Int> {
        var columns = 0
        var rows = 0

        val map = bedViewModel.plants?.toMap()
        map?.keys?.forEach {
            val plantPosCol = it.col
            val plantPosRow = it.row
            if(plantPosCol > columns) columns = plantPosCol
            if(plantPosRow > rows) rows = plantPosRow
        }

        return Pair(columns, rows)
    }
}