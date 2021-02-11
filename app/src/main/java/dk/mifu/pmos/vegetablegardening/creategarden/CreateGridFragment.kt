package dk.mifu.pmos.vegetablegardening.creategarden

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private lateinit var garden: Garden

    private val START = ConstraintSet.START
    private val END = ConstraintSet.END
    private val TOP = ConstraintSet.TOP
    private val BOTTOM = ConstraintSet.BOTTOM

    //Initial number of grid tiles
    private var columns = 2;
    private var rows = 2;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        garden = gardenViewModel.garden.value!!
        return inflater.inflate(R.layout.fragment_create_grid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        insert_plant_btn.setOnClickListener {
            // TODO update when GridTiles starts fragment
            val choosePlantFragment = ChoosePlantFragment(Pair(0, 0))
            fragmentManager?.beginTransaction()?.add(R.id.fragment_container, choosePlantFragment)
                ?.commit()
        }

        add_column_right_button.setOnClickListener{
            for(i in 0 until rows){
                val gridTile = GridTile(requireContext())
                parent_layout.addView(gridTile)

                garden.tileIds[Pair(columns,i)] = gridTile.id //Update garden with new tile

                val prevTileId = garden.tileIds[Pair(columns-1,i)]
                val upperTileId = garden.tileIds[Pair(columns, i-1)] ?: insert_plant_btn.id //Choosing uppermost view component if no tile above
                gridTile.addToGrid(prevTileId!!, upperTileId, true)
            }
            columns++
        }

        add_row_button.setOnClickListener{
            for(i in 0 until columns){
                val constraintSet = ConstraintSet()
                val gridTile = GridTile(requireContext())
                parent_layout.addView(gridTile)

                garden.tileIds[Pair(i, rows)] = gridTile.id //Update garden with new tile

                val prevTileId = garden.tileIds[Pair(i-1, rows)] ?: add_column_left_button.id
                val upperTileId = garden.tileIds[Pair(i, rows-1)]
                gridTile.addToGrid(prevTileId, upperTileId!!, false)
            }
            rows++
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
        }

        private fun setParams(): Constraints.LayoutParams{
            val params = Constraints.LayoutParams(
                Constraints.LayoutParams.WRAP_CONTENT,
                Constraints.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0,0,0,0)
            return params
        }

        fun addToGrid(prevTileId: Int, upperTileId: Int, row: Boolean) {
            val constraintSet = ConstraintSet()

            constraintSet.apply{
                clone(parent_layout)
                connect(id, START, prevTileId, END)
                connect(id, TOP, upperTileId, BOTTOM)
                if(row){
                    connect(R.id.add_column_right_button, START, id, END)
                } else {
                    connect(R.id.add_row_button, TOP, id, BOTTOM)
                }
                applyTo(parent_layout)
            }
        }
    }
}
