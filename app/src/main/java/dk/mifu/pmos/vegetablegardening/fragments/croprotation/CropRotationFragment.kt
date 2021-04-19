package dk.mifu.pmos.vegetablegardening.fragments.croprotation

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.database.AppDatabase
import dk.mifu.pmos.vegetablegardening.database.BedRepository
import dk.mifu.pmos.vegetablegardening.databinding.FragmentCropRotationBinding
import dk.mifu.pmos.vegetablegardening.helpers.listviews.CropRotationAdapter
import dk.mifu.pmos.vegetablegardening.models.Bed
import dk.mifu.pmos.vegetablegardening.models.CropRotationHistoryItem
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

        val expandableListView = binding.cropRotationExpandableList

        val plants = plantViewModel.plants.value!!

        MainScope().launch(Dispatchers.IO) {
            val beds = repository.findBedsWithSeason(seasonViewModel.currentSeason.value!!)
            val bedsLookup = beds.map {
                it to createHistoryList(it, plants)
            }.toMap()
            adapter = CropRotationAdapter(requireContext(), beds, bedsLookup)
            expandableListView.setAdapter(adapter)
        }

        return binding.root
    }

    val <T> List<T>.tail: List<T>
        get() = drop(1)

    val <T> List<T>.head: T?
        get() = firstOrNull()

    private suspend fun createHistoryList(initialBed: Bed, plants: List<Plant>): List<CropRotationHistoryItem> {
        return withContext(Dispatchers.IO){
            val list = mutableListOf<CropRotationHistoryItem>()
            val earlierVersionsOfBed = repository.findBedsByName(initialBed.name)

            if (earlierVersionsOfBed.isEmpty())
                return@withContext list

            fun addChildToList(bed: Bed) {
                val interval = findMinCropInterval(bed, plants)
                val seasonsSincePlantedHere = seasonViewModel.currentSeason.value!! - bed.season
                list.add(CropRotationHistoryItem(bed.order, interval - seasonsSincePlantedHere + 1))  // Plantable the year after time is up
            }

            fun addChildItems(currentBed: Bed?, remainingBeds : List<Bed>, currentChildOrder: Int?) {
                when {
                    remainingBeds.isEmpty() -> addChildToList(currentBed!!)
                    remainingBeds.head?.order == currentChildOrder -> {
                        addChildItems(currentBed, remainingBeds.tail, currentChildOrder)
                    }
                    remainingBeds.head?.order != currentChildOrder -> {
                        addChildToList(currentBed!!)
                        val nextBed = remainingBeds.head
                        addChildItems(nextBed, remainingBeds.tail, nextBed?.order)
                    }
                }
            }

            fun addFirstToList(bed: Bed, seasons: Int) {
                list.add(CropRotationHistoryItem(bed.order, seasons))
            }

            fun addFirstItem(currentBed: Bed?, remainingBeds: List<Bed>, seasons: Int) {
                when {
                    remainingBeds.isEmpty() -> addFirstToList(currentBed!!, seasons)
                    remainingBeds.head?.order == initialBed.order -> {
                        addFirstItem(currentBed, remainingBeds.tail, seasons+1)
                    }
                    else -> {
                        addFirstToList(currentBed!!, seasons)
                        val nextBed = remainingBeds.head
                        addChildItems(nextBed, remainingBeds.tail, nextBed?.order)
                    }
                }
            }
            addFirstItem(earlierVersionsOfBed.head, earlierVersionsOfBed.tail, 1)

            return@withContext list
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
                val cropRotationNumber = plant.cropRotation!!.substring(0,1).toIntOrNull() ?: return 0
                if(cropRotationNumber < lowestInterval)
                    lowestInterval = cropRotationNumber
            }
        }

        return lowestInterval
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.crop_rotation)
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.toolbar_crop_rotation)
    }
}