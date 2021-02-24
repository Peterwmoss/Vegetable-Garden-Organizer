package dk.mifu.pmos.vegetablegardening.creategarden

import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import dk.mifu.pmos.vegetablegardening.models.Coordinate

abstract class CreateGridNavigation: Fragment() {
    protected fun addPlantToTile(coordinate: Coordinate) {
        requireView().findNavController().navigate(CreateGridFragmentDirections.choosePlantAction(coordinate))
    }
}