package dk.mifu.pmos.vegetablegardening.viewmodels

import androidx.databinding.Observable
import androidx.databinding.ObservableArrayMap
import androidx.databinding.ObservableMap
import androidx.lifecycle.ViewModel
import dk.mifu.pmos.vegetablegardening.enums.Location
import dk.mifu.pmos.vegetablegardening.models.Bed
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.Plant

class BedViewModel : ViewModel() {
    var name : String? = null
    var location : Location? = null
    var plants : ObservableMap<Coordinate, Plant>? = null
    var tileIds : MutableMap<Coordinate, Int>? = null

    fun setBed(bed: Bed){
        val map = ObservableArrayMap<Coordinate, Plant>()
        bed.plants.forEach {
            map[it.key] = it.value
        }
        name = bed.name
        location = bed.location
        plants = map
    }
}