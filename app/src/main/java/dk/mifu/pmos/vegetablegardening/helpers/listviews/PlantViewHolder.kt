package dk.mifu.pmos.vegetablegardening.helpers.listviews

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.models.Plant

abstract class PlantViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val plantTextView: TextView = view.findViewById(R.id.choose_plant_row_item_text)
    lateinit var plant : Plant
}
