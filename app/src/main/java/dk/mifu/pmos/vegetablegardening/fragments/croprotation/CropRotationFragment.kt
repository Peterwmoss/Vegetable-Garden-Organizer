package dk.mifu.pmos.vegetablegardening.fragments.croprotation

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.database.AppDatabase
import dk.mifu.pmos.vegetablegardening.database.BedDao
import dk.mifu.pmos.vegetablegardening.database.BedRepository
import dk.mifu.pmos.vegetablegardening.databinding.FragmentCropRotationBinding
import dk.mifu.pmos.vegetablegardening.fragments.viewgarden.BedOverviewFragmentDirections
import dk.mifu.pmos.vegetablegardening.models.Bed
import dk.mifu.pmos.vegetablegardening.models.Plant
import dk.mifu.pmos.vegetablegardening.viewmodels.PlantViewModel
import dk.mifu.pmos.vegetablegardening.viewmodels.SeasonViewModel
import dk.mifu.pmos.vegetablegardening.views.Tooltip
import kotlinx.coroutines.*

class CropRotationFragment: Fragment() {
    private lateinit var binding: FragmentCropRotationBinding
    private lateinit var adapter: CropRotationAdapter
    private lateinit var repository: BedRepository

    private val plantViewModel: PlantViewModel by activityViewModels()
    private val seasonViewModel: SeasonViewModel by activityViewModels()

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
                Tooltip.newTooltip(requireContext(), getString(R.string.tooltip_crop_rotation), requireView().rootView.findViewById(R.id.tooltip))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentCropRotationBinding.inflate(inflater, container, false)
        val bedDao = AppDatabase.getDatabase(requireContext()).bedDao()
        repository = BedRepository(bedDao)

        val recyclerView = binding.cropRotationRecyclerView
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(context)

        MainScope().launch(Dispatchers.IO) {
            adapter = CropRotationAdapter(
                    repository.findBedsWithSeason(seasonViewModel.currentSeason.value!!)
            )
            recyclerView.adapter = adapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.crop_rotation)
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.toolbar_crop_rotation)
    }

    private inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bedName: TextView = view.findViewById(R.id.bed_name_crop_rotation)
        val seasons: TextView = view.findViewById(R.id.seasons_croprotation)
    }

    private inner class CropRotationAdapter(private val dataSet: List<Bed>): RecyclerView.Adapter<CropRotationFragment.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CropRotationFragment.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_crop_rotation, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: CropRotationFragment.ViewHolder, position: Int) {
            val bed = dataSet[position]
            holder.bedName.text = bed.name
            val plants = plantViewModel.plants.value

            MainScope().launch {
                holder.seasons.text = findNumberOfSeasonsWithBedPlacedHere(bed, plants!!)
            }
        }

        override fun getItemCount(): Int {
            return dataSet.size
        }

        private suspend fun findNumberOfSeasonsWithBedPlacedHere(bed: Bed, plants: List<Plant>): String {
            return withContext(Dispatchers.IO){
                val earlierVersionsOfBed = repository.findBedsByName(bed.name)
                var yearsInSameSpot = 0
                earlierVersionsOfBed.forEach {
                    if(it.order == bed.order) yearsInSameSpot++
                    else return@forEach
                }

                val minCropInterval = findMinCropInterval(bed, plants)
                return@withContext if(minCropInterval == 0){
                    getString(R.string.seasons_bed_without_plants)
                } else {
                    getString(R.string.seasons_bed_with_plants, yearsInSameSpot, minCropInterval)
                }
            }
        }

        private fun findMinCropInterval(bed: Bed, plants: List<Plant>): Int {
            var lowestInterval = Int.MAX_VALUE
            val bedPlants = bed.plants.values

            if(bedPlants.isEmpty()) return 0

            bed.plants.values.forEach{
                val plant = plants.find {
                    plant -> plant.name == it.name
                }

                if(plant != null){
                    val cropRotationNumber = plant.cropRotation!!.substring(0,1).toInt()
                    if(cropRotationNumber < lowestInterval)
                        lowestInterval = cropRotationNumber
                }
            }

            return lowestInterval
        }
    }
}