package dk.mifu.pmos.vegetablegardening.fragments.viewgarden

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.databinding.FragmentBedOverviewBinding
import dk.mifu.pmos.vegetablegardening.databinding.ListItemTileBinding
import dk.mifu.pmos.vegetablegardening.helpers.callbacks.BedCallback
import dk.mifu.pmos.vegetablegardening.helpers.GridHelper
import dk.mifu.pmos.vegetablegardening.helpers.callbacks.IconCallback
import dk.mifu.pmos.vegetablegardening.helpers.predicates.LocationPredicate
import dk.mifu.pmos.vegetablegardening.helpers.predicates.PlantablePredicate
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.MyPlant
import dk.mifu.pmos.vegetablegardening.models.Plant
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel
import dk.mifu.pmos.vegetablegardening.viewmodels.PlantViewModel

class BedOverviewFragment: Fragment() {
    private lateinit var binding: FragmentBedOverviewBinding
    private var existsPlantablePlants = false
    private val bedViewModel: BedViewModel by activityViewModels()
    private val plantViewModel: PlantViewModel by activityViewModels()
    private var columns = 0
    private var rows = 0


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentBedOverviewBinding.inflate(inflater, container, false)

        existsPlantablePlants = !plantViewModel.plants.value
                ?.filter(PlantablePredicate())
                ?.filter(LocationPredicate(bedViewModel.bedLocation))
                .isNullOrEmpty()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bedTextView.text = bedViewModel.name

        val gridSize = sizeOfBed()
        columns = gridSize.first
        rows = gridSize.second

        binding.gridlayout.columnCount = columns
        binding.gridlayout.rowCount = rows

        val orderedArrayList = getTilesInOrder()

        insertTilesInView(orderedArrayList)
        addOnMapChangedCallbacks()
        setExplanationTextViews()
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

    private fun getTilesInOrder(): List<Pair<Coordinate, MyPlant?>> {
        val orderedArrayList: MutableList<Pair<Coordinate, MyPlant?>> = mutableListOf()
        for(i in 0 until rows){
            for(j in 0 until columns){
                val coordinate = Coordinate(j,i)
                orderedArrayList.add(Pair(coordinate, bedViewModel.plants?.get(coordinate)))
            }
        }
        return orderedArrayList
    }

    private fun insertTilesInView(list: List<Pair<Coordinate, MyPlant?>>){
        list.forEach {
            val coordinate = it.first
            val plant = it.second
            val tileBinding = ListItemTileBinding.inflate(layoutInflater, binding.gridlayout, true)
            initializeTile(coordinate, plant, tileBinding)
            initializeIcons(coordinate, plant, tileBinding)
        }
    }

    private fun initializeTile(coordinate: Coordinate, plant: MyPlant?, tileBinding: ListItemTileBinding) {
        val tileSideLength = GridHelper.getTileSideLength()

        if(plant != null || existsPlantablePlants) //Only create listeners for tiles with plants or plantables
            tileBinding.plantButton.setOnClickListener { _ -> navigate(coordinate, plant) }

        tileBinding.plantButton.text = plant?.name ?: ""
        tileBinding.plantButton.width = tileSideLength
        tileBinding.plantButton.height = tileSideLength
        tileBinding.plantButton.id = View.generateViewId()

        bedViewModel.tileIds?.put(coordinate, tileBinding.plantButton.id)
    }

    private fun initializeIcons(coordinate: Coordinate, plant: MyPlant?, tileBinding: ListItemTileBinding){
        if(plant == null && existsPlantablePlants) {
            tileBinding.iconView.setImageResource(R.drawable.ic_flower)
            tileBinding.iconView.visibility = View.VISIBLE
        }

        bedViewModel.plantsToWater.observe(viewLifecycleOwner, {
            if(plant != null && it != null && it[coordinate] != null){
                tileBinding.iconView.setImageResource(R.drawable.ic_water)
                tileBinding.iconView.visibility = View.VISIBLE
            }
        })
    }

    private fun addOnMapChangedCallbacks(){
        bedViewModel.plants?.addOnMapChangedCallback(BedCallback(requireView(), bedViewModel))
        bedViewModel.plants?.addOnMapChangedCallback(IconCallback(requireView(), bedViewModel))
    }

    private fun navigate(coordinate: Coordinate, plant: MyPlant?) {
        if(plant == null) {
            requireView().findNavController().navigate(BedOverviewFragmentDirections.showPlantingOptions(coordinate, PlantablePredicate()))
        } else {
            requireView().findNavController().navigate(BedOverviewFragmentDirections.showPlantInfo(coordinate, plant))
        }
    }

    private fun setExplanationTextViews(){
        if(existsPlantablePlants){
            binding.plantableExplanationTextView.visibility = View.VISIBLE
            binding.plantableExplanationTextView.text = getString(R.string.explanation_new_plants)
            binding.plantableExplanationImageView.setImageResource(R.drawable.ic_flower)
        }

        bedViewModel.plantsToWater.observe(viewLifecycleOwner, {
            if(!it.isNullOrEmpty()){
                binding.waterExplanationTextView.visibility = View.VISIBLE
                binding.waterExplanationTextView.text = getString(R.string.explanation_check_water)
                binding.waterExplanationImageView.setImageResource(R.drawable.ic_water)
            }
        })
    }
}