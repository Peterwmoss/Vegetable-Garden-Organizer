package dk.mifu.pmos.vegetablegardening.creategarden

import android.os.Bundle
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

    //Initial number of grid tiles
    var columns = 2;
    var rows = 2;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        garden = (activity as CreateGardenActivity).gardenViewModel.gardens[0]
        return inflater.inflate(R.layout.fragment_create_grid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        location_text_view.text = "Yeet"

        add_column_button.setOnClickListener{
            for(i in 0 until columns){
                val constraintSet = ConstraintSet()
                val gridTile = ImageButton(context)

                val params = Constraints.LayoutParams(
                    Constraints.LayoutParams.WRAP_CONTENT,
                    Constraints.LayoutParams.WRAP_CONTENT
                )

                params.setMargins(0,0,0,0)

                gridTile.setImageResource(R.drawable.grid_tile)
                gridTile.scaleType = ImageView.ScaleType.FIT_CENTER
                gridTile.adjustViewBounds = true
                gridTile.background = null
                gridTile.id = View.generateViewId()
                gridTile.setPadding(0,0,0,0)
                gridTile.layoutParams = params
                parent_layout.addView(gridTile)

                garden.tileIds[Pair(columns,i)] = gridTile.id

                val prevTileId = garden.tileIds[Pair(columns-1,i)]
                val upperTileId = garden.tileIds[Pair(columns, i-1)] ?: location_text_view.id

                constraintSet.clone(parent_layout)
                constraintSet.connect(gridTile.id, ConstraintSet.START, prevTileId!!, ConstraintSet.END)
                constraintSet.connect(gridTile.id, ConstraintSet.TOP, upperTileId, ConstraintSet.BOTTOM)
                constraintSet.connect(R.id.add_column_button, ConstraintSet.START, gridTile.id, ConstraintSet.END)
                constraintSet.applyTo(parent_layout)
            }
        }
    }
}
