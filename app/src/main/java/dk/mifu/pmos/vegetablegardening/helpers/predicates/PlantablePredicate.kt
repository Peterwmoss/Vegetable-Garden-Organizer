package dk.mifu.pmos.vegetablegardening.helpers.predicates

import dk.mifu.pmos.vegetablegardening.helpers.predicates.Predicate
import dk.mifu.pmos.vegetablegardening.models.Plant
import java.util.*

class PlantablePredicate(): Predicate<Plant> {
    override fun invoke(plant: Plant): Boolean {
        val today = Date()
        return plant.earliest!! <= today && today <= plant.latest
    }
}