package dk.mifu.pmos.vegetablegardening.data

data class Garden(
        val location: Location,
        var name: String? = null,
        val plants: MutableMap<Pair<Int,Int>, Plant> = HashMap(),
        val tileIds: Map<Pair<Int,Int>, Int> = HashMap()
)
