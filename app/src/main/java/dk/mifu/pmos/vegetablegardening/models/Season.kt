package dk.mifu.pmos.vegetablegardening.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "seasons")
data class Season (@PrimaryKey var season: Int)