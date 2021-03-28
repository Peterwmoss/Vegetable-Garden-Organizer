package dk.mifu.pmos.vegetablegardening.fragments.dialogs

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dk.mifu.pmos.vegetablegardening.databinding.FragmentPlantDetailsDialogBinding
import dk.mifu.pmos.vegetablegardening.enums.MyPlantDate
import dk.mifu.pmos.vegetablegardening.enums.MyPlantDate.*
import dk.mifu.pmos.vegetablegardening.helpers.Formatter
import dk.mifu.pmos.vegetablegardening.helpers.callbacks.UpdateInViewCallback
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

    private lateinit var formatter: Formatter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPlantDetailsDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        binding.plantName.text = args.myPlant.name

        formatter = Formatter(requireContext())

        loadDataFromPlant(args.myPlant)
        setButtonListeners()
        setDatePickerListeners()
        addCallbacks()
    }

    private fun loadDataFromPlant(myPlant: MyPlant) {
        if (myPlant.sort != null)
            binding.sortText.text = myPlant.sort

        if (myPlant.germinated != null)
            binding.germinatedText.text = formatter.formatDate(myPlant.germinated)

        if (myPlant.wateredDate != null)
            binding.wateredText.text = formatter.formatDate(myPlant.wateredDate)

        if (myPlant.plantedDate != null)
            binding.plantedText.text = formatter.formatDate(myPlant.plantedDate)

        if (myPlant.harvestedDate != null)
            binding.harvestedText.text = formatter.formatDate(myPlant.harvestedDate)
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.title = bedViewModel.name
    }

    private fun addCallbacks() {
        bedViewModel.plants?.addOnMapChangedCallback(
                UpdateInViewCallback(args.coordinate, binding, formatter))
    }

    private fun setButtonListeners() {
        binding.detailsButton.setOnClickListener {
            val myPlant = args.myPlant
            val plant = plantViewModel.plants.value?.first { plant -> plant.name == myPlant.name }
            findNavController().navigate(PlantDetailsDialogFragmentDirections.toPlantDetails(plant!!))
        }

        binding.editSortButton.setOnClickListener {
            findNavController().navigate(PlantDetailsDialogFragmentDirections.toEditSort(args.myPlant, args.coordinate))
        }
    }

    private fun setDatePickerListeners() {
        binding.editGerminationButton.setOnClickListener(createDatePickerListener(Germinated))
        binding.plantedButton.setOnClickListener(createDatePickerListener(Planted))
        binding.harvestButton.setOnClickListener(createDatePickerListener(Harvested))
        binding.waterButton.setOnClickListener(createDatePickerListener(Watered))
    }

    private fun createDatePickerListener(date: MyPlantDate): (View) -> Unit {
        val c = Calendar.getInstance()
        val currentYear = c.get(Calendar.YEAR)
        val currentMonth = c.get(Calendar.MONTH)
        val currentDay = c.get(Calendar.DAY_OF_MONTH)

        return {
            val newCal = Calendar.getInstance()
            val listener: (Any, Int, Int, Int) -> Unit = { _, year, month, day ->
                newCal.set(year, month, day)
                when (date) {
                    Germinated -> args.myPlant.germinated = newCal.time
                    Planted -> args.myPlant.plantedDate = newCal.time
                    Watered -> {
                        args.myPlant.wateredDate = newCal.time
                        bedViewModel.plantsToWater.value?.remove(args.coordinate)
                    }
                    Harvested -> args.myPlant.harvestedDate = newCal.time
                }
                bedViewModel.plants?.put(args.coordinate, args.myPlant)
            }
            val dialog = DatePickerDialog(requireContext(), listener, currentYear, currentMonth, currentDay)
            dialog.show()
        }
    }
}