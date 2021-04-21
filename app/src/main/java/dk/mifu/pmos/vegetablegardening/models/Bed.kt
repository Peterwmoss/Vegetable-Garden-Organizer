package dk.mifu.pmos.vegetablegardening.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.TypeConverters
import dk.mifu.pmos.vegetablegardening.enums.BedLocation
import dk.mifu.pmos.vegetablegardening.helpers.database.DatabaseConverters

@Entity(tableName = "beds", primaryKeys = ["name", "season"], foreignKeys = [ForeignKey(entity = Season::class, parentColumns = ["season"], childColumns = ["season"])])
@TypeConverters(DatabaseConverters::class)
data class Bed (
        var name: String,
        var season: Int,
        var bedLocation: BedLocation,
        var plants: Map<Coordinate, MyPlant> = HashMap(),
        var columns: Int,
        var rows: Int,
        var order: Int
)
