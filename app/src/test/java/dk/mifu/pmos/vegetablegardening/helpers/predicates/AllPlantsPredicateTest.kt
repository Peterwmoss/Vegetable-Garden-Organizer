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
        assertThat(predicate.invoke(Plant("Test")), `is`(true))
        assertThat(predicate.invoke(MyPlant("Test")), `is`(true))
        assertThat(predicate.invoke(true), `is`(true))
        assertThat(predicate.invoke(false), `is`(true))
    }
}