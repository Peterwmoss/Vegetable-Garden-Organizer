package dk.mifu.pmos.vegetablegardening.views

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import dk.mifu.pmos.vegetablegardening.databinding.DialogPlantInfoBinding
import dk.mifu.pmos.vegetablegardening.models.Plant

class PlantInfoDialog(private val plant: Plant) : DialogFragment() {
    private lateinit var binding: DialogPlantInfoBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogPlantInfoBinding.inflate(layoutInflater)
        
        setButtonListeners()

        return AlertDialog.Builder(activity)
                .setTitle(plant.name)
                .setView(binding.root)
                .create()
    }

    private fun setButtonListeners() {
        binding.waterButton.setOnClickListener {
            Toast.makeText(activity, "Vand", Toast.LENGTH_SHORT).show()
        }

        binding.harvestButton.setOnClickListener {
            Toast.makeText(activity, "Høst", Toast.LENGTH_SHORT).show()
        }

        binding.detailsButton.setOnClickListener {
            Toast.makeText(activity, "Detaljer", Toast.LENGTH_SHORT).show()
            //parentFragment?.requireView()?.findNavController()?.navigate(R.id.)
        }
    }

    companion object {
        const val TAG = "PlantInfoDialog"
    }
}