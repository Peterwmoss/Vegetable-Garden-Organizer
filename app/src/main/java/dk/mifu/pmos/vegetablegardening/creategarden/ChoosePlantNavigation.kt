package dk.mifu.pmos.vegetablegardening.creategarden

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import dk.mifu.pmos.vegetablegardening.models.Plant
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel

abstract class ChoosePlantNavigation : DialogFragment() {
    protected val bedViewModel: BedViewModel by activityViewModels()

    fun navigateBack(args: ChoosePlantFragmentArgs, plant: Plant) {
        bedViewModel.plants?.set(args.coordinate, plant)
        dialog?.dismiss()
    }
}