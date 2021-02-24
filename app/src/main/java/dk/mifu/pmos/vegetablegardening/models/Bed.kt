package dk.mifu.pmos.vegetablegardening.models

import androidx.room.Entity
import androidx.room.TypeConverters
import androidx.room.PrimaryKey
import dk.mifu.pmos.vegetablegardening.enums.Location
import dk.mifu.pmos.vegetablegardening.helpers.DatabaseConverters

@Entity(tableName = "gardens")
@TypeConverters(DatabaseConverters::class)
data class Bed (
        @PrimaryKey var name: String,
        val location: Location,
        val plants: Map<Coordinate, Plant> = HashMap(),
)
