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

    private lateinit var recyclerView: RecyclerView
    private var adapter : PlantAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_choose_plant, container, false)
        recyclerView = view.findViewById(R.id.choose_plant_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        updateUI()

        return view
    }

    private fun updateUI() {
        val list = PlantViewModel().plants.toTypedArray()
        adapter = PlantAdapter(list)
        recyclerView.adapter = adapter
    }
}