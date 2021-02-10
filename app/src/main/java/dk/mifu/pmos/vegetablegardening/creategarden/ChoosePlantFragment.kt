package dk.mifu.pmos.vegetablegardening.creategarden

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.data.CurrentGardenViewModel
import dk.mifu.pmos.vegetablegardening.data.Plant
import dk.mifu.pmos.vegetablegardening.data.PlantViewModel
import java.util.*

class ChoosePlantFragment(private val coordinate: Pair<Int, Int>) : Fragment() {
    private val plantViewModel: PlantViewModel by activityViewModels()
    private val currentGardenViewModel: CurrentGardenViewModel by activityViewModels()

    private var adapter : PlantAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_choose_plant, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.choose_plant_recycler_view)
        createList(recyclerView)

        val search = view.findViewById<EditText>(R.id.search_plant_edittext)
        search.requestFocus()
        setupSearch(search)

        return view
    }

    private fun createList(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(context)
        val list = plantViewModel.plants
        adapter = PlantAdapter(list)
        recyclerView.adapter = adapter
    }

    private fun setupSearch(search: EditText) {
        search.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { /* Do nothing */ }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { /* Do nothing */ }

            override fun afterTextChanged(s: Editable?) {
                adapter?.filter?.filter(s.toString())
            }
        })
    }

    // Recyclerview functionality

    private inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val plantName: TextView = view.findViewById(R.id.choose_plant_row_item_text)

        init {
            view.setOnClickListener {
                currentGardenViewModel.garden.value!!.plants[coordinate] = Plant(plantName.text.toString())
            }
        }
    }

    private inner class PlantAdapter(private val dataSet: List<Plant>) : RecyclerView.Adapter<ViewHolder>(), Filterable {
        private var flowingData: List<Plant> = dataSet


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
}
