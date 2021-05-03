package dk.mifu.pmos.vegetablegardening.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dk.mifu.pmos.vegetablegardening.TestUtils
import dk.mifu.pmos.vegetablegardening.enums.BedLocation
import dk.mifu.pmos.vegetablegardening.models.Bed
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.MyPlant
import dk.mifu.pmos.vegetablegardening.models.Season
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import java.io.IOException


class BedDatabaseTest {
    private lateinit var bedDao: BedDao
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
        db.seasonDao().insert(Season(2021))
        db.seasonDao().insert(Season(2018))
        bedDao = db.bedDao()
    }

    @AfterEach
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    @Nested
    @DisplayName("Create bed")
    inner class CreateBedTest {
        private fun createBedNameParameters(): Iterable<Pair<Bed, String>> {
            return listOf(
                    Pair(Bed("Test1", 2021, BedLocation.Outdoors, HashMap(), columns = 0, rows = 0, 0), "Test1"),
                    Pair(Bed("Test2", 2021, BedLocation.Outdoors, HashMap(), columns = 0, rows = 0, 1), "Test2"),
            )
        }

        @TestFactory
        fun createBedWithNameTest() = createBedNameParameters().map { (bed, name) ->
            DynamicTest.dynamicTest("Create bed with a name specified stores a bed with that name") {
                bedDao.insert(bed)
                val byName = bedDao.findByPrimaryKeys(bed.name, bed.season)
                Assertions.assertEquals(name, byName?.name)
            }
        }

        private fun createBedLocationParameters(): Iterable<Pair<Bed, BedLocation>> {
            return listOf(
                    Pair(Bed("Test1", 2021, BedLocation.Outdoors, HashMap(), columns = 0, rows = 0, 0), BedLocation.Outdoors),
                    Pair(Bed("Test2", 2021, BedLocation.Greenhouse, HashMap(), columns = 0, rows = 0, 1), BedLocation.Greenhouse),
            )
        }

        @TestFactory
        fun createBedWithLocationTest() = createBedLocationParameters().map { (bed, location) ->
            DynamicTest.dynamicTest("Create bed with a location specified stores a bed with that location") {
                bedDao.insert(bed)
                val byName = bedDao.findByPrimaryKeys(bed.name, bed.season)
                Assertions.assertEquals(location, byName!!.bedLocation)
            }
        }

        private fun createBedPlantsParameters(): Iterable<Pair<Bed, Map<Coordinate, MyPlant>>> {
            return listOf(
                    Pair(Bed("Test1", 2021, BedLocation.Greenhouse, columns = 0, rows = 0, order = 0), HashMap()),
                    Pair(Bed("Test2", 2021, BedLocation.Greenhouse, mapOf(pair1), columns = 0, rows = 0, order = 1), mapOf(pair1)),
                    Pair(Bed("Test3", 2021, BedLocation.Greenhouse, mapOf(pair1, pair2), columns = 0, rows = 0, order = 2), mapOf(pair1, pair2)),
            )
        }

        @TestFactory
        fun createBedWithPlantsTest() = createBedPlantsParameters().map { (bed, plants) ->
            DynamicTest.dynamicTest("Create bed with plants specified stores a bed with those plants") {
                bedDao.insert(bed)
                val byName = bedDao.findByPrimaryKeys(bed.name, bed.season)
                Assertions.assertEquals(plants, byName!!.plants)
            }
        }

        private fun createBedSizeParameters(): Iterable<Pair<Bed, Pair<Int, Int>>>{
            return listOf(
                    Pair(Bed("Test1", 2021, BedLocation.Greenhouse, columns = 0, rows = 0, order = 0), Pair(0, 0)),
                    Pair(Bed("Test2", 2021, BedLocation.Greenhouse, columns = 1, rows = 0, order = 0), Pair(1, 0)),
                    Pair(Bed("Test3", 2021, BedLocation.Greenhouse, columns = 0, rows = 1, order = 0), Pair(0, 1)),
                    Pair(Bed("Test4", 2021, BedLocation.Greenhouse, columns = 1, rows = 1, order = 0), Pair(1, 1)),
                    Pair(Bed("Test5", 2021, BedLocation.Greenhouse, columns = 2, rows = 1, order = 0), Pair(2, 1))
            )
        }

        @TestFactory
        fun createBedWithSizeTest() = createBedSizeParameters().map { (bed, size) ->
            DynamicTest.dynamicTest("Create bed with columns and rows specified stores a bed with those columns and rows") {
                bedDao.insert(bed)
                val byName = bedDao.findByPrimaryKeys(bed.name, bed.season)
                Assertions.assertEquals(size.first, byName!!.columns)
                Assertions.assertEquals(size.second, byName.rows)
            }
        }

        private fun createBedSeasonParameters(): Iterable<Pair<Bed, Int>> {
            return listOf(
                    Pair(Bed("Test1", 2021, BedLocation.Outdoors, HashMap(), columns = 0, rows = 0, 0), 2021),
                    Pair(Bed("Test2", 2018, BedLocation.Greenhouse, HashMap(), columns = 0, rows = 0, 1), 2018),
            )
        }

        @TestFactory
        fun createBedWithSeasonTest() = createBedSeasonParameters().map { (bed, season) ->
            DynamicTest.dynamicTest("Create bed with a season specified stores a bed with that season") {
                bedDao.insert(bed)
                val byName = bedDao.findByPrimaryKeys(bed.name, bed.season)
                Assertions.assertEquals(season, byName!!.season)
            }
        }

        private fun createBedOrderParameters(): Iterable<Pair<Bed, Int>> {
            return listOf(
                    Pair(Bed("Test1", 2021, BedLocation.Outdoors, HashMap(), columns = 0, rows = 0, 0), 0),
                    Pair(Bed("Test2", 2021, BedLocation.Greenhouse, HashMap(), columns = 0, rows = 0, 1), 1),
            )
        }

        @TestFactory
        fun createBedWithOrderTest() = createBedOrderParameters().map { (bed, order) ->
            DynamicTest.dynamicTest("Create bed with a order specified stores a bed with that order") {
                bedDao.insert(bed)
                val byName = bedDao.findByPrimaryKeys(bed.name, bed.season)
                Assertions.assertEquals(order, byName!!.order)
            }
        }

        @Test
        @DisplayName("Exceptiontest")
        fun createBedWithNonExistentSeasonFailsTest() {
            val bed = Bed("Test", 1, BedLocation.Outdoors, columns = 0, rows = 0, order = 0)
            Assertions.assertThrows(Exception::class.java) { bedDao.insert(bed) }
        }
    }

    @Nested
    @DisplayName("Update bed")
    inner class UpdateBedTest {
        private fun updateBedWithLocationParameters(): Iterable<Pair<Bed, BedLocation>> {
            return listOf(
                    Pair(Bed("Test1", 2021, BedLocation.Greenhouse, columns = 0, rows = 0, order = 0), BedLocation.Greenhouse),
                    Pair(Bed("Test2", 2021, BedLocation.Outdoors, columns = 0, rows = 0, order = 0), BedLocation.Outdoors),
                    Pair(Bed("Test3", 2021, BedLocation.Greenhouse, columns = 0, rows = 0, order = 0), BedLocation.Outdoors),
                    Pair(Bed("Test4", 2021, BedLocation.Outdoors, columns = 0, rows = 0, order = 0), BedLocation.Greenhouse),
            )
        }

        @TestFactory
        fun updateBedWithLocationTest() = updateBedWithLocationParameters().map { (bed, newLocation) ->
            DynamicTest.dynamicTest("Update bed with new location updates the bed to have the new location") {
                val newBed = Bed(bed.name, 2021, newLocation, columns = 0, rows = 0, order = 0)
                bedDao.insert(bed)

                bedDao.update(newBed)
                val byName = bedDao.findByPrimaryKeys(bed.name, bed.season)

                Assertions.assertEquals(newLocation, byName!!.bedLocation)
            }
        }

        private fun updateBedWithPlantsParameters(): Iterable<Pair<Bed, Map<Coordinate, MyPlant>>> {
            return listOf(
                    Pair(Bed("Test1", 2021, BedLocation.Greenhouse, columns = 0, rows = 0, order = 0), mapOf(pair1)),
                    Pair(Bed("Test2", 2021, BedLocation.Greenhouse, mapOf(pair2), columns = 0, rows = 0, order = 0), mapOf(pair1, pair2)),
            )
        }

        @TestFactory
        fun updateBedWithPlantsTest() = updateBedWithPlantsParameters().map { (bed, newPlants) ->
            DynamicTest.dynamicTest("Update bed with new plants updates the bed to have the new plants") {
                val map = bed.plants.toMutableMap()
                map[pair1.first] = pair1.second
                val newBed = Bed(bed.name, 2021, bed.bedLocation, map, columns = 0, rows = 0, order = 0)
                bedDao.insert(bed)

                bedDao.update(newBed)
                val byName = bedDao.findByPrimaryKeys(bed.name, bed.season)

                Assertions.assertEquals(newPlants, byName!!.plants)
            }
        }

        private fun updateBedWithSizeParameters(): Iterable<Pair<Bed, Pair<Int, Int>>> {
            return listOf(
                    Pair(Bed("Test1", 2021, BedLocation.Greenhouse, columns = 0, rows = 0, order = 0), Pair(1, 1)),
                    Pair(Bed("Test2", 2021, BedLocation.Greenhouse, columns = 2, rows = 1, order = 0), Pair(1, 0))
            )
        }

        @TestFactory
        fun updateBedWithSizeTest() = updateBedWithSizeParameters().map { (bed, newSize) ->
            DynamicTest.dynamicTest("Update bed with new size updates the bed to have the new size") {
                bedDao.insert(bed)
                val newBed = Bed(bed.name, 2021, BedLocation.Greenhouse, columns = newSize.first, rows = newSize.second, order = 0)

                bedDao.update(newBed)
                val byName = bedDao.findByPrimaryKeys(bed.name, bed.season)

                Assertions.assertEquals(newSize.first, byName!!.columns)
                Assertions.assertEquals(newSize.second, byName.rows)
            }
        }

        private fun updateBedWithOrderParameters(): Iterable<Pair<Bed, Int>> {
            return listOf(
                    Pair(Bed("Test1", 2021, BedLocation.Greenhouse, columns = 0, rows = 0, order = 0), 1),
                    Pair(Bed("Test2", 2018, BedLocation.Greenhouse, columns = 2, rows = 1, order = 0), 2)
            )
        }

        @TestFactory
        fun updateBedWithOrderTest() = updateBedWithOrderParameters().map { (bed, newOrder) ->
            DynamicTest.dynamicTest("Update bed with new order updates the bed to have the new order") {
                bedDao.insert(bed)
                val newBed = Bed(bed.name, bed.season, BedLocation.Greenhouse, columns = 0, rows = 0, order = newOrder)

                bedDao.update(newBed)
                val byName = bedDao.findByPrimaryKeys(bed.name, bed.season)

                Assertions.assertEquals(newOrder, byName!!.order)
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
            val bed = Bed(name, 2021, BedLocation.Outdoors, columns = 0, rows = 0, order = 0)
            bedDao.insert(bed)

            bedDao.delete(name, 2021)
            val byName = bedDao.findByPrimaryKeys(name, bed.season)

            Assertions.assertNull(byName)
        }

        @Test
        @DisplayName("with name deletes only that bed from database")
        fun deleteBedDoesNotDeleteWrongTest() {
            // Bed 1
            val name1 = "Test1"
            val bed1 = Bed(name1, 2021, BedLocation.Outdoors, columns = 0, rows = 0, order = 0)
            bedDao.insert(bed1)
            // Bed 2
            val name2 = "Test2"
            val bed2 = Bed(name2, 2021, BedLocation.Outdoors, columns = 0, rows = 0, order = 0)
            bedDao.insert(bed2)

            // Delete bed 1
            bedDao.delete(name1, 2021)
            val byName = bedDao.findByPrimaryKeys(name2, bed2.season)

            // Ensure bed 2 still in database
            Assertions.assertNotNull(byName)
        }

        @Test
        @DisplayName("that does not exist does nothing")
        fun deleteBedNoExistDoesNothingTest() {
            val nameToDelete = "Delete"
            val nameToKeep = "Keep"
            val bed = Bed(nameToKeep, 2021, BedLocation.Outdoors, columns = 0, rows = 0, order = 0)
            bedDao.insert(bed)

            bedDao.delete(nameToDelete, 2021)
            val byName = bedDao.findByPrimaryKeys(nameToKeep, bed.season)

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
            val bed = Bed(name, 2021, BedLocation.Outdoors, columns = 0, rows = 0, order = 0)
            bedDao.insert(bed)

            val byName = bedDao.findByPrimaryKeys(name, bed.season)

            Assertions.assertNotNull(byName)
        }

        @Test
        @DisplayName("that does not exists returns null")
        fun findBedThatDoesNotExistTest() {
            val byName = bedDao.findByPrimaryKeys("Test", 2021)

            Assertions.assertNull(byName)
        }
    }

    @Nested
    @DisplayName("Get all beds")
    @ExtendWith(TestUtils.InstantExecutorExtension::class)
    inner class GetAllTest {
        @Test
        @DisplayName("when empty returns empty list")
        fun getAllBedsEmptyTest() {
            val beds = bedDao.getAll()

            beds.observe(TestUtils.TestLifeCycleOwner(), {
                Assertions.assertTrue(it.isEmpty())
            })
        }

        @Test
        @DisplayName("when not empty returns list with beds")
        fun getAllBedsNotEmptyTest() {
            val name = "Test1"
            val bed = Bed(name, 2021, BedLocation.Outdoors, columns = 0, rows = 0, order = 0)
            bedDao.insert(bed)

            val beds = bedDao.getAll()

            beds.observe(TestUtils.TestLifeCycleOwner(), {
                Assertions.assertTrue(it.isNotEmpty())
            })
        }

        @Test
        @DisplayName("when multiple returns list with multiple beds")
        fun getAllBedsMultipleTest() {
            val name1 = "Test1"
            val bed1 = Bed(name1, 2021, BedLocation.Outdoors, columns = 0, rows = 0, order = 0)
            bedDao.insert(bed1)

            val name2 = "Test2"
            val bed2 = Bed(name2, 2021, BedLocation.Outdoors, columns = 0, rows = 0, order = 0)
            bedDao.insert(bed2)

            val beds = bedDao.getAll()

            beds.observe(TestUtils.TestLifeCycleOwner(), {
                Assertions.assertEquals(2, it.size)
            })
        }
    }
}