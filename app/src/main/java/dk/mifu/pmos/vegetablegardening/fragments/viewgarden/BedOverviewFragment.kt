package dk.mifu.pmos.vegetablegardening.fragments.viewgarden

import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.databinding.FragmentBedOverviewBinding
import dk.mifu.pmos.vegetablegardening.databinding.ListItemTileBinding
import dk.mifu.pmos.vegetablegardening.helpers.grid.GridBuilder
import dk.mifu.pmos.vegetablegardening.helpers.callbacks.BedCallback
import dk.mifu.pmos.vegetablegardening.helpers.callbacks.IconCallback
import dk.mifu.pmos.vegetablegardening.helpers.callbacks.UpdateBedCallback
import dk.mifu.pmos.vegetablegardening.helpers.predicates.LocationPredicate
import dk.mifu.pmos.vegetablegardening.helpers.predicates.PlantablePredicate
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.MyPlant
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel
import dk.mifu.pmos.vegetablegardening.viewmodels.PlantViewModel
import dk.mifu.pmos.vegetablegardening.views.Tooltip

class BedOverviewFragment: Fragment() {
    private lateinit var binding: FragmentBedOverviewBinding
    private var existsPlantablePlants = false
    private val bedViewModel: BedViewModel by activityViewModels()
    private val plantViewModel: PlantViewModel by activityViewModels()
    private var plantableTileSlots: Boolean = false

    private var saveChangesCallback: UpdateBedCallback? = null
    private var updateGridViewCallback: BedCallback? = null
    private var updateIconsCallback: IconCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_bed_overview, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.tooltip -> {
                Tooltip.newTooltip(requireContext(), getString(R.string.tooltip_bed_overview), requireView().rootView.findViewById(R.id.tooltip))
                true
            }
            R.id.edit -> {
                findNavController().navigate(BedOverviewFragmentDirections.editBedAction())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

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

        val helper = BedOverviewGridBuilder()
        helper.updateGridSizeFromViewModel()
        helper.insertTilesInView()

        setExplanationTextViews()

        saveChangesCallback =
                UpdateBedCallback(
                        bedViewModel.name!!,
                        bedViewModel.bedLocation!!,
                        requireContext(),
                        bedViewModel.columns,
                        bedViewModel.rows)

        updateGridViewCallback = BedCallback(requireView(), bedViewModel)
        updateIconsCallback = IconCallback(requireView(), bedViewModel)
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.title = bedViewModel.name
        addOnMapChangedCallbacks()
    }

    override fun onPause() {
        super.onPause()
        removeOnMapChangedCallbacks()
    }

    private fun setExplanationTextViews(){
        if(existsPlantablePlants && plantableTileSlots){
            binding.plantableExplanationTextView.visibility = View.VISIBLE
            binding.plantableExplanationTextView.text = getString(R.string.guide_plantable_plants)
            binding.plantableExplanationImageView.setImageResource(R.drawable.ic_flower)
        }

        bedViewModel.plantsToWater.observe(viewLifecycleOwner, {
            if(!it.isNullOrEmpty()){
                binding.waterExplanationTextView.visibility = View.VISIBLE
                binding.waterExplanationTextView.text = getString(R.string.guide_check_water)
                binding.waterExplanationImageView.setImageResource(R.drawable.water)
            }
        })
    }

    private fun navigate(coordinate: Coordinate, plant: MyPlant?) {
        if(plant == null) {
            requireView().findNavController().navigate(BedOverviewFragmentDirections.showPlantingOptions(coordinate, PlantablePredicate()))
        } else {
            requireView().findNavController().navigate(BedOverviewFragmentDirections.showPlantInfo(coordinate, plant))
        }
    }

    private fun addOnMapChangedCallbacks(){
        bedViewModel.plants?.addOnMapChangedCallback(updateGridViewCallback)
        bedViewModel.plants?.addOnMapChangedCallback(updateIconsCallback)
        bedViewModel.plants?.addOnMapChangedCallback(saveChangesCallback)
    }

    private fun removeOnMapChangedCallbacks(){
        bedViewModel.plants?.removeOnMapChangedCallback(updateGridViewCallback)
        bedViewModel.plants?.removeOnMapChangedCallback(updateIconsCallback)
        bedViewModel.plants?.removeOnMapChangedCallback(saveChangesCallback)
    }

    private inner class BedOverviewGridBuilder : GridBuilder(bedViewModel, layoutInflater, binding.gridlayout, findNavController()) {
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

        override fun initializeIcon(coordinate: Coordinate, plant: MyPlant?, tileBinding: ListItemTileBinding) {
            if(plant == null && existsPlantablePlants) {
                tileBinding.iconView.setImageResource(R.drawable.ic_flower)
                tileBinding.iconView.visibility = View.VISIBLE
                plantableTileSlots = true
            }

            bedViewModel.plantsToWater.observe(viewLifecycleOwner, {
                if(plant != null && it != null && it[coordinate] != null){
                    tileBinding.iconView.setImageResource(R.drawable.water)
                    tileBinding.iconView.visibility = View.VISIBLE
                }
            })
        }
    }
}