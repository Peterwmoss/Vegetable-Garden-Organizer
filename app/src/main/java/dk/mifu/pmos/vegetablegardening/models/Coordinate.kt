package dk.mifu.pmos.vegetablegardening.models

import java.io.Serializable

data class Coordinate(val x: Int, val y: Int) : Serializable {
    override fun hashCode(): Int {
        return Pair(x,y).hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Coordinate

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }
}