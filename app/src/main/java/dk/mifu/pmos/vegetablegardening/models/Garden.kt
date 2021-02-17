package dk.mifu.pmos.vegetablegardening.models

import androidx.room.Entity
import androidx.room.TypeConverters
import dk.mifu.pmos.vegetablegardening.enums.Location

@Entity(tableName = "gardens", primaryKeys = ["location", "name"])
@TypeConverters(Converters::class)
data class Garden (
        val location: Location,
        var name: String,
        val plants: MutableMap<Coordinate, Plant> = HashMap(),
        val tileIds: MutableMap<Coordinate, Int> = HashMap()
)
