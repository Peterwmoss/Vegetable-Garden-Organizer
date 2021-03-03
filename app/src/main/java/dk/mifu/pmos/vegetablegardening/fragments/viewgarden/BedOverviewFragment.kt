package dk.mifu.pmos.vegetablegardening.fragments.viewgarden

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.ObservableMap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import dk.mifu.pmos.vegetablegardening.databinding.FragmentBedOverviewBinding
import dk.mifu.pmos.vegetablegardening.databinding.ListItemTileBinding
import dk.mifu.pmos.vegetablegardening.helpers.BedCallback
import dk.mifu.pmos.vegetablegardening.helpers.GridHelper
import dk.mifu.pmos.vegetablegardening.helpers.predicates.PlantToBooleanPredicate
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.Plant
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel
import dk.mifu.pmos.vegetablegardening.viewmodels.PlantViewModel

class BedOverviewFragment: Fragment() {
    private lateinit var binding: FragmentBedOverviewBinding
    private val bedViewModel: BedViewModel by activityViewModels()
    private val plantViewModel: PlantViewModel by activityViewModels()

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

        val orderedArrayList: ArrayList<Pair<Coordinate, Plant?>> = ArrayList()
        for(i in 0 until rows){
            for(j in 0 until columns){
                val coordinate = Coordinate(j,i)
                orderedArrayList.add(Pair(coordinate, bedViewModel.plants?.get(coordinate)))
            }
        }

        orderedArrayList.forEach {
            val coordinate = it.first
            val plant = it.second
            val tileBinding = ListItemTileBinding.inflate(layoutInflater, binding.gridlayout, true)
            initializeTile(coordinate, plant, tileBinding)
            initializeIcons(plant, tileBinding)
        }

        bedViewModel.plants?.addOnMapChangedCallback(BedCallback(requireView(), bedViewModel))
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

    private fun initializeTile(coordinate: Coordinate, plant: Plant?, tileBinding: ListItemTileBinding) {
        val tileSideLength = GridHelper.getTileSideLength()

        tileBinding.plantButton.text = plant?.name ?: ""
        tileBinding.plantButton.width = tileSideLength
        tileBinding.plantButton.height = tileSideLength
        tileBinding.plantButton.setOnClickListener { _ -> navigateToPlantInfoDialog(coordinate, plant) }
        tileBinding.plantButton.id = View.generateViewId()

        bedViewModel.tileIds?.put(coordinate, tileBinding.plantButton.id)
    }

    private fun initializeIcons(plant: Plant?, tileBinding: ListItemTileBinding){
        val plantablePlants = plantViewModel.plants.value?.filter(PlantViewModel.plantablePlantsPredicate())
        if(plant == null && !plantablePlants.isNullOrEmpty()){
            tileBinding.testTextview.text = "!"
            tileBinding.testTextview.visibility = View.VISIBLE
        }
    }

    private fun navigateToPlantInfoDialog(coordinate: Coordinate, plant: Plant?) {
        if(plant == null) {
            requireView().findNavController().navigate(BedOverviewFragmentDirections.showPlantingOptions(coordinate, PlantToBooleanPredicate()))
        } else {
            requireView().findNavController().navigate(BedOverviewFragmentDirections.showPlantInfo(coordinate, plant))
        }
    }
}