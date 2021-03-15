package dk.mifu.pmos.vegetablegardening.database

import android.content.Context
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dk.mifu.pmos.vegetablegardening.TestUtils
import dk.mifu.pmos.vegetablegardening.TestUtils.InstantExecutorExtension
import dk.mifu.pmos.vegetablegardening.TestUtils.TestLifeCycleOwner
import dk.mifu.pmos.vegetablegardening.enums.BedLocation
import dk.mifu.pmos.vegetablegardening.enums.BedLocation.Greenhouse
import dk.mifu.pmos.vegetablegardening.enums.BedLocation.Outdoors
import dk.mifu.pmos.vegetablegardening.models.Bed
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.MyPlant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.*
import java.io.IOException


class AppDatabaseTest {
    private lateinit var gardenDao: GardenDao
    private lateinit var db: AppDatabase

    companion object {
        // Globally available test parameters
        private val pair1 = Pair(Coordinate(0, 0), MyPlant("Plant1"))
        private val pair2 = Pair(Coordinate(1, 1), MyPlant("Plant2"))
    }

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

    @Nested
    inner class CreateBedTest {
        private fun createBedNameParameters(): Iterable<Pair<Bed, String>> {
            return listOf(
                    Pair(Bed("Test1", Outdoors, HashMap()), "Test2"),
                    Pair(Bed("Test2", Outdoors, HashMap()), "Test2"),
            )
        }

        @TestFactory
        fun createBedWithNameTest() = createBedNameParameters().map { (bed, name) ->
            DynamicTest.dynamicTest("Create bed with a name specified stores a bed with that name") {
                gardenDao.insert(bed)
                val byName = gardenDao.findByName(bed.name)
                Assertions.assertEquals(name, byName.name)
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
            DynamicTest.dynamicTest("Create bed with a location specified stores a bed with that location") {
                gardenDao.insert(bed)
                val byName = gardenDao.findByName(bed.name)
                Assertions.assertEquals(location, byName.bedLocation)
            }
        }

        private fun createBedPlantsParameters(): Iterable<Pair<Bed, Map<Coordinate, MyPlant>>> {
            return listOf(
                    Pair(Bed("Test1", Greenhouse), HashMap()),
                    Pair(Bed("Test2", Greenhouse, mapOf(pair1)), mapOf(pair1)),
                    Pair(Bed("Test3", Greenhouse, mapOf(pair1, pair2)), mapOf(pair1, pair2)),
            )
        }

        @TestFactory
        fun createBedWithPlantsTest() = createBedPlantsParameters().map { (bed, plants) ->
            DynamicTest.dynamicTest("Create bed with plants specified stores a bed with those plants") {
                gardenDao.insert(bed)
                val byName = gardenDao.findByName(bed.name)
                Assertions.assertEquals(plants, byName.plants)
            }
        }
    }

    @Nested
    inner class UpdateBedTest {
        private fun updateBedWithLocationParameters(): Iterable<Pair<Bed, BedLocation>> {
            return listOf(
                    Pair(Bed("Test1", Greenhouse), Greenhouse),
                    Pair(Bed("Test2", Outdoors), Outdoors),
                    Pair(Bed("Test3", Greenhouse), Outdoors),
                    Pair(Bed("Test4", Outdoors), Greenhouse),
            )
        }

        @TestFactory
        fun updateBedWithLocationTest() = updateBedWithLocationParameters().map { (bed, newLocation) ->
            DynamicTest.dynamicTest("Update bed with new location updates the bed to have the new location") {
                val newBed = Bed(bed.name, newLocation)
                gardenDao.insert(bed)

                gardenDao.update(newBed)
                val byName = gardenDao.findByName(bed.name)

                Assertions.assertEquals(newLocation, byName.bedLocation)
            }
        }

        private fun updateBedWithPlantsParameters(): Iterable<Pair<Bed, Map<Coordinate, MyPlant>>> {
            return listOf(
                    Pair(Bed("Test1", Greenhouse), mapOf(pair1)),
                    Pair(Bed("Test2", Greenhouse, mapOf(pair2)), mapOf(pair1, pair2)),
            )
        }

        @TestFactory
        fun updateBedWithPlantsTest() = updateBedWithPlantsParameters().map { (bed, newPlants) ->
            DynamicTest.dynamicTest("Update bed with new plants updates the bed to have the new plants") {
                val map = bed.plants.toMutableMap()
                map[pair1.first] = pair1.second
                val newBed = Bed(bed.name, bed.bedLocation, map)
                gardenDao.insert(bed)

                gardenDao.update(newBed)
                val byName = gardenDao.findByName(bed.name)

                Assertions.assertEquals(newPlants, byName.plants)
            }
        }
    }

    @Nested
    @DisplayName("Delete bed")
    inner class DeleteBedTest {
        @Test
        @DisplayName("with name deletes bed from database")
        fun deleteBedTest() {
            val name = "Test1"
            val bed = Bed(name, Outdoors)
            gardenDao.insert(bed)

            gardenDao.delete(name)
            val byName = gardenDao.findByName(name)

            Assertions.assertNull(byName)
        }

        @Test
        @DisplayName("with name deletes only that bed from database")
        fun deleteBedDoesNotDeleteWrongTest() {
            // Bed 1
            val name1 = "Test1"
            val bed1 = Bed(name1, Outdoors)
            gardenDao.insert(bed1)
            // Bed 2
            val name2 = "Test2"
            val bed2 = Bed(name2, Outdoors)
            gardenDao.insert(bed2)

            // Delete bed 1
            gardenDao.delete(name1)
            val byName = gardenDao.findByName(name2)

            // Ensure bed 2 still in database
            Assertions.assertNotNull(byName)
        }

        @Test
        @DisplayName("that does not exist does nothing")
        fun deleteBedNoExistDoesNothingTest() {
            val nameToDelete = "Delete"
            val nameToKeep = "Keep"
            val bed = Bed(nameToKeep, Outdoors)
            gardenDao.insert(bed)

            gardenDao.delete(nameToDelete)
            val byName = gardenDao.findByName(nameToKeep)

            Assertions.assertNotNull(byName)
        }
    }

    @Nested
    @DisplayName("Find bed")
    inner class FindBedTest {
        @Test
        @DisplayName("that exists returns not null")
        fun findBedThatExistsTest() {
            val name = "Test1"
            val bed = Bed(name, Outdoors)
            gardenDao.insert(bed)

            val byName = gardenDao.findByName(name)

            Assertions.assertNotNull(byName)
        }

        @Test
        @DisplayName("that does not exists returns null")
        fun findBedThatDoesNotExistTest() {
            val byName = gardenDao.findByName("Test")

            Assertions.assertNull(byName)
        }
    }

    @Nested
    @DisplayName("Get all beds")
    @ExtendWith(InstantExecutorExtension::class)
    inner class GetAllTest {
        @Test
        @DisplayName("when empty returns empty list")
        fun getAllBedsEmptyTest() {
            val beds = gardenDao.getAll()

            beds.observe(TestLifeCycleOwner(), {
                Assertions.assertTrue(it.isEmpty())
            })
        }

        @Test
        @DisplayName("when not empty returns list with beds")
        fun getAllBedsNotEmptyTest() {
            val name = "Test1"
            val bed = Bed(name, Outdoors)
            gardenDao.insert(bed)

            val beds = gardenDao.getAll()

            beds.observe(TestLifeCycleOwner(), {
                Assertions.assertTrue(it.isNotEmpty())
            })
        }

        @Test
        @DisplayName("when multiple returns list with multiple beds")
        fun getAllBedsMultipleTest() {
            val name1 = "Test1"
            val bed1 = Bed(name1, Outdoors)
            gardenDao.insert(bed1)

            val name2 = "Test2"
            val bed2 = Bed(name2, Outdoors)
            gardenDao.insert(bed2)

            val beds = gardenDao.getAll()

            beds.observe(TestLifeCycleOwner(), {
                Assertions.assertEquals(2, it.size)
            })
        }
    }
}