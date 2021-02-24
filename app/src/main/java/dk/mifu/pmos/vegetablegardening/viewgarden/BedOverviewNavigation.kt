package dk.mifu.pmos.vegetablegardening.viewgarden

import androidx.fragment.app.Fragment
import androidx.navigation.findNavController

abstract class BedOverviewNavigation: Fragment(){
    protected fun navigateToPlantInfoDialog() {
        requireView().findNavController().navigate(BedOverviewFragmentDirections.showPlantInfo())
    }
}