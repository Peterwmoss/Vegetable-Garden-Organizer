package dk.mifu.pmos.vegetablegardening.creategarden

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        // Setup recycler view with the plant adapter
        val recyclerView = view.findViewById<RecyclerView>(R.id.choose_plant_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val list = PlantViewModel().plants.toTypedArray()
        adapter = PlantAdapter(list)
        recyclerView.adapter = adapter

        return view
    }
}
