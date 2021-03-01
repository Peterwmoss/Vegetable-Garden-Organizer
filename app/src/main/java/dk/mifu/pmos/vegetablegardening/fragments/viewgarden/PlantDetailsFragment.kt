package dk.mifu.pmos.vegetablegardening.fragments.viewgarden

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.databinding.FragmentPlantDetailsBinding
import dk.mifu.pmos.vegetablegardening.viewmodels.PlantViewModel

class PlantDetailsFragment: Fragment() {
    private val args: PlantDetailsFragmentArgs by navArgs()
    private val plantViewModel: PlantViewModel by activityViewModels()

    private lateinit var binding: FragmentPlantDetailsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPlantDetailsBinding.inflate(inflater, container, false)

        val plant = args.plant
        val titles = plantViewModel.categoryTitles.value

        binding.run {
            plantName.text          = plant.name
            plantCategory.text      = addString(titles?.get(1), plant.category)
            plantEarliest.text      = addString(titles?.get(2), plant.earliest)
            plantLatest.text        = addString(titles?.get(3), plant.latest)
            plantSowing.text        = addString(titles?.get(4), plant.sowing.toString())
            plantCropRotation.text  = addString(titles?.get(5), plant.cropRotation)
            plantQuantity.text      = addString(titles?.get(6), plant.quantity)
            plantDistance.text      = addString(titles?.get(7), plant.distance.toString())
            plantFertilizer.text    = addString(titles?.get(8), plant.fertilizer)
            plantSowingDepth.text   = addString(titles?.get(9), plant.sowingDepth)
            plantHarvest.text       = addString(titles?.get(10), plant.harvest)
            plantPlantedDate.text   = plant.plantedDate.toString()
            plantWateredDate.text   = plant.wateredDate.toString()
            plantHarvestedDate.text = plant.harvestedDate.toString()
        }

        return binding.root
    }

    private fun addString(category: String?, field: String?): String {
        return String.format(resources.getString(R.string.category_format), category, field)
    }
}