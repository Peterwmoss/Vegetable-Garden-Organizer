package dk.mifu.pmos.vegetablegardening.fragments.creategarden

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.ObservableArrayMap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.databinding.FragmentCreateGridBinding
import dk.mifu.pmos.vegetablegardening.helpers.callbacks.BedCallback
import dk.mifu.pmos.vegetablegardening.helpers.grid.EditGridBuilder
import dk.mifu.pmos.vegetablegardening.helpers.grid.EmptyGridBuilder
import dk.mifu.pmos.vegetablegardening.helpers.grid.GridBuilder
import dk.mifu.pmos.vegetablegardening.helpers.grid.GridBuilder.Companion.remainingHeight
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel
import dk.mifu.pmos.vegetablegardening.views.Tooltip

class CreateGridFragment : Fragment() {
    companion object {
        private const val MAX_COLUMNS = 4
        private const val MIN_SIZE = 1
    }

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
        updateButtonVisibility()
        setColumnButtonListeners()
        setRowButtonListeners()
        setSaveBedListener()

        callback = BedCallback(requireView(), bedViewModel)
    }

    override fun onStart() {
        super.onStart()
        bedViewModel.plants?.addOnMapChangedCallback(callback)
    }

    private fun initializeGrid() {
        if (bedViewModel.name.isNullOrBlank()) {
            initNewGrid()
            (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.toolbar_create_grid)
        } else {
            loadCurrentGrid()
            (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.toolbar_edit_grid)
        }
    }

    private fun initNewGrid() {
        bedViewModel.plants = ObservableArrayMap()

        val builder = EmptyGridBuilder(
            bedViewModel = bedViewModel,
            layoutInflater = layoutInflater,
            grid = binding.gridlayout,
            navController = findNavController())
        builder.createEmptyGrid()
        builder.updateGridSizeFromViewModel()
    }

    private fun loadCurrentGrid() {
        val builder = EditGridBuilder(
                bedViewModel = bedViewModel,
                layoutInflater = layoutInflater,
                grid = binding.gridlayout,
                navController = findNavController())
        builder.updateGridSizeFromViewModel()
        builder.insertTilesInView()
    }

    private fun updateButtonVisibility() {
        if (bedViewModel.columns > MIN_SIZE)
            binding.removeColumnButton.visibility = View.VISIBLE
        else
            binding.removeColumnButton.visibility = View.GONE
        if (bedViewModel.rows > MIN_SIZE)
            binding.removeRowButton.visibility = View.VISIBLE
        else
            binding.removeRowButton.visibility = View.GONE

        if (bedViewModel.columns == MAX_COLUMNS) {
            binding.addColumnButton.visibility = View.GONE
            binding.removeColumnButton.visibility = View.VISIBLE
            adjustPlacementOfGrid(full = true, column = true)
        } else {
            binding.addColumnButton.visibility = View.VISIBLE
            adjustPlacementOfGrid(full = false, column = true)
        }
        if (remainingHeight(bedViewModel.rows, requireContext()) < tileSideLength) {
            binding.addRowButton.visibility = View.GONE
            binding.removeRowButton.visibility = View.VISIBLE
            adjustPlacementOfGrid(full = true, column = false)
        } else {
            binding.addRowButton.visibility = View.VISIBLE
            adjustPlacementOfGrid(full = false, column = false)
        }
    }

    private fun setSaveBedListener() {
        binding.saveGardenButton.setOnClickListener {
            findNavController().navigate(CreateGridFragmentDirections.toSaveBedDialog())
        }
    }

    private fun setRowButtonListeners() {
        val addButton = binding.addRowButton
        val removeButton = binding.removeRowButton

        addButton.setOnClickListener{
            changeTiles(add = true, column = false)
            updateButtonVisibility()
        }

        removeButton.setOnClickListener{
            changeTiles(add = false, column = false)
            updateButtonVisibility()
        }
    }

    private fun setColumnButtonListeners() {
        val addButton = binding.addColumnButton
        val removeButton = binding.removeColumnButton

        addButton.setOnClickListener{
            changeTiles(add = true, column = true)
            updateButtonVisibility()
        }

        removeButton.setOnClickListener{
            changeTiles(add = false, column = true)
            updateButtonVisibility()
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
        val gridLayout = binding.gridlayout

        gridLayout.removeAllViewsInLayout()

        if (column) {
            if (add) bedViewModel.columns++ else bedViewModel.columns--
        } else {
            if (add) bedViewModel.rows++ else bedViewModel.rows--
        }

        val builder = EditGridBuilder(
                bedViewModel = bedViewModel,
                layoutInflater = layoutInflater,
                grid = binding.gridlayout,
                navController = findNavController())
        builder.updateGridSizeFromViewModel()
        updatePlantsInViewModel(add, column)
        builder.insertTilesInView()
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
