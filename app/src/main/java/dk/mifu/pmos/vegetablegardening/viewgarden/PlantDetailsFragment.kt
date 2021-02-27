package dk.mifu.pmos.vegetablegardening.viewgarden

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import dk.mifu.pmos.vegetablegardening.databinding.FragmentPlantDetailsBinding

class PlantDetailsFragment: PlantDetailsNavigation() {
    private val args: PlantDetailsFragmentArgs by navArgs()

    private lateinit var binding: FragmentPlantDetailsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPlantDetailsBinding.inflate(inflater, container, false)

        val plant = args.plant

        binding.run {
            plantName.text          = plant.name
            plantCategory.text      = plant.category
            plantCropRotation.text  = plant.cropRotation
            plantDistance.text      = plant.distance.toString()
            plantEarliest.text      = plant.earliest
            plantLatest.text        = plant.latest
            plantFertilizer.text    = plant.fertilizer
            plantSowing.text        = plant.sowing.toString()
            plantSowingDepth.text   = plant.sowingDepth
            plantHarvest.text       = plant.harvest
            plantPlantedDate.text   = plant.plantedDate.toString()
            plantWateredDate.text   = plant.wateredDate.toString()
            plantHarvestedDate.text = plant.harvestedDate.toString()
        }

        return binding.root
    }
}