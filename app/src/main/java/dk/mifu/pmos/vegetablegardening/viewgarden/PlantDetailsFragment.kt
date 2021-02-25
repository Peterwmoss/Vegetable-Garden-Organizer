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

        binding.run {
            plantName.text          = args.plant.name
            plantCategory.text      = args.plant.category
            plantCropRotation.text  = args.plant.cropRotation
            plantDistance.text      = args.plant.distance.toString()
            plantEarliest.text      = args.plant.earliest
            plantLatest.text        = args.plant.latest
            plantFertilizer.text    = args.plant.fertilizer
            plantSowing.text        = args.plant.sowing.toString()
            plantSowingDepth.text   = args.plant.sowingDepth
            plantHarvest.text       = args.plant.harvest
            plantPlantedDate.text   = args.plant.plantedDate.toString()
            plantWateredDate.text   = args.plant.wateredDate.toString()
        }

        return binding.root
    }
}