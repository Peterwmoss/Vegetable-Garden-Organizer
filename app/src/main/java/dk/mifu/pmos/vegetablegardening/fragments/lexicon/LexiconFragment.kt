package dk.mifu.pmos.vegetablegardening.fragments.lexicon

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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.databinding.FragmentLexiconBinding
import dk.mifu.pmos.vegetablegardening.helpers.recyclerviews.PlantAdapter
import dk.mifu.pmos.vegetablegardening.helpers.recyclerviews.PlantViewHolder
import dk.mifu.pmos.vegetablegardening.helpers.search.PlantFilter
import dk.mifu.pmos.vegetablegardening.models.MyPlant
import dk.mifu.pmos.vegetablegardening.models.Plant
import dk.mifu.pmos.vegetablegardening.viewmodels.PlantViewModel
import java.util.*

class LexiconFragment: Fragment() {
    private lateinit var binding: FragmentLexiconBinding

    private val plantViewModel: PlantViewModel by activityViewModels()
    private var adapter : Adapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLexiconBinding.inflate(inflater, container, false)

        binding.searchPlantEdittext.setText("")
        plantViewModel.plants.observe(viewLifecycleOwner, {
            val recyclerView = binding.lexiconRecyclerView
            recyclerView.layoutManager = LinearLayoutManager(context)
            adapter = Adapter(it)
            recyclerView.adapter = adapter
            PlantFilter.setupSearch(adapter, binding.searchPlantEdittext)
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.searchPlantEdittext.setText("")
    }

    private inner class ViewHolder(view: View) : PlantViewHolder(view) {
        init {
            view.setOnClickListener {
                findNavController().navigate(LexiconFragmentDirections.toPlantDetails(plant, null))
            }
        }
    }

    private inner class Adapter(dataSet: List<Plant>) : PlantAdapter(dataSet) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_plant, parent, false)
            return ViewHolder(view)
        }
    }
}