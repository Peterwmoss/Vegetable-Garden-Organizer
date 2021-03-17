package dk.mifu.pmos.vegetablegardening.helpers.search

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Filter
import dk.mifu.pmos.vegetablegardening.helpers.recyclerviews.PlantAdapter
import dk.mifu.pmos.vegetablegardening.models.Plant
import java.util.*

abstract class PlantFilter(private val dataSet: Collection<Plant>) : Filter() {
    companion object {
        fun setupSearch(adapter: PlantAdapter?, search: EditText) {
            search.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { /* Do nothing */ }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { /* Do nothing */ }

                override fun afterTextChanged(s: Editable?) {
                    adapter?.filter?.filter(s.toString())
                }
            })
        }
    }

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
}