package dk.mifu.pmos.vegetablegardening.viewmodels

import androidx.databinding.ObservableMap
import androidx.lifecycle.ViewModel
import dk.mifu.pmos.vegetablegardening.enums.Location
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.Plant

class BedViewModel : ViewModel() {
    var name : String? = null
    var location : Location? = null
    var plants : ObservableMap<Coordinate, Plant>? = null
    var tileIds : MutableMap<Coordinate, Int>? = null
}