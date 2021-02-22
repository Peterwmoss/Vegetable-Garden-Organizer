package dk.mifu.pmos.vegetablegardening.models

import androidx.room.TypeConverter
import com.google.gson.Gson
import dk.mifu.pmos.vegetablegardening.enums.Location

@Suppress("UNCHECKED_CAST")
class Converters {
    @TypeConverter
    fun toLocation(value: String) = enumValueOf<Location>(value)

    @TypeConverter
    fun fromLocation(value: Location) = value.name

    @TypeConverter
    fun fromPlantMap(value: Map<Coordinate, Plant>): String = Gson().toJson(value)

    @TypeConverter
    fun toPlantMap(value: String): Map<Coordinate, Plant> = Gson().fromJson(value, Map::class.java) as Map<Coordinate, Plant>

    @TypeConverter
    fun fromTileIdMap(value: MutableMap<Coordinate, Int>): String = Gson().toJson(value)

    @TypeConverter
    fun toTileIdMap(value: String): MutableMap<Coordinate, Int> = Gson().fromJson(value, MutableMap::class.java) as MutableMap<Coordinate, Int>
}