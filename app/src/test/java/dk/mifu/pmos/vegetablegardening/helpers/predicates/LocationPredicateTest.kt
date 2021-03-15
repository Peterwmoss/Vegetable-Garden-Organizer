package dk.mifu.pmos.vegetablegardening.helpers.predicates

import dk.mifu.pmos.vegetablegardening.enums.BedLocation
import dk.mifu.pmos.vegetablegardening.models.Plant
import org.junit.Test

import org.junit.Assert.*

class LocationPredicateTest {

    private val greenhouse = "Drivhus"
    private val outdoors = "Udendørs"

    @Test
    fun invokeGreenhousePlantCorrectLocation() {
        val locationPredicate = LocationPredicate(BedLocation.Greenhouse)
        assertTrue(locationPredicate.invoke(Plant(name = "Plante1", category = greenhouse)))
    }

    @Test
    fun invokeGreenhousePlantWrongLocation() {
        val locationPredicate = LocationPredicate(BedLocation.Greenhouse)
        assertFalse(locationPredicate.invoke(Plant(name = "Plante2", category = outdoors)))
    }

    @Test
    fun invokeOutdoorsPlantCorrectLocation() {
        val locationPredicate = LocationPredicate(BedLocation.Outdoors)
        assertTrue(locationPredicate.invoke(Plant(name = "Plante3", category = outdoors)))
    }

    @Test
    fun invokeOutdoorsPlantWrongLocation() {
        val locationPredicate = LocationPredicate(BedLocation.Outdoors)
        assertFalse(locationPredicate.invoke(Plant(name = "Plante2", category = greenhouse)))
    }
}