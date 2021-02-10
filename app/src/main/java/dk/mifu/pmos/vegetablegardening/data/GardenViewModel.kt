package dk.mifu.pmos.vegetablegardening.data

import androidx.lifecycle.ViewModel

class GardenViewModel : ViewModel() {
    val gardens: ArrayList<Garden> by lazy {
        loadGardens()
    }

    private fun loadGardens(): ArrayList<Garden> {
        return ArrayList()
    }
}