package dk.mifu.pmos.vegetablegardening.fragments.viewgarden

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import dk.mifu.pmos.vegetablegardening.databinding.FragmentEmptyTileDialogBinding
import dk.mifu.pmos.vegetablegardening.viewmodels.PlantViewModel
import java.time.format.DateTimeFormatter
import java.util.*

class EmptyTileDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentEmptyTileDialogBinding

    private val plantViewModel: PlantViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEmptyTileDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val today = Date()
        val plantablePlants = plantViewModel.plants.value
    }
}