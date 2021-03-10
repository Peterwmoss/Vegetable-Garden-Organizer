package dk.mifu.pmos.vegetablegardening.helpers.predicates

import dk.mifu.pmos.vegetablegardening.models.MyPlant
import dk.mifu.pmos.vegetablegardening.models.Plant
import org.junit.Test

import org.junit.Assert.*

class AllPlantsPredicateTest {

    @Test
    fun invokeReturnsTrue() {
        val predicate = AllPlantsPredicate()
        assert(predicate.invoke(Plant("Test")))
        assert(predicate.invoke(MyPlant("Test")))
        assert(predicate.invoke(true))
        assert(predicate.invoke(false))
    }
}