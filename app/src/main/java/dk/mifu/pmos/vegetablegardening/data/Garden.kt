package dk.mifu.pmos.vegetablegardening.data

data class Garden(
        val location: Location,
        var name: String? = null,
        val plants: MutableMap<Coordinate, Plant> = HashMap(),
        val tileIds: MutableMap<Coordinate, Int> = HashMap()
)
