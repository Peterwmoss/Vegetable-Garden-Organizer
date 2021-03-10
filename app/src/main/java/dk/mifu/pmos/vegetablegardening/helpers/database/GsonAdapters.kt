package dk.mifu.pmos.vegetablegardening.helpers.database

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.MyPlant
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class GsonAdapters {
    class PlantMapAdapter: TypeAdapter<Map<Coordinate, MyPlant>>() {
        private val format = SimpleDateFormat("dd-MM-yyyy", Locale("da","DK"))

        private fun dateToString(date: Date?): String {
            return if (date != null)
                format.format(date)
            else
                ""
        }

        private fun stringToDate(date: String): Date? {
            return if (date.isNotBlank())
                format.parse(date)
            else
                null
        }

        override fun write(out: JsonWriter?, value: Map<Coordinate, MyPlant>?) {
            out!!.beginArray()
            value!!.forEach {
                out.beginArray()
                // Coordinate
                out.beginObject()
                out.name("col").value(it.key.col)
                out.name("row").value(it.key.row)
                out.endObject()

                // Plant
                out.beginObject()
                out.name("name").value(it.value.name)
                out.name("plantedDate").value(dateToString(it.value.plantedDate))
                out.name("wateredDate").value(dateToString(it.value.wateredDate))
                out.name("harvestedDate").value(dateToString(it.value.harvestedDate))
                out.endObject()

                out.endArray()
            }
            out.endArray()
        }

        override fun read(reader: JsonReader?): Map<Coordinate, MyPlant> {
            val map = HashMap<Coordinate, MyPlant>()

            reader!!.beginArray()
            while (reader.hasNext()) {
                var col = 0
                var row = 0

                var name = ""
                var plantedDate: Date? = null
                var wateredDate: Date? = null
                var harvestedDate: Date? = null

                reader.beginArray()
                reader.beginObject()
                while (reader.hasNext()) {
                    when (reader.nextName()) {
                        "col" -> col = reader.nextInt()
                        "row" -> row = reader.nextInt()
                    }
                }
                reader.endObject()
                reader.beginObject()
                while (reader.hasNext()) {
                    when (reader.nextName()) {
                        "name" -> name = reader.nextString()
                        "plantedDate" -> { plantedDate = stringToDate(reader.nextString()) }
                        "wateredDate" -> { wateredDate = stringToDate(reader.nextString()) }
                        "harvestedDate" -> { harvestedDate = stringToDate(reader.nextString()) }
                    }
                }
                reader.endObject()
                reader.endArray()

                map[Coordinate(col, row)] = MyPlant(name, plantedDate, wateredDate, harvestedDate)
            }
            reader.endArray()

            return map
        }

    }
}