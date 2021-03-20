package dk.mifu.pmos.vegetablegardening.fragments.viewgarden

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.databinding.FragmentBedOverviewBinding
import dk.mifu.pmos.vegetablegardening.helpers.callbacks.BedCallback
import dk.mifu.pmos.vegetablegardening.helpers.callbacks.IconCallback
import dk.mifu.pmos.vegetablegardening.helpers.callbacks.UpdateBedCallback
import dk.mifu.pmos.vegetablegardening.helpers.grid.BedOverviewGridHelper
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val helper = BedOverviewGridHelper.Builder()
                .setContext(requireContext())
                .setPlantViewModel(plantViewModel)
                .setBinding(binding)
                .setBedViewModel(bedViewModel)
                .setNavController(findNavController())
                .setLayoutInflater(layoutInflater)
                .setGridLayout(binding.gridlayout)
                .setLifecycleOwner(viewLifecycleOwner)
                .build()
        helper.updateGridSizeFromViewModel()
        helper.insertTilesInView()

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
}