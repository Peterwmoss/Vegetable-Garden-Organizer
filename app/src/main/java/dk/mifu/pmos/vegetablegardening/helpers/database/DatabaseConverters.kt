package dk.mifu.pmos.vegetablegardening.helpers.database

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dk.mifu.pmos.vegetablegardening.enums.BedLocation
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.MyPlant
import java.lang.reflect.Type
import java.util.*

class DatabaseConverters {
    private val plantMapType : Type = object : TypeToken<Map<Coordinate, MyPlant?>>(){}.type
    private val gson = GsonBuilder().registerTypeAdapter(plantMapType, GsonAdapters.PlantMapAdapter()).create()

    @TypeConverter
    fun toLocation(value: String) = enumValueOf<BedLocation>(value)

    @TypeConverter
    fun fromLocation(value: BedLocation) = value.name

    @TypeConverter
    fun fromPlantMap(value: Map<Coordinate, MyPlant>): String = gson.toJson(value, plantMapType)

    @TypeConverter
    fun toPlantMap(value: String): Map<Coordinate, MyPlant> = gson.fromJson(value, plantMapType)

    @TypeConverter
    fun fromDate(value: Date?): Long = value?.time ?: -1

    @TypeConverter
    fun toDate(value: Long) : Date? = if (value == -1L) null else Date(value)
}