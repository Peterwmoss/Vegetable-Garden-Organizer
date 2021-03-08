package dk.mifu.pmos.vegetablegardening.helpers.callbacks

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.ObservableMap
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.MyPlant
import dk.mifu.pmos.vegetablegardening.models.Plant
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel

class IconCallback(private val view: View, private val bedViewModel: BedViewModel):  BedPlantsChangedCallback()  {
    override fun onMapChanged(sender: ObservableMap<Coordinate, MyPlant>?, key: Coordinate?) {
        val id = bedViewModel.tileIds?.get(key)!!
        val associatedButton = view.findViewById<AppCompatButton>(id)
        val layout = associatedButton.parent as FrameLayout
        layout.findViewById<ImageView>(R.id.icon_view).visibility = View.GONE
    }
}