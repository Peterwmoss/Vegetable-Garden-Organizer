package dk.mifu.pmos.vegetablegardening.viewmodels

import androidx.databinding.ObservableArrayMap
import androidx.lifecycle.ViewModel
import dk.mifu.pmos.vegetablegardening.enums.Location
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.Plant

class CurrentGardenViewModel : ViewModel() {
    var name : String? = null
    var location : Location? = null
    val plants = ObservableArrayMap<Coordinate, Plant>()
    val tileIds = HashMap<Coordinate, Int>()
}