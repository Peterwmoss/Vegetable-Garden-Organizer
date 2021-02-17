package dk.mifu.pmos.vegetablegardening.models

import java.util.*

data class Plant(
        val name: String,
        val earliest: Date,
        val latest: Date,
        val sowing: Boolean,
        val cropRotation: String,
        val quantity: String,
        val sowingDepth: String,
        val distance: Int,
        val fertilizer: String,
        val harvest: String
)