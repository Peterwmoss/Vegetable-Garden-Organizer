package dk.mifu.pmos.vegetablegardening.models

import java.io.Serializable
import java.util.*

data class MyPlant (
        val name: String,
        var seasons: Int = 1,
        var wateredDate: Date? = null,
        var harvestedDate: Date? = null,
) : Serializable
