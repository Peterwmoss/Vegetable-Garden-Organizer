package dk.mifu.pmos.vegetablegardening.models

import java.io.Serializable
import java.util.*

data class MyPlant (
        val name: String,
        var plantedDate: Date? = null,
        var wateredDate: Date? = null,
        var harvestedDate: Date? = null,
) : Serializable
