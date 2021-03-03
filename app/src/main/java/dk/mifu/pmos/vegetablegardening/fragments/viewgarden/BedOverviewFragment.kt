package dk.mifu.pmos.vegetablegardening.fragments.viewgarden

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import dk.mifu.pmos.vegetablegardening.databinding.FragmentBedOverviewBinding
import dk.mifu.pmos.vegetablegardening.databinding.ListItemTileBinding
import dk.mifu.pmos.vegetablegardening.helpers.GridHelper
import dk.mifu.pmos.vegetablegardening.helpers.Weather
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.Plant
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class BedOverviewFragment: Fragment() {
    private lateinit var binding: FragmentBedOverviewBinding
    private val bedViewModel: BedViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentBedOverviewBinding.inflate(inflater, container, false)

        lifecycleScope.launch {
            val weather = object : Weather(Date(), requireContext()) {
                override fun handleResponse(json: JSONObject) {
                    json.keys().forEach {
                        Log.e("json", it)
                    }
                    val arr = json.getJSONArray("features")
                    for (i in 0 until arr.length()) {
                        Log.e("feature", arr[i].toString())
                    }
                }
            }
            weather.getLastRained()
        }

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

        val orderedArrayList: ArrayList<Pair<Coordinate, Plant?>> = ArrayList()
        for(i in 0 until columns){
            for(j in 0 until rows){
                val coordinate = Coordinate(i,j)
                orderedArrayList.add(Pair(coordinate, bedViewModel.plants?.get(coordinate)))
            }
        }

        orderedArrayList.forEach {
            val coordinate = it.first
            val plant = it.second
            val tileSideLength = GridHelper.getTileSideLength()
            val itemTileBinding = ListItemTileBinding.inflate(layoutInflater, binding.gridlayout, true)
            itemTileBinding.plantButton.text = plant?.name ?: ""
            itemTileBinding.plantButton.width = tileSideLength
            itemTileBinding.plantButton.height = tileSideLength
            itemTileBinding.plantButton.setOnClickListener { _ -> navigateToPlantInfoDialog(coordinate, plant) }
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

    private fun navigateToPlantInfoDialog(coordinate: Coordinate, plant: Plant?) {
        if (plant != null) {
            requireView().findNavController().navigate(BedOverviewFragmentDirections.showPlantInfo(coordinate, plant))
        }
    }
}