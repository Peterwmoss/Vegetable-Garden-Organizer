package dk.mifu.pmos.vegetablegardening.data

import androidx.lifecycle.ViewModel

class PlantViewModel : ViewModel() {
    val plants: List<Plant> by lazy {
        loadPlants()
    }

    private fun loadPlants(): List<Plant> {
        val carrot = Plant("Carrot")
        val tomato = Plant("Tomato")
        return listOf(carrot, tomato)
    }
}