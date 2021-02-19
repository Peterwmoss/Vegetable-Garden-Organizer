package dk.mifu.pmos.vegetablegardening.models

import androidx.room.Entity
import androidx.room.TypeConverters
import androidx.databinding.ObservableArrayMap
import androidx.databinding.ObservableMap
import androidx.room.PrimaryKey
import dk.mifu.pmos.vegetablegardening.enums.Location

@Entity(tableName = "gardens")
@TypeConverters(Converters::class)
data class Garden (
        @PrimaryKey var name: String,
        val location: Location,
        val plants: ObservableMap<Coordinate, Plant> = ObservableArrayMap(),
        val tileIds: MutableMap<Coordinate, Int> = HashMap()
)
