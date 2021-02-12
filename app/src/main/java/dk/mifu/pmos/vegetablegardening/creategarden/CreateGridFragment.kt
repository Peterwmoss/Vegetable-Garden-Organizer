package dk.mifu.pmos.vegetablegardening.creategarden

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.*
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Constraints
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.data.CurrentGardenViewModel
import dk.mifu.pmos.vegetablegardening.data.Garden
import kotlinx.android.synthetic.main.fragment_create_grid.*

class CreateGridFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_create_grid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        width = Resources.getSystem().displayMetrics.widthPixels
        height = Resources.getSystem().displayMetrics.heightPixels
        gridSide = width/4

        insertInitialGridTiles()

        insert_plant_btn.setOnClickListener {
            // TODO update when GridTiles starts fragment
            val choosePlantFragment = ChoosePlantFragment(Pair(0, 0))
            fragmentManager?.beginTransaction()?.add(R.id.fragment_container, choosePlantFragment)
                ?.commit()
        }

        add_column_button.setOnClickListener{
            addColumn()
            if(columns==4){
                add_column_button.visibility = View.GONE
            }
        }

        add_row_button.setOnClickListener{
            addRow()
            if(height-(gridSide*rows) < gridSide){ //If there isn't enough room for a whole row more
                add_row_button.visibility = View.GONE
            }
        }
    }

    private fun insertInitialGridTiles(){
        val initialTile1 = GridTile(requireContext())
        parent_layout.addView(initialTile1)
        garden.tileIds[Pair(0,0)] = initialTile1.id
        initialTile1.snapToGrid(null,null,true)

        val initialTile2 = GridTile(requireContext())
        parent_layout.addView(initialTile2)
        garden.tileIds[Pair(0,1)] = initialTile2.id
        initialTile2.snapToGrid(null,null,false)

        addColumn()
        addRow()
    }

    private fun addColumn() {
        for (i in 0 until rows) {
            val gridTile = GridTile(requireContext())
            parent_layout.addView(gridTile)

            garden.tileIds[Pair(columns, i)] = gridTile.id //Update garden with new tile

            val prevTileId = garden.tileIds[Pair(columns - 1, i)]
            val upperTileId = garden.tileIds[Pair(columns, i - 1)]
            gridTile.snapToGrid(prevTileId!!, upperTileId, true)
        }
        columns++
    }

    private fun addRow() {
        for (i in 0 until columns) {
            val gridTile = GridTile(requireContext())
            parent_layout.addView(gridTile)

            garden.tileIds[Pair(i, rows)] = gridTile.id //Update garden with new tile

            val prevTileId = garden.tileIds[Pair(i - 1, rows)]
            val upperTileId = garden.tileIds[Pair(i, rows - 1)]
            gridTile.snapToGrid(prevTileId, upperTileId!!, false)
        }
        rows++
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
