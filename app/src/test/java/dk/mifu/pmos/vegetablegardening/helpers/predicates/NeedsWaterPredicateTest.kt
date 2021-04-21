package dk.mifu.pmos.vegetablegardening.helpers.predicates

import dk.mifu.pmos.vegetablegardening.models.MyPlant
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.*

class NeedsWaterPredicateTest {

    private val todayC = Calendar.getInstance()

    @Test
    fun invokeJustRainedWateredDateNull() {
        val predicate = NeedsWaterPredicate(Date())
        assertFalse(predicate.invoke(MyPlant(name = "Plante1")))
    }

    @Test
    fun invokeJustRainedWateredDateBefore() {
        val predicate = NeedsWaterPredicate(Date())
        assertFalse(predicate.invoke(MyPlant(name = "Plante1", wateredDate = changeDate(-1))))
    }

    @Test
    fun invokeJustRainedWateredDateAfter() {
        val predicate = NeedsWaterPredicate(changeDate(-1))
        assertFalse(predicate.invoke(MyPlant(name = "Plante1", wateredDate = Date())))
    }

    @Test
    fun invokeRainedLongAgoWateredDateNull() {
        val predicate = NeedsWaterPredicate(changeDate(-30)) //Month since rained
        assertTrue(predicate.invoke(MyPlant(name = "Plante1")))
    }

    @Test
    fun invokeRainedLongAgoWateredRecently() {
        val predicate = NeedsWaterPredicate(changeDate(-30)) //Month since rained
        assertFalse(predicate.invoke(MyPlant(name = "Plante1", wateredDate = Date())))
    }

    @Test
    fun invokeRainedLongAgoWateredLongAgo() {
        val predicate = NeedsWaterPredicate(changeDate(-30)) //Month since rained
        assertTrue(predicate.invoke(MyPlant(name = "Plante1", wateredDate = changeDate(-30))))
    }

    private fun changeDate(change: Int): Date {
        val newDateC = Calendar.getInstance()
        val todayDate = todayC.get(Calendar.DAY_OF_YEAR)
        newDateC.set(Calendar.DAY_OF_YEAR, todayDate+change)
        return newDateC.time
    }

}