package dk.mifu.pmos.vegetablegardening.helpers.predicates

import dk.mifu.pmos.vegetablegardening.enums.BedLocation
import dk.mifu.pmos.vegetablegardening.models.Bed
import dk.mifu.pmos.vegetablegardening.models.Plant
import java.util.*

class LocationBedPredicate(private val location: BedLocation?): Predicate<Bed> {
    override fun invoke(bed: Bed): Boolean {
        return location == bed.bedLocation
    }
}