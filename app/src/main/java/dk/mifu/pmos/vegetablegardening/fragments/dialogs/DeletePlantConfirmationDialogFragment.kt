package dk.mifu.pmos.vegetablegardening.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.database.AppDatabase
import dk.mifu.pmos.vegetablegardening.database.PlantRepository
import dk.mifu.pmos.vegetablegardening.databinding.FragmentDeletePlantConfirmationDialogBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class DeletePlantConfirmationDialogFragment : DialogFragment() {
    private lateinit var binding : FragmentDeletePlantConfirmationDialogBinding

    private val args : DeletePlantConfirmationDialogFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDeletePlantConfirmationDialogBinding.inflate(inflater, container, false)

        binding.cancelButton.setOnClickListener {
            dialog?.cancel()
        }

        binding.deletePlantButton.setOnClickListener {
            deletePlant()
            Toast.makeText(requireContext(), getString(R.string.toast_plant_deleted), Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.lexiconFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val params = dialog!!.window!!.attributes

        params.width = WindowManager.LayoutParams.WRAP_CONTENT

        dialog!!.window!!.attributes = params
    }

    private fun deletePlant() {
        MainScope().launch(Dispatchers.IO) {
            val dao = AppDatabase.getDatabase(requireContext()).plantDao()
            val repository = PlantRepository(dao)
            repository.deletePlant(args.plant.name)
        }
    }

}