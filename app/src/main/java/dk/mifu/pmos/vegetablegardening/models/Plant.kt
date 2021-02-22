package dk.mifu.pmos.vegetablegardening.models

import java.util.*

data class Plant(
        val name: String,
        val category: String? = null,
        val earliest: String? = null,
        val latest: String? = null,
        val sowing: Boolean? = null,
        val cropRotation: String? = null,
        val quantity: String? = null,
        val sowingDepth: String? = null,
        val distance: Int? = null,
        val fertilizer: String? = null,
        val harvest: String? = null,
        val plantedDate: Date? = null,
        val wateredDate: Date? = null,
)