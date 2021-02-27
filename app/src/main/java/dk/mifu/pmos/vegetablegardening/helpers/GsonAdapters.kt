package dk.mifu.pmos.vegetablegardening.helpers

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.Plant

class GsonAdapters {
    class PlantMapAdapter: TypeAdapter<Map<Coordinate, Plant>>() {
        override fun write(out: JsonWriter?, value: Map<Coordinate, Plant>?) {
            out!!.beginArray()
            value!!.forEach {
                out.beginArray()
                // Coordinate
                out.beginObject()
                out.name("col")?.value(it.key.col)
                out.name("row")?.value(it.key.row)
                out.endObject()

                // Plant
                out.beginObject()
                out.name("name").value(it.value.name)
                out.name("category").value(it.value.category)
                out.name("earliest").value(it.value.earliest)
                out.name("latest").value(it.value.latest)
                out.name("sowing").value(it.value.sowing)
                out.name("cropRotation").value(it.value.cropRotation)
                out.name("quantity").value(it.value.quantity)
                out.name("sowingDepth").value(it.value.sowingDepth)
                out.name("distance").value(it.value.distance)
                out.name("fertilizer").value(it.value.fertilizer)
                out.name("harvest").value(it.value.harvest)
                out.endObject()

                out.endArray()
            }
            out.endArray()
        }

        override fun read(reader: JsonReader?): Map<Coordinate, Plant> {
            val map = HashMap<Coordinate, Plant>()

            reader!!.beginArray()
            while (reader.hasNext()) {
                var col = 0
                var row = 0

                var name = ""
                var category: String? = null
                var earliest: String? = null
                var latest: String? = null
                var sowing: Boolean? = null
                var cropRotation: String? = null
                var quantity: String? = null
                var sowingDepth: String? = null
                var distance: Int? = null
                var fertilizer: String? = null
                var harvest: String? = null

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
                        "category" -> category = reader.nextString()
                        "earliest" -> earliest = reader.nextString()
                        "latest" -> latest = reader.nextString()
                        "sowing" -> sowing = reader.nextBoolean()
                        "cropRotation" -> cropRotation = reader.nextString()
                        "quantity" -> quantity = reader.nextString()
                        "sowingDepth" -> sowingDepth = reader.nextString()
                        "distance" -> distance = reader.nextInt()
                        "fertilizer" -> fertilizer = reader.nextString()
                        "harvest" -> harvest = reader.nextString()
                    }
                }
                reader.endObject()
                reader.endArray()

                map[Coordinate(col, row)] = Plant(name, category, earliest, latest, sowing, cropRotation, quantity, sowingDepth, distance, fertilizer, harvest)
            }
            reader.endArray()

            return map
        }

    }
}