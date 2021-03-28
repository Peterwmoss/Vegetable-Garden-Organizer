package dk.mifu.pmos.vegetablegardening.fragments.viewgarden

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.Constraints
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.database.BedDao
import dk.mifu.pmos.vegetablegardening.database.BedRepository
import dk.mifu.pmos.vegetablegardening.database.AppDatabase
import dk.mifu.pmos.vegetablegardening.databinding.FragmentAreaOverviewBinding
import dk.mifu.pmos.vegetablegardening.helpers.predicates.CurrentSeasonPredicate
import dk.mifu.pmos.vegetablegardening.models.Bed
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel
import dk.mifu.pmos.vegetablegardening.viewmodels.SeasonViewModel
import dk.mifu.pmos.vegetablegardening.views.Tooltip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class AreaOverviewFragment : Fragment() {
    private lateinit var binding: FragmentAreaOverviewBinding
    private lateinit var adapter: AreaOverviewAdapter
    private lateinit var updatedBeds: MutableList<Bed>

    private val bedViewModel: BedViewModel by activityViewModels()
    private val seasonViewModel: SeasonViewModel by activityViewModels()
    private var repository: BedRepository? = null
    private val args: AreaOverviewFragmentArgs by navArgs()

    private val bedDragHelper by lazy {
        val bedDragCallback =
                object : ItemTouchHelper.SimpleCallback(UP or DOWN or START or END, 0) {
                    override fun onMove(
                            recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder): Boolean {

                        viewHolder as ViewHolder

                        val from = viewHolder.adapterPosition
                        val to = target.adapterPosition
                        val bed = viewHolder.bed

                        adapter.moveItem(bed, from, to)
                        adapter.notifyItemMoved(from, to)

                        return true
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
                }
        ItemTouchHelper(bedDragCallback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bedViewModel.clear()
        bedViewModel.bedLocation = args.location
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_default, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.tooltip -> {
                if (adapter.itemCount == 0)
                    Tooltip.newTooltip(requireContext(), getString(R.string.guide_empty_garden), requireView().rootView.findViewById(R.id.tooltip))
                else
                    Tooltip.newTooltip(requireContext(), getString(R.string.tooltip_area_overview), requireView().rootView.findViewById(R.id.tooltip))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAreaOverviewBinding.inflate(inflater, container, false)
        val bedDao = AppDatabase.getDatabase(requireContext()).bedDao()
        repository = BedRepository(bedDao)

        val recyclerView = binding.gardensRecyclerView
        bedDragHelper.attachToRecyclerView(recyclerView)

        recyclerView.layoutManager = GridLayoutManager(context, 3)

        binding.newBedBtn.setOnClickListener {
            findNavController().navigate(AreaOverviewFragmentDirections.toCreateGrid())
        }

        val observer = { list: List<Bed> ->
            seasonViewModel.currentSeason.observe(viewLifecycleOwner, { currentSeason ->
            adapter = AreaOverviewAdapter(
                    list.filter(CurrentSeasonPredicate(currentSeason)))
                    updatedBeds = list.toMutableList()
            recyclerView.adapter = adapter
            setExplanatoryTextBasedOnItemCount()
        })}

        repository?.findBedsWithLocation(args.location)?.observe(viewLifecycleOwner, observer)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.title = seasonViewModel.currentSeason.value.toString()
    }

    override fun onStop() {
        super.onStop()
        MainScope().launch(Dispatchers.IO) {
            updatedBeds.forEach {
                repository?.updateBed(it)
            }
        }
    }

    private fun setExplanatoryTextBasedOnItemCount(){
        if (adapter.itemCount == 0)
            binding.gardenTextView.visibility = View.VISIBLE
    }

    private inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bedImage: AppCompatButton = view.findViewById(R.id.bed_image_button)
        lateinit var bed: Bed

        init {
            bedImage.setOnClickListener {
                bedViewModel.setBed(bed)
                findNavController().navigate(AreaOverviewFragmentDirections.seeBedAction())
            }
        }
    }

    private inner class AreaOverviewAdapter(private val dataSet: List<Bed>): RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_bed, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bed = dataSet[position]
            holder.bedImage.background = getDrawable(requireContext(), R.drawable.grid_tile)
            holder.bedImage.text = dataSet[position].name
            holder.bedImage.layoutParams = Constraints.LayoutParams(
                    Constraints.LayoutParams.WRAP_CONTENT,
                    Constraints.LayoutParams.WRAP_CONTENT
            )
        }

        override fun getItemCount(): Int {
            return dataSet.size
        }

        fun moveItem(bed: Bed, from: Int, to: Int) {
            if(from > to) { //Move left
                updatedBeds.forEachIndexed { index, b ->
                    if(b.order in to until from){
                        b.order++
                        updatedBeds[index] = b
                    }
                }
            } else { //Move right
                updatedBeds.forEachIndexed { index, b ->
                    if(b.order in (from + 1)..to){
                        b.order--
                        updatedBeds[index] = b
                    }
                }
            }

            bed.order = to
            updatedBeds.forEachIndexed { index, b ->
                if(b.name == bed.name)
                    updatedBeds[index] = bed
            }
        }
    }
}