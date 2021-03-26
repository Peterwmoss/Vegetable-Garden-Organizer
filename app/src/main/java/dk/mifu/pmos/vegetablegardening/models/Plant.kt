package dk.mifu.pmos.vegetablegardening.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import dk.mifu.pmos.vegetablegardening.helpers.database.DatabaseConverters
import java.io.Serializable
import java.util.*

@Entity(tableName = "plants")
@TypeConverters(DatabaseConverters::class)
data class Plant (
        @PrimaryKey var name: String,
        var category: String? = null,
        var earliest: Date? = null,
        var latest: Date? = null,
        var sowing: Boolean? = null,
        var cropRotation: String? = null,
        var quantity: String? = null,
        var sowingDepth: String? = null,
        var distance: Int? = null,
        var fertilizer: String? = null,
        var harvest: String? = null,
) : Serializable