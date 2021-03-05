package dk.mifu.pmos.vegetablegardening.helpers.predicates

import dk.mifu.pmos.vegetablegardening.models.Plant
import java.util.*

class NeedsWaterPredicate(private val date: Date): Predicate<Plant> {
    override fun invoke(plant: Plant): Boolean {
        val wateredDate = plant.wateredDate
        return if (wateredDate != null)
            wateredDate > date
        else {
            true
        }
    }
}