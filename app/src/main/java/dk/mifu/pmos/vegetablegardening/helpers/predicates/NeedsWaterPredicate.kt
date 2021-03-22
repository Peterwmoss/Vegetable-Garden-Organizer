package dk.mifu.pmos.vegetablegardening.helpers.predicates

import dk.mifu.pmos.vegetablegardening.models.MyPlant
import java.util.*

class NeedsWaterPredicate(private val lastRainDate: Date): Predicate<MyPlant?> {
    override fun invoke(plant: MyPlant?): Boolean {
        if (plant == null)
            return false
        val wateredDate = plant.wateredDate
        val today = Date().time
        return if (wateredDate != null) {
            ((today - wateredDate.time) > MAX_TIME_SINCE_LAST_WATER
                    && (today - lastRainDate.time) > MAX_TIME_SINCE_LAST_WATER)
        }
        else {
            (today - lastRainDate.time) > MAX_TIME_SINCE_LAST_WATER
        }
    }

    companion object {
        private const val ONE_DAY_IN_MILLIS: Long = (60*60*24)*1000
        private const val MAX_TIME_SINCE_LAST_WATER: Long = 5 * ONE_DAY_IN_MILLIS
    }
}