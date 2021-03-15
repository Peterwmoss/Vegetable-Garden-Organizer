package dk.mifu.pmos.vegetablegardening.helpers.recyclerviews

import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import dk.mifu.pmos.vegetablegardening.helpers.search.PlantFilter
import dk.mifu.pmos.vegetablegardening.models.Plant

abstract class PlantAdapter(private val dataSet: List<Plant>) : RecyclerView.Adapter<PlantViewHolder>(), Filterable {
    private var flowingData: List<Plant> = dataSet

    override fun onBindViewHolder(viewHolder: PlantViewHolder, position: Int) {
        viewHolder.plantTextView.text = flowingData[position].name
        viewHolder.plant = flowingData[position]
    }

    override fun getItemCount() = flowingData.size

    // Search functionality

    private val filter: Filter = object : PlantFilter(dataSet) {
        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            flowingData = results!!.values as List<Plant>
            notifyDataSetChanged()
        }
    }

    override fun getFilter(): Filter {
        return filter
    }
}