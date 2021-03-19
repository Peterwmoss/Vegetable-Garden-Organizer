package dk.mifu.pmos.vegetablegardening.fragments.creategarden

import android.os.Bundle
import android.view.*
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.ObservableArrayMap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.skydoves.balloon.iconForm
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.database.AppDatabase
import dk.mifu.pmos.vegetablegardening.database.GardenRepository
import dk.mifu.pmos.vegetablegardening.databinding.FragmentCreateGridBinding
import dk.mifu.pmos.vegetablegardening.helpers.grid.GridBuilder
import dk.mifu.pmos.vegetablegardening.helpers.grid.GridBuilder.Companion.remainingHeight
import dk.mifu.pmos.vegetablegardening.helpers.callbacks.BedCallback
import dk.mifu.pmos.vegetablegardening.helpers.grid.EditGridBuilder
import dk.mifu.pmos.vegetablegardening.helpers.grid.EmptyGridBuilder
import dk.mifu.pmos.vegetablegardening.models.Bed
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel
import dk.mifu.pmos.vegetablegardening.views.Tooltip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class CreateGridFragment : Fragment() {
    private lateinit var binding: FragmentCreateGridBinding

    private val bedViewModel: BedViewModel by activityViewModels()

    private var tileSideLength = GridBuilder.getTileSideLength()
    private var callback: BedCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_default, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.tooltip -> {
                Tooltip.newTooltip(requireContext(), getString(R.string.tooltip_create_grid), requireView().rootView.findViewById(R.id.tooltip))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCreateGridBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bedViewModel.tileIds = HashMap()

        initializeGrid()
        setColumnButtonListeners()
        setRowButtonListeners()
        setSaveBedListener()

        callback = BedCallback(requireView(), bedViewModel)
    }

    private fun initializeGrid() {
        // Initialize new bed if not editing an existing one
        if (bedViewModel.name.isNullOrBlank()) {
            bedViewModel.plants = ObservableArrayMap()
            val builder = EmptyGridBuilder(bedViewModel, layoutInflater, binding.gridlayout, findNavController())
            initNewGrid(builder)
            (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.toolbar_create_grid)
        } else {
            val builder = EditGridBuilder(bedViewModel, layoutInflater, binding.gridlayout, findNavController())
            loadCurrentGrid(builder)
            (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.toolbar_edit_grid)
        }
    }

    override fun onStart() {
        super.onStart()
        bedViewModel.plants?.addOnMapChangedCallback(callback)
    }

    private fun initNewGrid(builder: EmptyGridBuilder) {
        bedViewModel.columns = 2
        bedViewModel.rows = 2

        builder.updateGridSizeFromViewModel()
        builder.createEmptyGrid()
    }

    private fun loadCurrentGrid(builder: EditGridBuilder) {
        builder.updateGridSizeFromViewModel()
        builder.insertTilesInView()
    }

    private fun setSaveBedListener() {
        binding.saveGardenButton.setOnClickListener {
            if (bedViewModel.name.isNullOrBlank()) {
                findNavController().navigate(CreateGridFragmentDirections.toSaveBedDialog())
            } else {
                MainScope().launch(Dispatchers.IO) {
                    val dao = AppDatabase.getDatabase(requireContext()).bedDao()
                    val repository = GardenRepository(dao)
                    repository.updateBed(Bed(bedViewModel.name!!, bedViewModel.bedLocation!!, bedViewModel.plants!!.toMap(), bedViewModel.columns, bedViewModel.rows))
                }
                findNavController().navigateUp()
            }
        }
    }

    private fun setRowButtonListeners() {
        val addButton = binding.addRowButton
        val removeButton = binding.removeRowButton

        addButton.setOnClickListener{
            changeTiles(add = true, column = false)
            if (remainingHeight(bedViewModel.rows, requireContext()) < tileSideLength) { //If there isn't enough room for a whole row more
                adjustPlacementOfGrid(full = true, column = false)
                addButton.visibility = View.GONE
            }
            removeButton.visibility = View.VISIBLE
        }

        removeButton.setOnClickListener{
            changeTiles(add = false, column = false)
            if (addButton.visibility == View.GONE) {
                adjustPlacementOfGrid(full = false, column = false)
                addButton.visibility = View.VISIBLE
            }
            if (bedViewModel.rows==2) {
                removeButton.visibility = View.GONE
            }
        }
    }

    private fun setColumnButtonListeners() {
        val addButton = binding.addColumnButton
        val removeButton = binding.removeColumnButton

        addButton.setOnClickListener{
            changeTiles(add = true, column = true)
            if (bedViewModel.columns==4) {
                adjustPlacementOfGrid(full = true, column = true)
                addButton.visibility = View.GONE
            }
            removeButton.visibility = View.VISIBLE
        }

        removeButton.setOnClickListener{
            changeTiles(add = false, column = true)
            if (addButton.visibility == View.GONE) {
                adjustPlacementOfGrid(full = false, column = true)
                addButton.visibility = View.VISIBLE
            }
            if (bedViewModel.columns==2) {
                removeButton.visibility = View.GONE
            }
        }
    }

    private fun adjustPlacementOfGrid(full: Boolean, column: Boolean) {
        val constraints = ConstraintSet()

        constraints.apply {
            clone(binding.parentLayout)
            if (full) {
                if (column)
                    clear(binding.gridlayout.id, ConstraintSet.END)
                else
                    clear(binding.gridlayout.id, ConstraintSet.BOTTOM)
            } else {
                if (column)
                    connect(binding.gridlayout.id, ConstraintSet.END, binding.parentLayout.id, ConstraintSet.END)
                else
                    connect(binding.gridlayout.id, ConstraintSet.BOTTOM, binding.parentLayout.id, ConstraintSet.BOTTOM)
            }
            applyTo(binding.parentLayout)
        }
    }

    private fun changeTiles(add: Boolean, column: Boolean) {
        bedViewModel.plants?.removeOnMapChangedCallback(callback)
        val grid = binding.gridlayout

        grid.removeAllViewsInLayout()

        if (column) {
            if (add) bedViewModel.columns++ else bedViewModel.columns--
        } else {
            if (add) bedViewModel.rows++ else bedViewModel.rows--
        }

        val helper = EditGridBuilder(bedViewModel, layoutInflater, binding.gridlayout, findNavController())
        helper.updateGridSizeFromViewModel()
        updatePlantsInViewModel(add, column)
        helper.insertTilesInView()
        bedViewModel.plants?.addOnMapChangedCallback(callback)
    }

    private fun updatePlantsInViewModel(add: Boolean, column: Boolean) {
        for (i in 0 until if (column) bedViewModel.rows else bedViewModel.columns) {
            val coordinate = if (column) Coordinate(bedViewModel.columns, i) else Coordinate(i, bedViewModel.rows)
            if (add)
                bedViewModel.plants?.set(coordinate, null)
            else
                bedViewModel.plants?.remove(coordinate)
        }
    }
}
