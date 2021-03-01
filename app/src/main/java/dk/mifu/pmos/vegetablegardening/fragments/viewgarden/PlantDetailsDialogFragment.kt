package dk.mifu.pmos.vegetablegardening.fragments.viewgarden

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dk.mifu.pmos.vegetablegardening.databinding.FragmentPlantDetailsDialogBinding
import dk.mifu.pmos.vegetablegardening.models.Plant
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel
import java.util.*

class PlantDetailsDialogFragment : DialogFragment() {
    private val args : PlantDetailsDialogFragmentArgs by navArgs()
    private lateinit var binding: FragmentPlantDetailsDialogBinding

    private val bedViewModel: BedViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPlantDetailsDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        setButtonListeners()
    }

    private fun setButtonListeners() {
        binding.waterButton.setOnClickListener {
            args.plant.wateredDate = Date()
            bedViewModel.plants?.put(args.coordinate, args.plant)
        }

        binding.harvestButton.setOnClickListener {
            args.plant.harvestedDate = Date()
            bedViewModel.plants?.put(args.coordinate, args.plant)
        }

        binding.detailsButton.setOnClickListener {
            navigateToPlantDetails(args.plant)
        }
    }

    private fun navigateToPlantDetails(plant: Plant) {
        findNavController().navigate(PlantDetailsDialogFragmentDirections.toPlantDetails(plant))
    }
}