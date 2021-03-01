package dk.mifu.pmos.vegetablegardening.models

import java.io.Serializable

data class Coordinate(val col: Int, val row: Int) : Serializable {
    override fun hashCode(): Int {
        return Pair(col,row).hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Coordinate

        if (col != other.col) return false
        if (row != other.row) return false

        return true
    }
}