package dk.mifu.pmos.vegetablegardening.fragments.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.DialogFragment
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.databinding.FragmentCreatePlantDialogBinding
import dk.mifu.pmos.vegetablegardening.models.Plant
import java.text.SimpleDateFormat
import java.util.*

class CreatePlantDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentCreatePlantDialogBinding

    private var plant: Plant = Plant("")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCreatePlantDialogBinding.inflate(inflater, container, false)

        setDatePickerListeners()

        val list = listOf("Plantes", "Sås")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item_plant_sowing, list)
        binding.plantSowingText.setAdapter(adapter)

        binding.savePlantButton.setOnClickListener {
            plant.name = binding.plantName.text.toString()
            plant.category = binding.plantCategory.text.toString()
            plant.sowing = binding.plantSowingText.text.toString() == "Sås"
            plant.cropRotation = binding.plantCropRotation.text.toString()
            plant.quantity = binding.plantName.text.toString()
            plant.sowingDepth = "${binding.plantName.text}cm"
            plant.distance = binding.plantName.text.toString().toInt()
            plant.fertilizer = binding.plantName.text.toString()
            plant.harvest = binding.plantName.text.toString()
        }

        binding.cancelButton.setOnClickListener {
            dialog?.cancel()
        }

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.plants)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val params = dialog!!.window!!.attributes

        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.MATCH_PARENT

        dialog!!.window!!.attributes = params

    }
}