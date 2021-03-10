package dk.mifu.pmos.vegetablegardening.helpers.predicates

import dk.mifu.pmos.vegetablegardening.models.Plant
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

import java.util.*

class PlantablePredicateTest {

    private val todayC = Calendar.getInstance()
    private val predicate = PlantablePredicate()

    @Test
    fun invokePlantIsPlantable() {
        val plant = Plant(
                name ="Plante1",
                earliest = changeDate(-1),
                latest = changeDate(1))

        assertThat(predicate.invoke(plant), `is`(true))
    }

    @Test
    fun invokeTooEarly(){
        val plant = Plant(
                name = "Plante2",
                earliest = changeDate(1),
                latest = changeDate(2))

        assertThat(predicate.invoke(plant), `is`(false))
    }

    @Test
    fun invokeTooLate(){
        val plant = Plant(
                name = "Plante3",
                earliest = changeDate(-1),
                latest = changeDate(-2))

        assertThat(predicate.invoke(plant), `is`(false))
    }

    private fun changeDate(change: Int): Date {
        val newDateC = Calendar.getInstance()
        val todayDate = todayC.get(Calendar.DAY_OF_YEAR)
        newDateC.set(Calendar.DAY_OF_YEAR, todayDate+change)
        return newDateC.time
    }
}