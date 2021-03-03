package dk.mifu.pmos.vegetablegardening.fragments.viewgarden

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.databinding.FragmentEmptyTileBinding
import dk.mifu.pmos.vegetablegardening.viewmodels.PlantViewModel
import java.time.format.DateTimeFormatter
import java.util.*

class EmptyTileFragment : DialogFragment() {
    private lateinit var binding: FragmentEmptyTileBinding

    private val plantViewModel: PlantViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEmptyTileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val today = Date()
        val plantablePlants = plantViewModel.plants.value?.filter {
            it.earliest!! <= today && today <= it.latest
        }

        binding.emptyTileTextView.text = if(plantablePlants.isNullOrEmpty()) resources.getString(R.string.no_plantables_text) else plantablePlants.toString()

    }
}