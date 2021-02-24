package dk.mifu.pmos.vegetablegardening.models

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import dk.mifu.pmos.vegetablegardening.enums.Location

class Converters {
    private val gson = GsonBuilder().registerTypeAdapter(Coordinate::class.java, GsonAdapters.CoordinateAdapter()).create()

    @TypeConverter
    fun toLocation(value: String) = enumValueOf<Location>(value)

    @TypeConverter
    fun fromLocation(value: Location) = value.name

    @TypeConverter
    fun fromPlantMap(value: Map<Coordinate, Plant>): String = gson.toJson(value)

    @TypeConverter
    fun toPlantMap(value: String): Map<Coordinate, Plant> = gson.fromJson<Map<Coordinate, Plant>>(value, Map::class.java)

    @TypeConverter
    fun fromTileIdMap(value: MutableMap<Coordinate, Int>): String = gson.toJson(value)

    @TypeConverter
    fun toTileIdMap(value: String): MutableMap<Coordinate, Int> = gson.fromJson<MutableMap<Coordinate, Int>>(value, MutableMap::class.java)
}