package dk.mifu.pmos.vegetablegardening.creategarden

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.data.PlantViewModel
import dk.mifu.pmos.vegetablegardening.plantlist.PlantAdapter

class ChoosePlantFragment : Fragment() {

    private var adapter : PlantAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_choose_plant, container, false)

        createList(view)
        setupSearch(view, adapter!!)

        return view
    }

    private fun createList(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.choose_plant_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val list = PlantViewModel().plants
        adapter = PlantAdapter(list)
        recyclerView.adapter = adapter
    }

    private fun setupSearch(view: View, adapter: PlantAdapter) {
        val search = view.findViewById<SearchView>(R.id.search_plant_searchview)
        val listener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter.filter(query)
                return false
            }
        }
        search.setOnQueryTextListener(listener)
    }
}
