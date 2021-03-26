package dk.mifu.pmos.vegetablegardening.fragments.croprotation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.database.AppDatabase
import dk.mifu.pmos.vegetablegardening.database.BedDao
import dk.mifu.pmos.vegetablegardening.database.BedRepository
import dk.mifu.pmos.vegetablegardening.databinding.FragmentCropRotationBinding
import dk.mifu.pmos.vegetablegardening.models.Bed
import dk.mifu.pmos.vegetablegardening.models.Plant
import dk.mifu.pmos.vegetablegardening.viewmodels.PlantViewModel
import dk.mifu.pmos.vegetablegardening.viewmodels.SeasonViewModel
import kotlinx.coroutines.*

class CropRotationFragment: Fragment() {
    private lateinit var binding: FragmentCropRotationBinding
    private lateinit var adapter: CropRotationAdapter
    private lateinit var repository: BedRepository
    private lateinit var bedDao: BedDao
    private val plantViewModel: PlantViewModel by activityViewModels()
    private val seasonViewModel: SeasonViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentCropRotationBinding.inflate(inflater, container, false)
        bedDao = AppDatabase.getDatabase(requireContext()).bedDao()
        repository = BedRepository(bedDao)

        val recyclerView = binding.cropRotationRecyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 1)
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

    private inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var bedName: TextView = view.findViewById(R.id.bed_name_crop_rotation)
        var seasons: TextView = view.findViewById(R.id.seasons_croprotation)
    }

    private inner class CropRotationAdapter(private val dataSet: List<Bed>): RecyclerView.Adapter<CropRotationFragment.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CropRotationFragment.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_crop_rotation, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: CropRotationFragment.ViewHolder, position: Int) {
            val bed = dataSet[position]
            holder.bedName.text = bed.name

            MainScope().launch {
                val plants = plantViewModel.plants.value
                holder.seasons.text = withContext(Dispatchers.Default) { findNumberOfSeasonsWithBedHere(bed, plants) }
            }
        }

        override fun getItemCount(): Int {
            return dataSet.size
        }

        private fun findNumberOfSeasonsWithBedHere(bed: Bed,  plants: List<Plant>?): String {
                val earlierVersionsOfBed = repository.findBedsByName(bed.name)
                var yearsInSameSpot = 0
                earlierVersionsOfBed.forEach {
                    if(it.order == bed.order) yearsInSameSpot++
                    else return@forEach
                }

            return if(plants.isNullOrEmpty()){
                getString(R.string.seasons_bed_without_plants)
            } else {
                getString(R.string.seasons_bed_with_plants, yearsInSameSpot, findMinCropInterval(bed, plants))
            }
        }

        private fun findMinCropInterval(bed: Bed, plants: List<Plant>): Int {
            var lowestInterval = Int.MAX_VALUE
            bed.plants.forEach{
                val plant = plants.find {
                    plant -> plant.name == it.value.name
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