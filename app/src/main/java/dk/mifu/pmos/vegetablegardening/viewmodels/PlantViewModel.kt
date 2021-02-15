package dk.mifu.pmos.vegetablegardening.viewmodels

import androidx.lifecycle.ViewModel
import dk.mifu.pmos.vegetablegardening.models.Plant

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