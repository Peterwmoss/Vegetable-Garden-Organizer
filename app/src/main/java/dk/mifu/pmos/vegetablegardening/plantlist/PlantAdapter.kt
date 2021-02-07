package dk.mifu.pmos.vegetablegardening.plantlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.data.Plant
import java.util.*

class PlantAdapter(private val dataSet: List<Plant>) : RecyclerView.Adapter<PlantAdapter.ViewHolder>(), Filterable {
    private var flowingData: List<Plant> = dataSet

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val plantName: TextView = view.findViewById(R.id.choose_plant_row_item_text)

        init {
            view.setOnClickListener { /* Do something with plantName */ }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_plant, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.plantName.text = flowingData[position].name
    }

    override fun getItemCount() = flowingData.size

    // Search functionality

    private val filter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            if (constraint == null || constraint.isEmpty())
                results.values = dataSet
            else {
                val pattern = constraint.toString().toLowerCase(Locale.getDefault()).trim()
                results.values = dataSet.filter { plant -> plant.name.toLowerCase(Locale.getDefault()).contains(pattern) }
            }
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            flowingData = results!!.values as List<Plant>
            notifyDataSetChanged()
        }
    }

    override fun getFilter(): Filter {
        return filter
    }
}
