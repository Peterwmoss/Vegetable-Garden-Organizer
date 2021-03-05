package dk.mifu.pmos.vegetablegardening.helpers.database

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dk.mifu.pmos.vegetablegardening.enums.Location
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.Plant
import java.lang.reflect.Type

class DatabaseConverters {
    private val plantMapType : Type = object : TypeToken<Map<Coordinate, Plant>>(){}.type
    private val gson = GsonBuilder().registerTypeAdapter(plantMapType, GsonAdapters.PlantMapAdapter()).create()

    @TypeConverter
    fun toLocation(value: String) = enumValueOf<Location>(value)

    @TypeConverter
    fun fromLocation(value: Location) = value.name

    @TypeConverter
    fun fromPlantMap(value: Map<Coordinate, Plant>): String = gson.toJson(value, plantMapType)

    @TypeConverter
    fun toPlantMap(value: String): Map<Coordinate, Plant> = gson.fromJson(value, plantMapType)
}