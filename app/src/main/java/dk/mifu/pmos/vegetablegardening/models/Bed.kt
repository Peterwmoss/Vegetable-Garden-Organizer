package dk.mifu.pmos.vegetablegardening.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.TypeConverters
import androidx.room.PrimaryKey
import dk.mifu.pmos.vegetablegardening.enums.BedLocation
import dk.mifu.pmos.vegetablegardening.helpers.database.DatabaseConverters

@Entity(tableName = "beds", primaryKeys = ["name", "season"], foreignKeys = [ForeignKey(entity = Season::class, parentColumns = ["season"], childColumns = ["season"])])
@TypeConverters(DatabaseConverters::class)
data class Bed (
        var name: String,
        var season: Int,
        val bedLocation: BedLocation,
        val plants: Map<Coordinate, MyPlant> = HashMap(),
        val columns: Int,
        val rows: Int
)
