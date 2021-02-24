package dk.mifu.pmos.vegetablegardening.models

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class GsonAdapters {
    class CoordinateAdapter: TypeAdapter<Coordinate>() {
        override fun write(out: JsonWriter?, value: Coordinate?) {
            out?.beginObject()
            out?.name("x")?.value(value?.col)
            out?.name("y")?.value(value?.row)
            out?.endObject()
        }

        override fun read(reader: JsonReader?): Coordinate {
            var x = 0
            var y = 0

            reader!!.beginObject()
            while (reader.hasNext()) {
                when (reader.nextName()) {
                    "x" -> x = reader.nextInt()
                    "y" -> y = reader.nextInt()
                }
            }
            reader.endObject()

            return Coordinate(x,y)
        }

    }
}