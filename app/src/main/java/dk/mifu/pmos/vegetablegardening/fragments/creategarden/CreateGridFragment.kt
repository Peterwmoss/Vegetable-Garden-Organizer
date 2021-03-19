package dk.mifu.pmos.vegetablegardening.fragments.creategarden

import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.ObservableArrayMap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.databinding.FragmentCreateGridBinding
import dk.mifu.pmos.vegetablegardening.fragments.dialogs.SaveBedDialogFragment
import dk.mifu.pmos.vegetablegardening.helpers.callbacks.BedCallback
import dk.mifu.pmos.vegetablegardening.helpers.GridHelper
import dk.mifu.pmos.vegetablegardening.helpers.GridHelper.Companion.START
import dk.mifu.pmos.vegetablegardening.helpers.GridHelper.Companion.TOP
import dk.mifu.pmos.vegetablegardening.helpers.GridHelper.Companion.BOTTOM
import dk.mifu.pmos.vegetablegardening.helpers.GridHelper.Companion.END
import dk.mifu.pmos.vegetablegardening.helpers.GridHelper.Companion.remainingHeight
import dk.mifu.pmos.vegetablegardening.helpers.predicates.AllPlantsPredicate
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel
import dk.mifu.pmos.vegetablegardening.views.GridTile
import dk.mifu.pmos.vegetablegardening.views.Tooltip

class CreateGridFragment : Fragment() {
    private lateinit var binding: FragmentCreateGridBinding

    private val bedViewModel: BedViewModel by activityViewModels()

    private var height = 0
    private var tileSideLength = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.tooltip -> {
                Tooltip.newTooltip(requireContext(), getString(R.string.guide_create_grid_text), requireView().rootView.findViewById(R.id.tooltip))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentCreateGridBinding.inflate(inflater, container, false)

        // Initialize current bed
        bedViewModel.plants = ObservableArrayMap()
        bedViewModel.tileIds = HashMap()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        height = GridHelper.getHeightOfScreen()
        tileSideLength = GridHelper.getTileSideLength()

        insertInitialGridTiles()
        setListeners()
        setSaveBedListener()

        bedViewModel.plants?.addOnMapChangedCallback(BedCallback(requireView(), bedViewModel))

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.create_grid_title)
    }

    private fun setSaveBedListener() {
        binding.saveGardenButton.setOnClickListener {
            findNavController().navigate(CreateGridFragmentDirections.toSaveBedDialog())
        }
    }

    private fun gridTileListener(coordinate: Coordinate): View.OnClickListener {
        return View.OnClickListener {
            navigateToChoosePlantFragment(coordinate)
        }
    }

    private fun insertInitialGridTiles(){
        val coordinate1 = Coordinate(0,0)
        val initialTile1 = GridTile(requireContext(), gridTileListener(coordinate1), binding)
        binding.parentLayout.addView(initialTile1)
        bedViewModel.tileIds?.set(coordinate1, initialTile1.id)
        initialTile1.snapToGrid(null,null,false)

        val coordinate2 = Coordinate(0,1)
        val initialTile2 = GridTile(requireContext(), gridTileListener(coordinate2), binding)
        binding.parentLayout.addView(initialTile2)
        bedViewModel.tileIds?.set(coordinate2, initialTile2.id)
        initialTile2.snapToGrid(null,initialTile1.id,false)

        bedViewModel.columns = 1
        bedViewModel.rows = 2

        addTiles(column = true)
    }

    private fun setListeners() {
        binding.addColumnButton.setOnClickListener{
            addTiles(column = true)
            if(bedViewModel.columns==4){
                binding.addColumnButton.visibility = View.GONE
                changePlacementOfRemoveButton(
                    column = true,
                    buttonId = binding.removeColumnButton.id
                )
            }
            if(binding.removeColumnButton.visibility == View.GONE){
                binding.removeColumnButton.visibility = View.VISIBLE
            }
        }

        binding.addRowButton.setOnClickListener{
            addTiles(column = false)
            if(remainingHeight(bedViewModel.rows, requireContext()) < tileSideLength){ //If there isn't enough room for a whole row more
                binding.addRowButton.visibility = View.GONE
                changePlacementOfRemoveButton(
                    column = false,
                    buttonId = binding.removeRowButton.id
                )
            }
            if(binding.removeRowButton.visibility == View.GONE){
                binding.removeRowButton.visibility = View.VISIBLE
            }
        }

        binding.removeColumnButton.setOnClickListener{
            removeTiles(column = true)
            if(binding.addColumnButton.visibility == View.GONE){
                binding.addColumnButton.visibility = View.VISIBLE
                changePlacementOfRemoveButton(
                    column = true,
                    buttonId = binding.removeColumnButton.id
                )
            }
            if(bedViewModel.columns==2){
                binding.removeColumnButton.visibility = View.GONE
            }
        }

        binding.removeRowButton.setOnClickListener{
            removeTiles(column = false)
            if(binding.addRowButton.visibility == View.GONE){
                binding.addRowButton.visibility = View.VISIBLE
                changePlacementOfRemoveButton(
                    column = false,
                    buttonId = binding.removeRowButton.id
                )
            }

            if(bedViewModel.rows==2){
                binding.removeRowButton.visibility = View.GONE
            }
        }
    }

    private fun removeTiles(column: Boolean) {
        for (i in 0 until if (column) bedViewModel.rows else bedViewModel.columns) {
            val coordinate = if (column) Coordinate(bedViewModel.columns-1, i) else Coordinate(i, bedViewModel.rows-1)
            val gridTileId = bedViewModel.tileIds?.get(coordinate)
            val gridTile = requireView().findViewById<Button>(gridTileId!!)
            binding.parentLayout.removeView(gridTile)

            bedViewModel.tileIds?.remove(coordinate)
        }
        if (column) bedViewModel.columns-- else bedViewModel.rows--
        snapButtonsToRestOfGrid(column)
    }

    private fun addTiles(column: Boolean) {
        for (i in 0 until if (column) bedViewModel.rows else bedViewModel.columns) {
            val coordinate = if (column) Coordinate(bedViewModel.columns, i) else Coordinate(i, bedViewModel.rows)
            val gridTile = GridTile(requireContext(), gridTileListener(coordinate), binding)
            binding.parentLayout.addView(gridTile)

            bedViewModel.tileIds?.set(coordinate, gridTile.id) //Update garden with new tile

            val prevTileId = bedViewModel.tileIds?.get(if (column) Coordinate(bedViewModel.columns-1, i) else Coordinate(i-1, bedViewModel.rows))
            val upperTileId = bedViewModel.tileIds?.get(if (column) Coordinate(bedViewModel.columns, i - 1) else Coordinate(i, bedViewModel.rows - 1))
            gridTile.snapToGrid(prevTileId, upperTileId, column)
        }
        if (column) bedViewModel.columns++ else bedViewModel.rows++
        snapButtonsToRestOfGrid(column)
    }

    private fun snapButtonsToRestOfGrid(column: Boolean) {
        val tileId = bedViewModel.tileIds?.get(if (column) Coordinate(bedViewModel.columns-1, 0) else Coordinate(0, bedViewModel.rows-1))
        val constraintSet = ConstraintSet()
        constraintSet.apply {
            clone(binding.parentLayout)
            if(column){
                connect(binding.addColumnButton.id, START, tileId!!, END)
                connect(binding.addColumnButton.id, TOP, binding.parentLayout.id, TOP)
            } else {
                connect(binding.addRowButton.id, TOP, tileId!!, BOTTOM)
                connect(binding.addRowButton.id, START, binding.parentLayout.id, START)
            }
            applyTo(binding.parentLayout)
        }
    }

    private fun changePlacementOfRemoveButton(column: Boolean, buttonId: Int){
        val constraintSet = ConstraintSet()
        constraintSet.apply {
            clone(binding.parentLayout)
            if(column) {
                connect(buttonId, START, binding.addColumnButton.id, START)
                connect(buttonId, TOP, binding.addColumnButton.id, BOTTOM)
            } else {
                connect(buttonId, START, binding.addRowButton.id, END)
                connect(buttonId, TOP, binding.addRowButton.id, TOP)
            }
            applyTo(binding.parentLayout)
        }
    }

    private fun navigateToChoosePlantFragment(coordinate: Coordinate) {
        findNavController().navigate(CreateGridFragmentDirections.choosePlantAction(coordinate, AllPlantsPredicate()))
    }
}
