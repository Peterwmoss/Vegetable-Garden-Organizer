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

class BedOverviewGridHelper private constructor(
        bedViewModel: BedViewModel,
        plantViewModel: PlantViewModel,
        layoutInflater: LayoutInflater,
        grid: GridLayout,
        navController: NavController,
        lifecycleOwner: LifecycleOwner,
        context: Context,
        binding: FragmentBedOverviewBinding) : GridHelper(bedViewModel, layoutInflater, grid, navController) {

    private val plantViewModel: PlantViewModel
    private val lifecycleOwner: LifecycleOwner
    private val context: Context
    private val binding: FragmentBedOverviewBinding

    private var existsPlantablePlants = false
    private var plantableTileSlots = false

    init {
        this.plantViewModel = plantViewModel
        this.lifecycleOwner = lifecycleOwner
        this.context = context
        this.binding = binding
        existsPlantablePlants = !plantViewModel.plants.value
                ?.filter(PlantablePredicate())
                ?.filter(LocationPredicate(bedViewModel.bedLocation))
                .isNullOrEmpty()

        setExplanationTextViews()
    }

    class Builder : GridHelper.Builder() {
        private var plantViewModel: PlantViewModel? = null
        private var lifecycleOwner: LifecycleOwner? = null
        private var context: Context? = null
        private var binding: FragmentBedOverviewBinding? = null

        fun setPlantViewModel(plantViewModel: PlantViewModel) = apply { this.plantViewModel = plantViewModel }
        fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) = apply { this.lifecycleOwner = lifecycleOwner }
        fun setContext(context: Context) = apply { this.context = context }
        fun setBinding(binding: FragmentBedOverviewBinding) = apply { this.binding = binding }

        // Allow for arbitrary order of applying parameters
        override fun setBedViewModel(bedViewModel: BedViewModel) = apply { this.bedViewModel = bedViewModel }
        override fun setLayoutInflater(layoutInflater: LayoutInflater) = apply { this.layoutInflater = layoutInflater }
        override fun setGridLayout(grid: GridLayout) = apply { this.grid = grid }
        override fun setNavController(navController: NavController) = apply { this.navController = navController }

        override fun build(): BedOverviewGridHelper {
            return BedOverviewGridHelper(
                    bedViewModel!!,
                    plantViewModel!!,
                    layoutInflater!!,
                    grid!!,
                    navController!!,
                    lifecycleOwner!!,
                    context!!,
                    binding!!)
        }
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

    private fun setExplanationTextViews() {
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