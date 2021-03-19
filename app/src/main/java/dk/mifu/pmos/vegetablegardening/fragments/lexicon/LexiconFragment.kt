package dk.mifu.pmos.vegetablegardening.fragments.lexicon

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.databinding.FragmentLexiconBinding
import dk.mifu.pmos.vegetablegardening.helpers.recyclerviews.PlantAdapter
import dk.mifu.pmos.vegetablegardening.helpers.recyclerviews.PlantViewHolder
import dk.mifu.pmos.vegetablegardening.helpers.search.PlantFilter
import dk.mifu.pmos.vegetablegardening.models.Plant
import dk.mifu.pmos.vegetablegardening.viewmodels.PlantViewModel
import dk.mifu.pmos.vegetablegardening.views.Tooltip
import java.util.*

class LexiconFragment: Fragment() {
    private lateinit var binding: FragmentLexiconBinding

    private val plantViewModel: PlantViewModel by activityViewModels()
    private var adapter : Adapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.tooltip -> {
                Tooltip.newTooltip(requireContext(), getString(R.string.tooltip_lexicon), requireView().rootView.findViewById(R.id.tooltip))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.plants)
    }

    override fun onResume() {
        super.onResume()
        binding.searchPlantEdittext.setText("")
    }

    private inner class ViewHolder(view: View) : PlantViewHolder(view) {
        init {
            view.setOnClickListener {
                findNavController().navigate(LexiconFragmentDirections.toPlantDetails(plant, null, null))
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