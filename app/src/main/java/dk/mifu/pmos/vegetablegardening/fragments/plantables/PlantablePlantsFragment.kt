package dk.mifu.pmos.vegetablegardening.fragments.plantables

import android.os.Bundle
import android.view.*
import android.widget.Adapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.Constraints
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.databinding.FragmentPlantablePlantsBinding
import dk.mifu.pmos.vegetablegardening.enums.BedLocation
import dk.mifu.pmos.vegetablegardening.fragments.viewgarden.AreaOverviewFragmentDirections
import dk.mifu.pmos.vegetablegardening.helpers.Formatter
import dk.mifu.pmos.vegetablegardening.helpers.predicates.LocationPlantPredicate
import dk.mifu.pmos.vegetablegardening.helpers.predicates.PlantablePredicate
import dk.mifu.pmos.vegetablegardening.models.Bed
import dk.mifu.pmos.vegetablegardening.models.Plant
import dk.mifu.pmos.vegetablegardening.viewmodels.PlantViewModel
import dk.mifu.pmos.vegetablegardening.views.Tooltip

class PlantablePlantsFragment: Fragment() {
    private lateinit var binding: FragmentPlantablePlantsBinding
    private val plantViewModel: PlantViewModel by activityViewModels()
    private var plantablesOutdoors: MutableList<Plant> = mutableListOf()
    private var plantablesGreenhouse: MutableList<Plant> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPlantablePlantsBinding.inflate(inflater, container, false)

        findPlantables()
        fillRecyclerViews()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.drawer_plantables)
    }

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
                Tooltip.newTooltip(requireContext(), getString(R.string.tooltip_plantables), requireView().rootView.findViewById(R.id.tooltip))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun findPlantables() {
        val plantables = plantViewModel.plants.value?.filter(PlantablePredicate())
        val greenhousePredicate = LocationPlantPredicate(BedLocation.Greenhouse)
        plantables?.forEach { plant ->
            if (greenhousePredicate.invoke(plant)) plantablesGreenhouse.add(plant)
            else plantablesOutdoors.add(plant)
        }
    }

    private fun fillRecyclerViews(){
        val outdoorsRecyclerView = binding.outdoorsPlantablesRecyclerview
        val greenhouseRecyclerView = binding.greenhousePlantablesRecyclerview
        val divider = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)

        outdoorsRecyclerView.layoutManager = LinearLayoutManager(context)
        outdoorsRecyclerView.addItemDecoration(divider)
        outdoorsRecyclerView.adapter = PlantablePlantsAdapter(plantablesOutdoors)

        greenhouseRecyclerView.layoutManager = LinearLayoutManager(context)
        greenhouseRecyclerView.addItemDecoration(divider)
        greenhouseRecyclerView.adapter = PlantablePlantsAdapter(plantablesGreenhouse)

        if(plantablesOutdoors.isEmpty()) binding.emptyOutdoorsText.visibility = View.VISIBLE
        if(plantablesGreenhouse.isEmpty()) binding.emptyGreenhouseText.visibility = View.VISIBLE
    }

    private inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val plantName: TextView = view.findViewById(R.id.plant_name)
        val earliest: TextView = view.findViewById(R.id.plant_earliest_text)
        val latest: TextView = view.findViewById(R.id.plant_latest_text)
    }

    private inner class PlantablePlantsAdapter(private val dataSet: List<Plant>): RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_plantable, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val plant = dataSet[position]
            val formatter = Formatter(requireContext())
            holder.plantName.text = plant.name
            holder.earliest.text = formatter.formatDate(plant.earliest)
            holder.latest.text = formatter.formatDate(plant.latest)
        }

        override fun getItemCount(): Int {
            return dataSet.size
        }
    }
}