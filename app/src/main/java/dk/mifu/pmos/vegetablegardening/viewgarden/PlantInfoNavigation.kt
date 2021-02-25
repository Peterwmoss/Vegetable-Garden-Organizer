package dk.mifu.pmos.vegetablegardening.viewgarden

import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import dk.mifu.pmos.vegetablegardening.models.Plant

abstract class PlantInfoNavigation: DialogFragment() {
    protected fun navigateToPlantDetails(plant: Plant) {
        findNavController().navigate(PlantInfoFragmentDirections.toPlantDetails(plant))
    }
}