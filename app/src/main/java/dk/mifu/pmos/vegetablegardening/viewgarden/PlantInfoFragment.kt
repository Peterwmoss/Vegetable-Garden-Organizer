package dk.mifu.pmos.vegetablegardening.viewgarden

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import dk.mifu.pmos.vegetablegardening.databinding.FragmentPlantInfoBinding

class PlantInfoFragment : PlantInfoNavigation() {
    private val args : PlantInfoFragmentArgs by navArgs()
    private lateinit var binding: FragmentPlantInfoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPlantInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        setButtonListeners()
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
            navigateToPlantDetails(args.plant)
        }
    }
}