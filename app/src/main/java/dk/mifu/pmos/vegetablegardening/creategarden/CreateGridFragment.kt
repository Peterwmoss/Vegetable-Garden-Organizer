package dk.mifu.pmos.vegetablegardening.creategarden

import android.content.res.Resources
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.findNavController
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.viewmodels.CurrentGardenViewModel
import dk.mifu.pmos.vegetablegardening.models.Garden
import dk.mifu.pmos.vegetablegardening.databinding.FragmentCreateGridBinding
import dk.mifu.pmos.vegetablegardening.views.GridTile

class CreateGridFragment : Fragment() {
    private lateinit var binding: FragmentCreateGridBinding
  
    private val gardenViewModel: CurrentGardenViewModel by activityViewModels()
    private var height = 0
    private var width = 0
    private var tileSideLength = 0
    private lateinit var garden: Garden

    private val START = ConstraintSet.START
    private val END = ConstraintSet.END
    private val TOP = ConstraintSet.TOP
    private val BOTTOM = ConstraintSet.BOTTOM

    //Initial number of grid tiles
    private var columns = 1
    private var rows = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        garden = gardenViewModel.garden.value!!
        binding = FragmentCreateGridBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        width = Resources.getSystem().displayMetrics.widthPixels
        height = Resources.getSystem().displayMetrics.heightPixels
        tileSideLength = width/4

        insertInitialGridTiles()
        setListeners()
    }

    private fun insertInitialGridTiles(){
        val initialTile1 = GridTile(requireContext(), binding, tileSideLength)
        binding.parentLayout.addView(initialTile1)
        garden.tileIds[Coordinate(0,0)] = initialTile1.id
        initialTile1.snapToGrid(null,null,true)

        val initialTile2 = GridTile(requireContext(), binding, tileSideLength)
        binding.parentLayout.addView(initialTile2)
        garden.tileIds[Coordinate(0,1)] = initialTile2.id
        initialTile2.snapToGrid(null,null,false)

        addColumn()
        addRow()
    }

    private fun setListeners() {
        binding.insertPlantBtn.setOnClickListener {
            // TODO update when GridTiles starts fragment
            requireView().findNavController().navigate(CreateGridFragmentDirections.choosePlantAction(
                Coordinate(0,0)
            ))
        }

        binding.addColumnButton.setOnClickListener{
            addColumn()
            if(columns==4){
                binding.addColumnButton.visibility = View.GONE
                changePlacementOfRemoveButton(
                    full = true,
                    column = true,
                    buttonId = binding.removeColumnButton.id
                )
            }
            if(binding.removeColumnButton.visibility == View.GONE){
                binding.removeColumnButton.visibility = View.VISIBLE
            }
        }

        binding.addRowButton.setOnClickListener{
            addRow()
            if(height-(tileSideLength*rows) < tileSideLength){ //If there isn't enough room for a whole row more
                binding.addRowButton.visibility = View.GONE
                changePlacementOfRemoveButton(
                    full = true,
                    column = false,
                    buttonId = binding.removeRowButton.id
                )
            }
            if(binding.removeRowButton.visibility == View.GONE){
                binding.removeRowButton.visibility = View.VISIBLE
            }
        }

        binding.removeColumnButton.setOnClickListener{
            removeColumn()
            if(binding.addColumnButton.visibility == View.GONE){
                binding.addColumnButton.visibility = View.VISIBLE
                changePlacementOfRemoveButton(
                    full = false,
                    column = true,
                    buttonId = binding.removeColumnButton.id
                )
            }
            if(columns==2){
                binding.removeColumnButton.visibility = View.GONE
            }
        }

        binding.removeRowButton.setOnClickListener{
            removeRow()
            if(binding.addRowButton.visibility == View.GONE){
                binding.addRowButton.visibility = View.VISIBLE
                changePlacementOfRemoveButton(
                    full = false,
                    column = false,
                    buttonId = binding.removeRowButton.id
                )
            }

            if(rows==2){
                binding.removeRowButton.visibility = View.GONE
            }
        }
    }

    private fun removeColumn() {
        for (i in 0 until rows)
            removeTile(Coordinate(columns-1, i))
        val prevTileId = garden.tileIds[Coordinate(columns-2, 0)]
        snapButtonsToRestOfGrid(prevTileId!!, true)
        columns--
    }

    private fun removeRow() {
        for (i in 0 until columns)
            removeTile(Coordinate(i, rows-1))
        val upperTileId = garden.tileIds[Coordinate(0, rows-2)]
        snapButtonsToRestOfGrid(upperTileId!!, false)
        rows--
    }

    private fun removeTile(coordinate: Coordinate){
        val gridTileId = garden.tileIds[coordinate]
        val gridTile = requireView().findViewById<Button>(gridTileId!!)
        binding.parentLayout.removeView(gridTile)

        garden.tileIds.remove(coordinate)
    }

    private fun addColumn() {
        for (i in 0 until rows) {
            val gridTile = GridTile(requireContext(), binding, tileSideLength)
            binding.parentLayout.addView(gridTile)

            garden.tileIds[Coordinate(columns, i)] = gridTile.id //Update garden with new tile

            val prevTileId = garden.tileIds[Coordinate(columns-1, i)]
            val upperTileId = garden.tileIds[Coordinate(columns, i - 1)]
            gridTile.snapToGrid(prevTileId!!, upperTileId, true)
        }
        columns++
    }

    private fun addRow() {
        for (i in 0 until columns) {
            val gridTile = GridTile(requireContext(), binding, tileSideLength)
            binding.parentLayout.addView(gridTile)

            garden.tileIds[Coordinate(i, rows)] = gridTile.id //Update garden with new tile

            val prevTileId = garden.tileIds[Coordinate(i-1, rows)]
            val upperTileId = garden.tileIds[Coordinate(i, rows - 1)]
            gridTile.snapToGrid(prevTileId, upperTileId!!, false)
        }
        rows++
    }

    private fun snapButtonsToRestOfGrid(tileId: Int, column: Boolean) {
        val constraintSet = ConstraintSet()
        constraintSet.apply {
            clone(binding.parentLayout)
            if(column){
                connect(binding.addColumnButton.id, START, tileId, END)
                connect(binding.addColumnButton.id, TOP, binding.parentLayout.id, TOP)
            } else {
                connect(binding.addRowButton.id, TOP, tileId, BOTTOM)
                connect(binding.addRowButton.id, START, binding.parentLayout.id, START)
            }
            applyTo(binding.parentLayout)
        }
    }

    private fun changePlacementOfRemoveButton(full: Boolean, column: Boolean, buttonId: Int){
        val furthestTileId = garden.tileIds[if (column) Coordinate(columns-1,0) else Coordinate(0, rows-1)]
        val constraintSet = ConstraintSet()
        constraintSet.apply {
            clone(binding.parentLayout)
            if(full){
                connect(buttonId, START, if(column) furthestTileId!! else binding.parentLayout.id, START)
                connect(buttonId, TOP, if(column) binding.parentLayout.id else furthestTileId!!, TOP)
            } else {
                if(column) {
                    connect(buttonId, START, binding.addColumnButton.id, START)
                    connect(buttonId, TOP, binding.addColumnButton.id, BOTTOM)
                } else {
                    connect(buttonId, START, binding.addRowButton.id, END)
                    connect(buttonId, TOP, binding.addRowButton.id, TOP)
                }
            }

            applyTo(binding.parentLayout)
        }
    }
}
