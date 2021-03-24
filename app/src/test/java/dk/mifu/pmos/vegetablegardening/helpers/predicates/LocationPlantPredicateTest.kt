package dk.mifu.pmos.vegetablegardening.helpers.predicates

import dk.mifu.pmos.vegetablegardening.enums.BedLocation
import dk.mifu.pmos.vegetablegardening.models.Plant
import org.junit.Test

import org.junit.Assert.*

class LocationPlantPredicateTest {

    private val greenhouse = "Drivhus"
    private val outdoors = "Udendørs"

    @Test
    fun invokeGreenhousePlantCorrectLocation() {
        val locationPredicate = LocationPlantPredicate(BedLocation.Greenhouse)
        assertTrue(locationPredicate.invoke(Plant(name = "Plante1", category = greenhouse)))
    }

    @Test
    fun invokeGreenhousePlantWrongLocation() {
        val locationPredicate = LocationPlantPredicate(BedLocation.Greenhouse)
        assertFalse(locationPredicate.invoke(Plant(name = "Plante2", category = outdoors)))
    }

    @Test
    fun invokeOutdoorsPlantCorrectLocation() {
        val locationPredicate = LocationPlantPredicate(BedLocation.Outdoors)
        assertTrue(locationPredicate.invoke(Plant(name = "Plante3", category = outdoors)))
    }

    @Test
    fun invokeOutdoorsPlantWrongLocation() {
        val locationPredicate = LocationPlantPredicate(BedLocation.Outdoors)
        assertFalse(locationPredicate.invoke(Plant(name = "Plante2", category = greenhouse)))
    }
}