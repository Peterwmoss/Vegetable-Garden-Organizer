package dk.mifu.pmos.vegetablegardening.creategarden

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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


        val recyclerView = view.findViewById<RecyclerView>(R.id.choose_plant_recycler_view)
        createList(recyclerView)

        val search = view.findViewById<EditText>(R.id.search_plant_edittext)
        search.requestFocus()
        setupSearch(search)

        return view
    }

    private fun createList(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(context)
        val list = PlantViewModel().plants
        adapter = PlantAdapter(list)
        recyclerView.adapter = adapter
    }

    private fun setupSearch(search: EditText) {
        search.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { /* Do nothing */ }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { /* Do nothing */ }

            override fun afterTextChanged(s: Editable?) {
                adapter!!.filter.filter(s.toString())
            }

        })
    }
}
