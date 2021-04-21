package dk.mifu.pmos.vegetablegardening.fragments.viewgarden

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.database.AppDatabase
import dk.mifu.pmos.vegetablegardening.database.BedRepository
import dk.mifu.pmos.vegetablegardening.databinding.FragmentBedOverviewBinding
import dk.mifu.pmos.vegetablegardening.helpers.callbacks.BedCallback
import dk.mifu.pmos.vegetablegardening.helpers.callbacks.IconCallback
import dk.mifu.pmos.vegetablegardening.helpers.callbacks.UpdateBedCallback
import dk.mifu.pmos.vegetablegardening.helpers.grid.BedOverviewGridBuilder
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel
import dk.mifu.pmos.vegetablegardening.viewmodels.LocationViewModel
import dk.mifu.pmos.vegetablegardening.viewmodels.SeasonViewModel
import dk.mifu.pmos.vegetablegardening.views.Tooltip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class BedOverviewFragment: Fragment() {
    private lateinit var binding: FragmentBedOverviewBinding

    private val bedViewModel: BedViewModel by activityViewModels()
    private val seasonViewModel: SeasonViewModel by activityViewModels()
    private val locationViewModel: LocationViewModel by activityViewModels()

    private var saveChangesCallback: UpdateBedCallback? = null
    private var updateGridViewCallback: BedCallback? = null
    private var updateIconsCallback: IconCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_editable, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.tooltip -> {
                Tooltip.newTooltip(requireContext(), getString(R.string.tooltip_bed_overview), requireView().rootView.findViewById(R.id.tooltip))
                true
            }
            R.id.edit -> {
                findNavController().navigate(BedOverviewFragmentDirections.toCreateGrid())
                true
            }
            R.id.delete -> {
                findNavController().navigate(BedOverviewFragmentDirections.toDeleteBedConfirmationDialog())
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

        val builder = BedOverviewGridBuilder(
                bedViewModel = bedViewModel,
                layoutInflater = layoutInflater,
                grid = binding.gridlayout,
                navController = findNavController(),
                lifecycleOwner = viewLifecycleOwner,
                context = requireContext(),
                binding = binding)

        loadBed(builder)

        saveChangesCallback =
                UpdateBedCallback(
                        bedViewModel.name!!,
                        seasonViewModel.currentSeason.value!!,
                        bedViewModel.bedLocation!!,
                        requireContext(),
                        bedViewModel.columns,
                        bedViewModel.rows,
                        bedViewModel.order)

        updateGridViewCallback = BedCallback(requireView(), bedViewModel)
        updateIconsCallback = IconCallback(requireView(), bedViewModel)
    }

    private fun loadBed(builder: BedOverviewGridBuilder) {
        MainScope().launch(Dispatchers.Main) {
            val def = async(Dispatchers.IO) {
                val dao = AppDatabase.getDatabase(requireContext()).bedDao()
                val repository = BedRepository(dao)
                return@async repository.findBedByPrimaryKeys(bedViewModel.name!!, seasonViewModel.currentSeason.value!!)
            }
            bedViewModel.setBed(def.await()!!)
            addOnMapChangedCallbacks()
            builder.updateGridSizeFromViewModel()
            builder.insertTilesInView()
            builder.setExplanationTextViews()

            locationViewModel.weather.observe(viewLifecycleOwner, {
                bedViewModel.setPlantsToWater(it.date)
            })
        }
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

    private fun removeOnMapChangedCallbacks() {
        bedViewModel.plants?.removeOnMapChangedCallback(updateGridViewCallback)
        bedViewModel.plants?.removeOnMapChangedCallback(updateIconsCallback)
        bedViewModel.plants?.removeOnMapChangedCallback(saveChangesCallback)
    }
}