package dk.mifu.pmos.vegetablegardening.creategarden

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Constraints
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.data.Coordinate
import dk.mifu.pmos.vegetablegardening.data.CurrentGardenViewModel
import dk.mifu.pmos.vegetablegardening.data.Garden
import dk.mifu.pmos.vegetablegardening.databinding.FragmentCreateGridBinding

class CreateGridFragment : Fragment() {
    private lateinit var binding: FragmentCreateGridBinding
  
    private val gardenViewModel: CurrentGardenViewModel by activityViewModels()
    private var height = 0
    private var width = 0
    private var gridSide = 0
    private lateinit var garden: Garden

    private val START = ConstraintSet.START
    private val END = ConstraintSet.END
    private val TOP = ConstraintSet.TOP
    private val BOTTOM = ConstraintSet.BOTTOM

    //Initial number of grid tiles
    private var columns = 1
    private var rows = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        garden = gardenViewModel.garden.value!!
        binding = FragmentCreateGridBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        width = Resources.getSystem().displayMetrics.widthPixels
        height = Resources.getSystem().displayMetrics.heightPixels
        gridSide = width/4

        insertInitialGridTiles()
        setInitialVisibilityOfButtons()

        binding.insertPlantBtn.setOnClickListener {
            // TODO update when GridTiles starts fragment
            requireView().findNavController().navigate(CreateGridFragmentDirections.choosePlantAction(
                Coordinate(0,0)
            ))
        }

        binding.addColumnButton.setOnClickListener{
            addColumn()
            if(columns==4){
                add_column_button.visibility = View.GONE
                changePlacementOfRemoveColumnButton(true)
            }
            if(remove_column_button.visibility == View.GONE){
                remove_column_button.visibility = View.VISIBLE
            }
        }

        binding.addRowButton.setOnClickListener{
            addRow()
            if(height-(gridSide*rows) < gridSide){ //If there isn't enough room for a whole row more
                add_row_button.visibility = View.GONE
                changePlacementOfRemoveRowButton(true)
            }
            if(remove_row_button.visibility == View.GONE){
                remove_row_button.visibility = View.VISIBLE
            }
        }

        binding.removeColumnButton.setOnClickListener{
            removeColumn()
            if(add_column_button.visibility == View.GONE){
                add_column_button.visibility = View.VISIBLE
                changePlacementOfRemoveColumnButton(false)
            }
            if(columns==2){
                remove_column_button.visibility = View.GONE
            }
        }

        binding.removeRowButton.setOnClickListener{
            removeRow()
            if(add_row_button.visibility == View.GONE){
                add_row_button.visibility = View.VISIBLE
                changePlacementOfRemoveRowButton(false)
            }

            if(rows==2){
                remove_row_button.visibility = View.GONE
            }
        }
    }

    private fun insertInitialGridTiles(){
        val initialTile1 = GridTile(requireContext())
        garden.tileIds[Coordinate(0,0)] = initialTile1.id
        initialTile1.snapToGrid(null,null,true)

        val initialTile2 = GridTile(requireContext())
        garden.tileIds[Coordinate(0,1)] = initialTile2.id
        initialTile2.snapToGrid(null,null,false)

        addColumn()
        addRow()
    }

    private fun setInitialVisibilityOfButtons(){
        binding.removeRowButton.visibility = View.GONE
        binding.removeColumnButton.visibility = View.GONE
    }

    private fun removeColumn() {
        for (i in 0 until rows){
            removeTile(Coordinate(columns-1, i))

            val prevTileId = garden.tileIds[Coordinate(columns-2, i)]
            snapButtonsToRestOfGrid(prevTileId!!, true)
        }
        columns--
    }

    private fun removeRow() {
        for (i in 0 until columns){
            removeTile(Coordinate(i, rows-1))

            val upperTileId = garden.tileIds[Coordinate(i, rows-2)]
            snapButtonsToRestOfGrid(upperTileId!!, false)
        }
        rows--
    }

    private fun removeTile(coordinate: Coordinate){
        val gridTileId = garden.tileIds[coordinate]
        val gridTile = requireView().findViewById<ImageButton>(gridTileId!!)
        parent_layout.removeView(gridTile)

        garden.tileIds[coordinate] = 0 //Remove tile from garden
    }

    private fun addColumn() {
        for (i in 0 until rows) {
            val gridTile = GridTile(requireContext())

            garden.tileIds[Coordinate(columns, i)] = gridTile.id //Update garden with new tile

            val prevTileId = garden.tileIds[Coordinate(columns-1, i)]
            val upperTileId = garden.tileIds[Coordinate(columns, i - 1)]
            gridTile.snapToGrid(prevTileId!!, upperTileId, true)
        }
        columns++
    }

    private fun addRow() {
        for (i in 0 until columns) {
            val gridTile = GridTile(requireContext())

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
            clone(parent_layout)
            if(column){
                connect(add_column_button.id, START, tileId, END)
                connect(add_column_button.id, TOP, parent_layout.id, TOP)
            } else {
                connect(add_row_button.id, TOP, tileId, BOTTOM)
                connect(add_row_button.id, START, parent_layout.id, START)
            }
            applyTo(parent_layout)
        }
    }

    private fun changePlacementOfRemoveColumnButton(full: Boolean){
        val furthestTileId = garden.tileIds[Coordinate(columns-1,rows-1)]
        val constraintSet = ConstraintSet()
        constraintSet.apply {
            clone(parent_layout)
            if(full){
                connect(remove_column_button.id, START, furthestTileId!!, START)
                connect(remove_column_button.id, TOP, parent_layout.id, TOP)
            } else {
                connect(remove_column_button.id, START, add_column_button.id, START)
                connect(remove_column_button.id, TOP, add_column_button.id, BOTTOM)
            }

            applyTo(parent_layout)
        }
    }

    private fun changePlacementOfRemoveRowButton(full: Boolean){
        val furthestTileId = garden.tileIds[Coordinate(columns-1,rows-1)]
        val constraintSet = ConstraintSet()
        constraintSet.apply {
            clone(parent_layout)
            if(full){
                connect(remove_row_button.id, START, parent_layout.id, START)
                connect(remove_row_button.id, TOP, furthestTileId!!, TOP)
            } else {
                connect(remove_row_button.id, START, add_row_button.id, END)
                connect(remove_row_button.id, TOP, add_row_button.id, TOP)
            }

            applyTo(parent_layout)
        }
    }

    private inner class GridTile(context: Context): androidx.appcompat.widget.AppCompatImageButton(context) {
        init {
            id = View.generateViewId()

            //Picture
            setImageResource(R.drawable.grid_tile)
            scaleType = ScaleType.FIT_CENTER
            adjustViewBounds = true
            background = null

            //Layout
            setPadding(0,0,0,0)
            layoutParams = setParams()

            //View
            parent_layout.addView(this)
        }

        fun snapToGrid(prevTileId: Int?, upperTileId: Int?, row: Boolean) {
            val constraintSet = ConstraintSet()

            constraintSet.apply{
                clone(parent_layout)

                if(prevTileId!=null){
                    connect(id, START, prevTileId, END)
                } else {
                    connect(id, START, parent_layout.id, START)
                }

                if(upperTileId!=null){
                    connect(id, TOP, upperTileId, BOTTOM)
                } else {
                    connect(id, TOP, parent_layout.id, TOP)
                }

                if(row){
                    connect(R.id.add_column_button, START, id, END)
                } else {
                    connect(R.id.add_row_button, TOP, id, BOTTOM)
                }
                applyTo(parent_layout)
            }
        }
    }

    fun setParams(): Constraints.LayoutParams{
        val params = Constraints.LayoutParams(
            gridSide,
            gridSide
        )
        params.setMargins(0,0,0,0)
        return params
    }
}
