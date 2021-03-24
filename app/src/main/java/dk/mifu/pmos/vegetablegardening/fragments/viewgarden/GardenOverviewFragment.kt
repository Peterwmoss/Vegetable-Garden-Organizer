package dk.mifu.pmos.vegetablegardening.fragments.viewgarden

import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Constraints
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.database.BedDao
import dk.mifu.pmos.vegetablegardening.database.BedRepository
import dk.mifu.pmos.vegetablegardening.database.AppDatabase
import dk.mifu.pmos.vegetablegardening.databinding.FragmentGardenOverviewBinding
import dk.mifu.pmos.vegetablegardening.enums.BedLocation.Greenhouse
import dk.mifu.pmos.vegetablegardening.enums.BedLocation.Outdoors
import dk.mifu.pmos.vegetablegardening.helpers.predicates.CurrentSeasonPredicate
import dk.mifu.pmos.vegetablegardening.models.Bed
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel
import dk.mifu.pmos.vegetablegardening.viewmodels.SeasonViewModel
import dk.mifu.pmos.vegetablegardening.views.Tooltip

class GardenOverviewFragment : Fragment() {
    private lateinit var binding: FragmentGardenOverviewBinding

    private val bedViewModel: BedViewModel by activityViewModels()
    private val seasonViewModel: SeasonViewModel by activityViewModels()
    private var gardenDb: BedDao? = null
    private var repository: BedRepository? = null

    private lateinit var adapter: GardenOverviewAdapter

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
                if (adapter.itemCount == 0)
                    Tooltip.newTooltip(requireContext(), getString(R.string.tooltip_empty_garden), requireView().rootView.findViewById(R.id.tooltip))
                else
                    Tooltip.newTooltip(requireContext(), getString(R.string.tooltip_garden_with_beds), requireView().rootView.findViewById(R.id.tooltip))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentGardenOverviewBinding.inflate(inflater, container, false)
        gardenDb = AppDatabase.getDatabase(requireContext()).bedDao()
        repository = BedRepository(gardenDb!!)

        val recyclerView = binding.gardensRecyclerView

        recyclerView.layoutManager = GridLayoutManager(context, 2)
        repository?.getAllBeds()?.observe(viewLifecycleOwner, { list ->
            seasonViewModel.currentSeason.observe(viewLifecycleOwner, {
                adapter = GardenOverviewAdapter(list.filter(CurrentSeasonPredicate(it)))
                recyclerView.adapter = adapter
                setExplanatoryTextBasedOnItemCount()
            })
        })

        binding.newLocationBtn.setOnClickListener {
            navigateToSpecifyLocationFragment()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.title = seasonViewModel.currentSeason.value.toString()
        bedViewModel.clear()
    }

    private fun setExplanatoryTextBasedOnItemCount(){
        if (adapter.itemCount == 0)
            binding.gardenTextView.visibility = View.VISIBLE
    }

    private inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bedImage: ImageButton = view.findViewById(R.id.bed_image_button)
        val bedName: TextView = view.findViewById(R.id.bed_name_text)
        lateinit var bed: Bed

        init {
            bedImage.setOnClickListener {
                bedViewModel.setBed(bed)
                navigateToBedOverviewFragment()
            }
        }
    }

    private inner class GardenOverviewAdapter(private val dataSet: List<Bed>): RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_garden, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bedName.text = dataSet[position].name
            holder.bed = dataSet[position]

            when (dataSet[position].bedLocation) {
                Outdoors -> holder.bedImage.setImageResource(R.drawable.outdoors)
                Greenhouse -> holder.bedImage.setImageResource(R.drawable.greenhouse)
            }

            holder.bedImage.layoutParams = Constraints.LayoutParams(
                    Constraints.LayoutParams.WRAP_CONTENT,
                    Constraints.LayoutParams.WRAP_CONTENT
            )
        }

        override fun getItemCount(): Int {
            return dataSet.size
        }
    }

    private fun navigateToSpecifyLocationFragment() {
        findNavController().navigate(GardenOverviewFragmentDirections.toSpecifyLocation())
    }

    private fun navigateToBedOverviewFragment() {
        findNavController().navigate(GardenOverviewFragmentDirections.seeBedAction())
    }
}