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
    class PlantMapAdapter: TypeAdapter<Map<Coordinate, MyPlant?>>() {
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

        override fun write(out: JsonWriter?, value: Map<Coordinate, MyPlant?>?) {
            out!!.beginArray()
            value!!.forEach {
                out.beginArray()
                // Coordinate
                out.beginObject()
                out.name("col").value(it.key.col)
                out.name("row").value(it.key.row)
                out.endObject()

                // Plant
                if (it.value != null) {
                    out.beginObject()
                    out.name("name").value(it.value?.name)
                    out.name("sort").value(it.value?.sort)
                    out.name("wateredDate").value(dateToString(it.value?.wateredDate))
                    out.name("harvestedDate").value(dateToString(it.value?.harvestedDate))
                    out.name("plantedDate").value(dateToString(it.value?.plantedDate))
                    out.name("notes").value(it.value?.notes)
                    out.name("germinated").value(dateToString(it.value?.germinated))
                    out.endObject()
                }

                out.endArray()
            }
            out.endArray()
        }

        override fun read(reader: JsonReader?): Map<Coordinate, MyPlant?> {
            val map = HashMap<Coordinate, MyPlant?>()

            reader!!.beginArray()
            while (reader.hasNext()) {
                var col = 0
                var row = 0

                var name: String? = null
                var sort: String? = null
                var seasons = 0
                var wateredDate: Date? = null
                var harvestedDate: Date? = null
                var plantedDate: Date? = null
                var notes: String? = null
                var germinated: Date? = null

                reader.beginArray()
                reader.beginObject()
                while (reader.hasNext()) {
                    when (reader.nextName()) {
                        "col" -> col = reader.nextInt()
                        "row" -> row = reader.nextInt()
                    }
                }
                reader.endObject()

                if (reader.hasNext()) {
                    reader.beginObject()
                    while (reader.hasNext()) {
                        when (reader.nextName()) {
                            "name" -> name = reader.nextString()
                            "sort" -> sort = reader.nextString()
                            "wateredDate" -> { wateredDate = stringToDate(reader.nextString()) }
                            "harvestedDate" -> { harvestedDate = stringToDate(reader.nextString()) }
                            "plantedDate" -> { plantedDate = stringToDate(reader.nextString()) }
                            "notes" -> notes = reader.nextString()
                            "germinated" -> { germinated = stringToDate(reader.nextString()) }
                        }
                    }
                    reader.endObject()
                }

                reader.endArray()

                map[Coordinate(col, row)] =
                        if (name != null)
                            MyPlant(name, sort, wateredDate, harvestedDate, plantedDate, notes, germinated)
                        else
                            null
            }
            reader.endArray()

            return map
        }

    }
}