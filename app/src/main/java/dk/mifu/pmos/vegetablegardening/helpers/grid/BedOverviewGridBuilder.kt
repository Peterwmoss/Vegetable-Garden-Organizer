package dk.mifu.pmos.vegetablegardening.helpers.grid

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.GridLayout
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.databinding.FragmentBedOverviewBinding
import dk.mifu.pmos.vegetablegardening.databinding.ListItemTileBinding
import dk.mifu.pmos.vegetablegardening.fragments.viewgarden.BedOverviewFragmentDirections
import dk.mifu.pmos.vegetablegardening.helpers.predicates.LocationPredicate
import dk.mifu.pmos.vegetablegardening.helpers.predicates.PlantablePredicate
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.MyPlant
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel
import dk.mifu.pmos.vegetablegardening.viewmodels.PlantViewModel

class BedOverviewGridBuilder(
        bedViewModel: BedViewModel,
        plantViewModel: PlantViewModel,
        layoutInflater: LayoutInflater,
        grid: GridLayout,
        navController: NavController,
        private val lifecycleOwner: LifecycleOwner,
        private val context: Context,
        private val binding: FragmentBedOverviewBinding
) : GridBuilder(bedViewModel, layoutInflater, grid, navController) {
    private var existsPlantablePlants = false
    private var plantableTileSlots = false

    init {
        existsPlantablePlants = !plantViewModel.plants.value
                ?.filter(PlantablePredicate())
                ?.filter(LocationPredicate(bedViewModel.bedLocation))
                .isNullOrEmpty()
    }

    override fun initializeIcon(coordinate: Coordinate, plant: MyPlant?, tileBinding: ListItemTileBinding) {
        if(plant == null && existsPlantablePlants) {
            tileBinding.iconView.setImageResource(R.drawable.ic_flower)
            tileBinding.iconView.visibility = View.VISIBLE
            plantableTileSlots = true
        }

        bedViewModel.plantsToWater.observe(lifecycleOwner, {
            if(plant != null && it != null && it[coordinate] != null){
                tileBinding.iconView.setImageResource(R.drawable.water)
                tileBinding.iconView.visibility = View.VISIBLE
            }
        })
    }

    override fun initializeTile(coordinate: Coordinate, plant: MyPlant?, tileBinding: ListItemTileBinding) {
        if(plant != null || existsPlantablePlants) //Only create listeners for tiles with plants or plantables
            tileBinding.plantButton.setOnClickListener { _ -> navigate(coordinate, plant) }

        tileBinding.plantButton.text = plant?.name ?: ""
        val tileSideLength = getTileSideLength()
        val params = FrameLayout.LayoutParams(tileSideLength, tileSideLength)
        tileBinding.plantButton.layoutParams = params
        tileBinding.plantButton.id = View.generateViewId()

        bedViewModel.tileIds?.put(coordinate, tileBinding.plantButton.id)
    }

    fun setExplanationTextViews() {
        if(existsPlantablePlants && plantableTileSlots){
            binding.plantableExplanationTextView.visibility = View.VISIBLE
            binding.plantableExplanationTextView.text = context.getString(R.string.guide_plantable_plants)
            binding.plantableExplanationImageView.setImageResource(R.drawable.ic_flower)
        }

        bedViewModel.plantsToWater.observe(lifecycleOwner, {
            if(!it.isNullOrEmpty()){
                binding.waterExplanationTextView.visibility = View.VISIBLE
                binding.waterExplanationTextView.text = context.getString(R.string.guide_check_water)
                binding.waterExplanationImageView.setImageResource(R.drawable.water)
            }
        })
    }

    private fun navigate(coordinate: Coordinate, plant: MyPlant?) {
        if(plant == null) {
            navController.navigate(BedOverviewFragmentDirections.showPlantingOptions(coordinate, PlantablePredicate()))
        } else {
            navController.navigate(BedOverviewFragmentDirections.showPlantInfo(coordinate, plant))
        }
    }

}