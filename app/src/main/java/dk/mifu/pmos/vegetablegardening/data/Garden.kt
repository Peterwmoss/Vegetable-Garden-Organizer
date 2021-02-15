package dk.mifu.pmos.vegetablegardening.data

import dk.mifu.pmos.vegetablegardening.enums.Location
import dk.mifu.pmos.vegetablegardening.models.Plant

data class Garden(
        val location: Location,
        var name: String? = null,
        val plants: MutableMap<Coordinate, Plant> = HashMap(),
        val tileIds: MutableMap<Coordinate, Int> = HashMap()
)
