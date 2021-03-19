package dk.mifu.pmos.vegetablegardening.views

import android.content.Context
import android.view.View
import com.skydoves.balloon.*
import dk.mifu.pmos.vegetablegardening.R

class Tooltip {
    companion object {
        fun newTooltip(context: Context, text: String, view: View) {
            val tooltip = createBalloon(context) {
                setArrowSize(10)
                setArrowOrientation(ArrowOrientation.TOP)
                setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                setWidth(300)
                setBackgroundColorResource(R.color.dark_green)
                setAutoDismissDuration(10000L)
                setText(text)
                setTextSize(18f)
                setPadding(10)
                setBalloonAnimation(BalloonAnimation.FADE)
            }
            tooltip.show(view,0, 0)
        }
    }
}