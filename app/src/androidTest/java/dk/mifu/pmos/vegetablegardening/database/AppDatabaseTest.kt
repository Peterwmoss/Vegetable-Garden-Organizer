package dk.mifu.pmos.vegetablegardening.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dk.mifu.pmos.vegetablegardening.enums.BedLocation
import dk.mifu.pmos.vegetablegardening.enums.BedLocation.Greenhouse
import dk.mifu.pmos.vegetablegardening.enums.BedLocation.Outdoors
import dk.mifu.pmos.vegetablegardening.models.Bed
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.MyPlant
import java.io.IOException
import org.junit.jupiter.api.*

class AppDatabaseTest {
    private lateinit var gardenDao: GardenDao
    private lateinit var db: AppDatabase

    @BeforeEach
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        gardenDao = db.gardenDao()
    }

    @AfterEach
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    private fun createBedNameParameters(): Iterable<Pair<Bed, String>> {
        return listOf(
            Pair(Bed("Test1", Outdoors, HashMap()), "Test1"),
            Pair(Bed("Test2", Outdoors, HashMap()), "Test2"),
        )
    }

    @TestFactory
    fun createBedWithNameTest() = createBedNameParameters().map { (bed, name) ->
        DynamicTest.dynamicTest("when I create a bed with bla bla") {
                gardenDao.insert(bed)
                val byName = gardenDao.findByName(bed.name)
                Assertions.assertEquals(byName.name, name)
            }
        }

    private fun createBedLocationParameters(): Iterable<Pair<Bed, BedLocation>> {
        return listOf(
            Pair(Bed("Test1", Outdoors, HashMap()), Outdoors),
            Pair(Bed("Test2", Greenhouse, HashMap()), Greenhouse),
        )
    }

    @TestFactory
    fun createBedWithLocationTest() = createBedLocationParameters().map { (bed, location) ->
        DynamicTest.dynamicTest("when I create a bed with bla bla") {
            gardenDao.insert(bed)
            val byName = gardenDao.findByName(bed.name)
            Assertions.assertEquals(byName.bedLocation, location)
        }
    }

    private fun createBedPlantsParameters(): Iterable<Pair<Bed, Map<Coordinate, MyPlant>>> {
        val pair1 = Pair(Coordinate(0, 0), MyPlant("Plant1"))
        val pair2 = Pair(Coordinate(1, 1), MyPlant("Plant2"))
        return listOf(
            Pair(Bed("Test1", Greenhouse), HashMap()),
            Pair(Bed("Test2", Greenhouse, mapOf(pair1)), mapOf(pair1)),
            Pair( Bed("Test3", Greenhouse, mapOf(pair1, pair2)), mapOf(pair1, pair2)),
        )
    }

    @TestFactory
    fun createBedWithPlantsTest() = createBedPlantsParameters().map { (bed, plants) ->
        DynamicTest.dynamicTest("when I create a bed with bla bla") {
            gardenDao.insert(bed)
            val byName = gardenDao.findByName(bed.name)
            Assertions.assertEquals(byName.plants, plants)
        }
    }
}