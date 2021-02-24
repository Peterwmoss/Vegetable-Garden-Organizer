package dk.mifu.pmos.vegetablegardening.creategarden

import androidx.fragment.app.DialogFragment

abstract class ChoosePlantNavigation : DialogFragment() {
    protected fun navigateBack() {
        dialog?.dismiss()
    }
}