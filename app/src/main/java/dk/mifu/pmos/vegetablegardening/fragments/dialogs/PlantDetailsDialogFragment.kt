package dk.mifu.pmos.vegetablegardening.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.databinding.FragmentPlantDetailsDialogBinding
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.MyPlant
import dk.mifu.pmos.vegetablegardening.models.Plant
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel
import dk.mifu.pmos.vegetablegardening.viewmodels.PlantViewModel
import java.util.*

class PlantDetailsDialogFragment : DialogFragment() {
    private val args : PlantDetailsDialogFragmentArgs by navArgs()
    private lateinit var binding: FragmentPlantDetailsDialogBinding

    private val bedViewModel: BedViewModel by activityViewModels()
    private val plantViewModel: PlantViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPlantDetailsDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        binding.plantDetailsDialogPlant.text = args.plant.name
        if(args.plant.sort.isNotBlank()) {
            binding.sort.visibility = View.VISIBLE
            binding.sort.text = args.plant.sort
        }
        setButtonListeners()
    }

    private fun setButtonListeners() {
        binding.waterButton.setOnClickListener {
            args.plant.wateredDate = Date()
            bedViewModel.plants?.put(args.coordinate, args.plant)
            bedViewModel.plantsToWater.value?.remove(args.coordinate)
            Toast.makeText(context, getString(R.string.register_water_text), Toast.LENGTH_SHORT).show()
        }

        binding.harvestButton.setOnClickListener {
            args.plant.harvestedDate = Date()
            bedViewModel.plants?.put(args.coordinate, args.plant)
            Toast.makeText(context, getString(R.string.register_harvest_text), Toast.LENGTH_SHORT).show()
        }

        binding.detailsButton.setOnClickListener {
            val myPlant = args.plant
            val plant = plantViewModel.plants.value?.first { plant -> plant.name == myPlant.name }
            navigateToPlantDetails(plant!!, myPlant, args.coordinate)
        }
    }

    private fun navigateToPlantDetails(plant: Plant, myPlant: MyPlant, coordinate: Coordinate) {
        findNavController().navigate(PlantDetailsDialogFragmentDirections.toPlantDetails(plant, myPlant, coordinate))
    }
}