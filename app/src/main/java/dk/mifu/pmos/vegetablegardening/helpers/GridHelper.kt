package dk.mifu.pmos.vegetablegardening.helpers

import android.content.res.Resources

class GridHelper {
    companion object {
        fun getTileSideLength(): Int {
            val width = Resources.getSystem().displayMetrics.widthPixels
            return width/4
        }

        fun getWidthOfScreen(): Int {
            return Resources.getSystem().displayMetrics.widthPixels
        }

        fun getHeightOfScreen(): Int {
            return Resources.getSystem().displayMetrics.heightPixels
        }
    }
}