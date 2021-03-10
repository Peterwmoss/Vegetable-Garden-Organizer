package dk.mifu.pmos.vegetablegardening.helpers.predicates

import dk.mifu.pmos.vegetablegardening.models.MyPlant
import org.hamcrest.core.Is.`is`
import org.junit.Test

import org.junit.Assert.*
import java.util.*

class NeedsWaterPredicateTest {

    private val todayC = Calendar.getInstance()

    @Test
    fun invokeJustRainedWateredDateNull() {
        val predicate = NeedsWaterPredicate(Date())
        assertThat(predicate.invoke(MyPlant(name = "Plante1")), `is` (false))
    }

    @Test
    fun invokeJustRainedWateredDateBefore() {
        val predicate = NeedsWaterPredicate(Date())
        assertThat(predicate.invoke(MyPlant(name = "Plante1", wateredDate = changeDate(-1))), `is` (false))
    }

    @Test
    fun invokeJustRainedWateredDateAfter() {
        val predicate = NeedsWaterPredicate(changeDate(-1))
        assertThat(predicate.invoke(MyPlant(name = "Plante1", wateredDate = Date())), `is` (false))
    }

    @Test
    fun invokeRainedLongAgoWateredDateNull() {
        val predicate = NeedsWaterPredicate(changeDate(-30)) //Month since rained
        assertThat(predicate.invoke(MyPlant(name = "Plante1")), `is` (true))
    }

    @Test
    fun invokeRainedLongAgoWateredRecently() {
        val predicate = NeedsWaterPredicate(changeDate(-30)) //Month since rained
        assertThat(predicate.invoke(MyPlant(name = "Plante1", wateredDate = Date())), `is` (false))
    }

    @Test
    fun invokeRainedLongAgoWateredLongAgo() {
        val predicate = NeedsWaterPredicate(changeDate(-30)) //Month since rained
        assertThat(predicate.invoke(MyPlant(name = "Plante1", wateredDate = changeDate(-30))), `is` (true))
    }

    private fun changeDate(change: Int): Date {
        val newDateC = Calendar.getInstance()
        val todayDate = todayC.get(Calendar.DAY_OF_YEAR)
        newDateC.set(Calendar.DAY_OF_YEAR, todayDate+change)
        return newDateC.time
    }

}