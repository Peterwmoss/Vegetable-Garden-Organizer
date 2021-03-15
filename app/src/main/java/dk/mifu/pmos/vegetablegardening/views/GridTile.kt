package dk.mifu.pmos.vegetablegardening.views

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Constraints
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.databinding.FragmentCreateGridBinding
import dk.mifu.pmos.vegetablegardening.helpers.GridHelper
import dk.mifu.pmos.vegetablegardening.helpers.GridHelper.Companion.START
import dk.mifu.pmos.vegetablegardening.helpers.GridHelper.Companion.TOP
import dk.mifu.pmos.vegetablegardening.helpers.GridHelper.Companion.BOTTOM
import dk.mifu.pmos.vegetablegardening.helpers.GridHelper.Companion.END

@SuppressLint("ViewConstructor")
class GridTile(context: Context,
               onClickListener: OnClickListener,
               private val binding: FragmentCreateGridBinding): androidx.appcompat.widget.AppCompatButton(context) {
    init {
        id = View.generateViewId()
        setBackgroundResource(R.drawable.grid_tile)
        setPadding(0,0,0,0)
        layoutParams = setParams()
        setOnClickListener(onClickListener)
    }

    fun snapToGrid(prevTileId: Int?, upperTileId: Int?, column: Boolean) {
        val constraintSet = ConstraintSet()

        constraintSet.apply{
            clone(binding.parentLayout)

            if(prevTileId!=null) connect(id, START, prevTileId, END)
            else connect(id, START, binding.parentLayout.id, START)

            if(upperTileId!=null) connect(id, TOP, upperTileId, BOTTOM)
            else connect(id, TOP, binding.createGridGuideTextView.id, BOTTOM)

            if(column) connect(binding.addColumnButton.id, START, id, END)
            else connect(binding.addRowButton.id, TOP, id, BOTTOM)
            applyTo(binding.parentLayout)
        }
    }

    private fun setParams(): Constraints.LayoutParams{
        val params = Constraints.LayoutParams(
            GridHelper.getTileSideLength(),
            GridHelper.getTileSideLength()
        )
        params.setMargins(0,0,0,0)
        return params
    }
}