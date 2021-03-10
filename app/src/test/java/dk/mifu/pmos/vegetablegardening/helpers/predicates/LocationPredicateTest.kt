package dk.mifu.pmos.vegetablegardening.helpers.predicates

import android.app.Instrumentation
import android.content.pm.InstrumentationInfo
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.enums.BedLocation
import dk.mifu.pmos.vegetablegardening.models.Plant
import org.hamcrest.core.Is.`is`
import org.junit.Test

import org.junit.Assert.*

class LocationPredicateTest {

    private val greenhouse = "Drivhus"
    private val outdoors = "Udendørs"

    @Test
    fun invokeGreenhousePlantCorrectLocation() {
        val locationPredicate = LocationPredicate(BedLocation.Greenhouse)
        assertThat(locationPredicate.invoke(Plant(name = "Plante1", category = greenhouse)), `is` (true))
    }

    @Test
    fun invokeGreenhousePlantWrongLocation() {
        val locationPredicate = LocationPredicate(BedLocation.Greenhouse)
        assertThat(locationPredicate.invoke(Plant(name = "Plante2", category = outdoors)), `is` (false))
    }

    @Test
    fun invokeOutdoorsPlantCorrectLocation() {
        val locationPredicate = LocationPredicate(BedLocation.Outdoors)
        assertThat(locationPredicate.invoke(Plant(name = "Plante3", category = outdoors)), `is` (true))
    }

    @Test
    fun invokeOutdoorsPlantWrongLocation() {
        val locationPredicate = LocationPredicate(BedLocation.Outdoors)
        assertThat(locationPredicate.invoke(Plant(name = "Plante2", category = greenhouse)), `is` (false))
    }
}