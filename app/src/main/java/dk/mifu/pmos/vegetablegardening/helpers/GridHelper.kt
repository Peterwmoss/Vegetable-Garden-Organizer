package dk.mifu.pmos.vegetablegardening.helpers

import android.content.res.Resources
import androidx.constraintlayout.widget.ConstraintSet

class GridHelper {
    companion object {
        const val buttonSideLength = 160
        const val toolBarSize = 160

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

        fun remainingHeight(rows: Int): Int {
            return getHeightOfScreen()-(getTileSideLength()*rows)-buttonSideLength-toolBarSize
        }

        const val START = ConstraintSet.START
        const val END = ConstraintSet.END
        const val TOP = ConstraintSet.TOP
        const val BOTTOM = ConstraintSet.BOTTOM
    }
}