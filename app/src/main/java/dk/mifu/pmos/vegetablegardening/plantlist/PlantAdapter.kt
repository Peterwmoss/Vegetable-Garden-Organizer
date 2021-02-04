package dk.mifu.pmos.vegetablegardening.plantlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.data.Plant

class PlantAdapter(private val dataSet: Array<Plant>) : RecyclerView.Adapter<PlantAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val plantName: TextView = view.findViewById(R.id.choose_plant_row_item_text)

        init {
            view.setOnClickListener { Toast.makeText(view.context, "${plantName.text} pressed!", Toast.LENGTH_SHORT).show() }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_plant, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.plantName.text = dataSet[position].name
    }

    override fun getItemCount() = dataSet.size
}
