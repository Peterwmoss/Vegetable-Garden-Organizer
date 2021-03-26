package dk.mifu.pmos.vegetablegardening.fragments.dialogs

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.database.AppDatabase
import dk.mifu.pmos.vegetablegardening.database.PlantRepository
import dk.mifu.pmos.vegetablegardening.databinding.FragmentCustomPlantDialogBinding
import dk.mifu.pmos.vegetablegardening.models.Plant
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class CustomPlantDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentCustomPlantDialogBinding

    private val args: CustomPlantDialogFragmentArgs by navArgs()

    private var plant: Plant = Plant("")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCustomPlantDialogBinding.inflate(inflater, container, false)

        setDataFromArgs()
        setDatePickerListeners()
        addOptionsToPlantSowing()
        setSaveButtonListener()
        setCancelButtonListener()

        return binding.root
    }

    private fun setDataFromArgs() {
        val argPlant = args.plant
        if (argPlant != null) {
            plant = argPlant
            updateViewFromPlant()
        }
    }

    private fun updateViewFromPlant() {
        binding.plantName.setText(plant.name)
        binding.plantCategory.setText(plant.category)
        if (plant.earliest != null)
            binding.plantEarliestText.text = formatDate(plant.earliest!!)
        if (plant.latest != null)
            binding.plantLatestText.text = formatDate(plant.latest!!)
        if (plant.sowing != null)
            binding.plantSowingText.setText(if (plant.sowing!!) "Sås" else "Plantes")
        binding.plantCropRotation.setText(plant.cropRotation)
        binding.plantQuantity.setText(plant.quantity)
        binding.plantSowingDepth.setText(plant.sowingDepth)
        binding.plantDistance.setText(if (plant.distance == null) "0" else plant.distance!!.toString())
        binding.plantFertilizer.setText(plant.fertilizer)
        binding.plantHarvest.setText(plant.harvest)
    }

    private fun setCancelButtonListener() {
        binding.cancelButton.setOnClickListener {
            dialog?.cancel()
        }
    }

    private fun addOptionsToPlantSowing() {
        val list = listOf("Plantes", "Sås")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item_plant_sowing, list)
        binding.plantSowingText.setAdapter(adapter)
    }

    private fun setSaveButtonListener() {
        binding.savePlantButton.setOnClickListener {
            val name = binding.plantName.text.toString()

            when {
                name.isBlank() -> {
                    binding.plantName.requestFocus()
                    binding.plantName.error = getString(R.string.no_plant_name_given)
                }
                else -> {
                    plant.name = name
                    plant.category = binding.plantCategory.text.toString()
                    plant.sowing = binding.plantSowingText.text.toString() == "Sås"
                    plant.cropRotation = binding.plantCropRotation.text.toString()
                    plant.quantity = binding.plantQuantity.text.toString()
                    plant.sowingDepth = binding.plantSowingDepth.text.toString()
                    plant.distance = binding.plantDistance.text.toString().toIntOrNull()
                    plant.fertilizer = binding.plantFertilizer.text.toString()
                    plant.harvest = binding.plantHarvest.text.toString()

                    if (args.plant == null) {
                        MainScope().launch(Dispatchers.IO) {
                            val exists = async { exists(name) }
                            if (!exists.await()) {
                                saveInDatabase()
                                dismiss()
                            } else
                                Toast.makeText(requireContext(), getString(R.string.guide_plant_already_exists), Toast.LENGTH_LONG).show()
                        }
                    } else {
                        MainScope().launch(Dispatchers.IO) {
                            val dao = AppDatabase.getDatabase(requireContext()).plantDao()
                            val repository = PlantRepository(dao)
                            if (name != args.plant!!.name) {
                                deleteOldFromDatabase()
                                saveInDatabase()
                            } else {
                                repository.updatePlant(plant)
                            }
                        }
                        val navOptions = NavOptions.Builder().setPopUpTo(R.id.lexiconFragment, true).build()
                        findNavController().navigate(R.id.lexiconFragment, null, navOptions)
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val params = dialog!!.window!!.attributes

        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.MATCH_PARENT

        dialog!!.window!!.attributes = params
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.plants)
    }

    private suspend fun deleteOldFromDatabase() {
        withContext(Dispatchers.IO) {
            val dao = AppDatabase.getDatabase(requireContext()).plantDao()
            val repository = PlantRepository(dao)
            repository.deletePlant(plant.name)
        }
    }

    private suspend fun exists(name: String) : Boolean {
        return withContext(Dispatchers.IO) {
            val dao = AppDatabase.getDatabase(requireContext()).plantDao()
            val repository = PlantRepository(dao)
            val plant = repository.findPlant(name)
            return@withContext plant != null
        }
    }

    private suspend fun saveInDatabase() {
        withContext(Dispatchers.IO) {
            val dao = AppDatabase.getDatabase(requireContext()).plantDao()
            val repository = PlantRepository(dao)
            repository.insertPlant(plant)
        }
    }

    private fun setDatePickerListeners() {
        val c = Calendar.getInstance()
        val currentYear = c.get(Calendar.YEAR)
        val currentMonth = c.get(Calendar.MONTH)
        val currentDay = c.get(Calendar.DAY_OF_MONTH)

        binding.plantEarliest.setOnClickListener {
            val newCal = Calendar.getInstance()
            val listener = { _: Any, year: Int, month: Int, day: Int ->
                newCal.set(year, month, day)
                plant.earliest = newCal.time
                binding.plantEarliestText.text = formatDate(newCal.time)
            }
            val dialog = DatePickerDialog(requireContext(), listener, currentYear, currentMonth, currentDay)
            dialog.show()
        }

        binding.plantLatest.setOnClickListener {
            val newCal = Calendar.getInstance()
            val listener = { _: Any, year: Int, month: Int, day: Int ->
                newCal.set(year, month, day)
                plant.latest = newCal.time
                binding.plantLatestText.text = formatDate(newCal.time)
            }
            val dialog = DatePickerDialog(requireContext(), listener, currentYear, currentMonth, currentDay)
            dialog.show()
        }
    }

    private fun formatDate(date: Date): String? {
        val pattern = "dd. MMMM"
        val simpleDateFormat = SimpleDateFormat(pattern, Locale("da", "DK"))
        return simpleDateFormat.format(date)
    }
}