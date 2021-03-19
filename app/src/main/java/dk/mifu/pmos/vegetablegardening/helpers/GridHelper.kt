package dk.mifu.pmos.vegetablegardening.helpers

import android.content.Context
import android.content.res.Resources
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import dk.mifu.pmos.vegetablegardening.R

class GridHelper {
    companion object {
        private const val buttonSideLength = 160

        private fun toolBarSize(context: Context): Int {
            return context.resources.getDimension(R.dimen.toolbar).toInt()
        }

        fun getTileSideLength(): Int {
            val width = Resources.getSystem().displayMetrics.widthPixels - buttonSideLength
            return width shr 2 //Divide by 4
        }

        fun getWidthOfScreen(): Int {
            return Resources.getSystem().displayMetrics.widthPixels
        }

        fun getHeightOfScreen(): Int {
            return Resources.getSystem().displayMetrics.heightPixels
        }

        fun remainingHeight(rows: Int, context: Context): Int {
            return (getHeightOfScreen()
                    -(getTileSideLength()*rows)
                    -toolBarSize(context)
                    -buttonSideLength)
        }

        const val START = ConstraintSet.START
        const val END = ConstraintSet.END
        const val TOP = ConstraintSet.TOP
        const val BOTTOM = ConstraintSet.BOTTOM
    }
}