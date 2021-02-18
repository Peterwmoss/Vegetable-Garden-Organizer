package dk.mifu.pmos.vegetablegardening.models

import androidx.databinding.ObservableArrayMap
import androidx.databinding.ObservableMap
import dk.mifu.pmos.vegetablegardening.enums.Location

data class Garden(
        val location: Location,
        var name: String? = null,
        val plants: ObservableMap<Coordinate, Plant> = ObservableArrayMap(),
        val tileIds: MutableMap<Coordinate, Int> = HashMap()
)
