package dk.mifu.pmos.vegetablegardening.helpers.predicates

import dk.mifu.pmos.vegetablegardening.enums.BedLocation
import dk.mifu.pmos.vegetablegardening.enums.BedLocation.Greenhouse
import dk.mifu.pmos.vegetablegardening.models.Plant
import java.util.*

class LocationPlantPredicate(private val bedLocation: BedLocation?): Predicate<Plant> {
    override fun invoke(plant: Plant): Boolean {
        val locale = Locale("da", "DK")
        val category = plant.category?.toLowerCase(locale)
        val greenHouseString = "drivhus"
        return when (bedLocation) {
            Greenhouse -> category == greenHouseString
            else -> category != greenHouseString
        }
    }
}