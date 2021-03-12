package dk.mifu.pmos.vegetablegardening.helpers.predicates

import dk.mifu.pmos.vegetablegardening.models.MyPlant
import dk.mifu.pmos.vegetablegardening.models.Plant
import org.hamcrest.core.Is.`is`
import org.junit.Test

import org.junit.Assert.*

class AllPlantsPredicateTest {

    @Test
    fun invokeReturnsTrue() {
        val predicate = AllPlantsPredicate()
        assertTrue(predicate.invoke(Plant("Test")))
        assertTrue(predicate.invoke(MyPlant("Test")))
        assertTrue(predicate.invoke(true))
        assertTrue(predicate.invoke(false))
    }
}