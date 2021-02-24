package dk.mifu.pmos.vegetablegardening.creategarden

import androidx.fragment.app.Fragment
import androidx.navigation.findNavController

abstract class SpecifyLocationNavigation: Fragment() {
    protected fun toNextView() {
        requireView().findNavController().navigate(SpecifyLocationFragmentDirections.nextAction())
    }
}