package dk.mifu.pmos.vegetablegardening.creategarden

import android.content.Context
import android.media.Image
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Constraints
import androidx.fragment.app.Fragment
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.data.Garden
import kotlinx.android.synthetic.main.fragment_create_grid.*

class CreateGridFragment() : Fragment() {
    private lateinit var garden: Garden

    private var START = ConstraintSet.START
    private var END = ConstraintSet.END
    private var TOP = ConstraintSet.TOP
    private var BOTTOM = ConstraintSet.BOTTOM

    //Initial number of grid tiles
    private var columns = 2;
    private var rows = 2;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        garden = (activity as CreateGardenActivity).gardenViewModel.gardens[0]
        return inflater.inflate(R.layout.fragment_create_grid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        location_text_view.text = "Yeet"

        add_column_button.setOnClickListener{
            for(i in 0 until rows){
                val constraintSet = ConstraintSet()
                val gridTile = GridTile(context!!)
                parent_layout.addView(gridTile)

                garden.tileIds[Pair(columns,i)] = gridTile.id //Update garden with new tile

                val prevTileId = garden.tileIds[Pair(columns-1,i)]
                val upperTileId = garden.tileIds[Pair(columns, i-1)] ?: location_text_view.id

                constraintSet.apply{
                    clone(parent_layout)
                    connect(gridTile.id, START, prevTileId!!, END)
                    connect(gridTile.id, TOP, upperTileId, BOTTOM)
                    connect(R.id.add_column_button, START, gridTile.id, END)
                    applyTo(parent_layout)
                }
            }
            columns += 1
        }
    }

    private inner class GridTile(context: Context): androidx.appcompat.widget.AppCompatImageButton(context) {
        init {
            setImageResource(R.drawable.grid_tile)
            scaleType = ScaleType.FIT_CENTER
            adjustViewBounds = true
            background = null
            id = View.generateViewId()
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
    }
}
