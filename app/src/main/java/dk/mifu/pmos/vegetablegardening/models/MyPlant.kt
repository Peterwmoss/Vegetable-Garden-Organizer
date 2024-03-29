package dk.mifu.pmos.vegetablegardening.models

import java.io.Serializable
import java.util.*

data class MyPlant (
        val name: String,
        var sort: String? = null,
        var wateredDate: Date? = null,
        var harvestedDate: Date? = null,
        var plantedDate: Date? = null,
        var notes: String? = null,
        var germinated: Date? = null
) : Serializable
