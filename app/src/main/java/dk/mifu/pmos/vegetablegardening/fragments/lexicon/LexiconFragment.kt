package dk.mifu.pmos.vegetablegardening.fragments.lexicon

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.database.AppDatabase
import dk.mifu.pmos.vegetablegardening.database.PlantRepository
import dk.mifu.pmos.vegetablegardening.databinding.FragmentLexiconBinding
import dk.mifu.pmos.vegetablegardening.helpers.KeyboardHelper
import dk.mifu.pmos.vegetablegardening.helpers.recyclerviews.PlantAdapter
import dk.mifu.pmos.vegetablegardening.helpers.recyclerviews.PlantViewHolder
import dk.mifu.pmos.vegetablegardening.helpers.search.PlantFilter
import dk.mifu.pmos.vegetablegardening.models.Plant
import dk.mifu.pmos.vegetablegardening.viewmodels.PlantViewModel
import dk.mifu.pmos.vegetablegardening.views.Tooltip
import kotlinx.coroutines.*

class LexiconFragment: Fragment() {
    private lateinit var binding: FragmentLexiconBinding

    private val plantViewModel: PlantViewModel by activityViewModels()
    private var adapter : Adapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_default, menu)
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
        plantViewModel.plants.observe(viewLifecycleOwner, { plants ->
            MainScope().launch(Dispatchers.Main) {
                getUserPlants().observe(viewLifecycleOwner, { userPlants ->
                    userPlants.addAll(plants)
                    val recyclerView = binding.lexiconRecyclerView
                    recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    adapter = Adapter(userPlants)
                    recyclerView.adapter = adapter
                    PlantFilter.setupSearch(adapter, binding.searchPlantEdittext)
                })
            }
        })

        binding.newPlantBtn.setOnClickListener {
            findNavController().navigate(LexiconFragmentDirections.toCustomPlantDialog())
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.plants)
    }

    override fun onResume() {
        super.onResume()
        binding.searchPlantEdittext.setText("")
    }

    private suspend fun getUserPlants(): LiveData<MutableList<Plant>> {
        return withContext(Dispatchers.IO) {
            val dao = AppDatabase.getDatabase(requireContext()).plantDao()
            val repository = PlantRepository(dao)
            return@withContext repository.getAllPlants()
        }
    }

    private inner class ViewHolder(view: View) : PlantViewHolder(view) {
        init {
            view.setOnClickListener {
                findNavController().navigate(LexiconFragmentDirections.toPlantDetails(plant))
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