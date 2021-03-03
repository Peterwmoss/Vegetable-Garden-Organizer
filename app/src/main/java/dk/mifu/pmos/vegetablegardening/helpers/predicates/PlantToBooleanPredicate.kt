package dk.mifu.pmos.vegetablegardening.helpers.predicates

import dk.mifu.pmos.vegetablegardening.helpers.predicates.Predicate
import dk.mifu.pmos.vegetablegardening.models.Plant
import java.util.*

class PlantToBooleanPredicate(): Predicate() {
    override fun invoke(plant: Any): Boolean {
        plant as Plant
        val today = Date()
        return plant.earliest!! <= today && today <= plant.latest
    }
}