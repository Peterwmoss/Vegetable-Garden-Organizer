package dk.mifu.pmos.vegetablegardening.viewgarden

import androidx.fragment.app.Fragment
import androidx.navigation.findNavController

abstract class PlantDetailsNavigation: Fragment() {
    protected fun navigateBack() {
        requireView().findNavController().navigateUp()
    }
}