package dk.mifu.pmos.vegetablegardening.viewgarden

import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import dk.mifu.pmos.vegetablegardening.models.Plant

abstract class BedOverviewNavigation: Fragment(){
    protected fun navigateToPlantInfoDialog(plant: Plant?) {
        if (plant != null) {
            requireView().findNavController().navigate(BedOverviewFragmentDirections.showPlantInfo(plant))
        }
    }
}