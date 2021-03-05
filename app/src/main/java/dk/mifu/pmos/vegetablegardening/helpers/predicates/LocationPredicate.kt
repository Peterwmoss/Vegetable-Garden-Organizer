package dk.mifu.pmos.vegetablegardening.helpers.predicates

import dk.mifu.pmos.vegetablegardening.enums.Location
import dk.mifu.pmos.vegetablegardening.enums.Location.Greenhouse
import dk.mifu.pmos.vegetablegardening.models.Plant
import java.util.*

class LocationPredicate(private val location: Location?): Predicate<Plant> {
    override fun invoke(plant: Plant): Boolean {
        val locale = Locale("da", "DK")
        val category = plant.category?.toLowerCase(locale)
        val greenHouseString = "drivhus"
        return when (location) {
            Greenhouse -> category == greenHouseString
            else -> category != greenHouseString
        }
    }
}