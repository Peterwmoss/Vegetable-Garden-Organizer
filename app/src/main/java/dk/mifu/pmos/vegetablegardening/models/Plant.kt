package dk.mifu.pmos.vegetablegardening.models

import java.io.Serializable
import java.util.*

data class Plant (
        val name: String,
        val category: String? = null,
        val earliest: Date? = null,
        val latest: Date? = null,
        val sowing: Boolean? = null,
        val cropRotation: String? = null,
        val quantity: String? = null,
        val sowingDepth: String? = null,
        val distance: Int? = null,
        val fertilizer: String? = null,
        val harvest: String? = null,
        var plantedDate: Date? = null,
        var wateredDate: Date? = null,
        var harvestedDate: Date? = null,
) : Serializable