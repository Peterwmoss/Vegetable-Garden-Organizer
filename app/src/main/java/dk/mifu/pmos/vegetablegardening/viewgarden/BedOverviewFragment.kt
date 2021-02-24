package dk.mifu.pmos.vegetablegardening.viewgarden

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dk.mifu.pmos.vegetablegardening.databinding.FragmentBedOverviewBinding
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel

class BedOverviewFragment: Fragment() {
    private lateinit var binding: FragmentBedOverviewBinding
    private val bedViewModel: BedViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentBedOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bedTextView.text = bedViewModel.name

        val gridSize = sizeOfBed()

        binding.gridViewComponent.numColumns = gridSize.first
    }

    private fun sizeOfBed(): Pair<Int,Int> {
        var columns = 0
        var rows = 0

        bedViewModel.plants?.forEach {
            val plantPosCol = it.key.col
            val plantPosRow = it.key.row
            if(plantPosCol > columns) columns = plantPosCol
            if(plantPosRow > rows) rows = plantPosRow
        }

        return Pair(columns, rows)
    }
}