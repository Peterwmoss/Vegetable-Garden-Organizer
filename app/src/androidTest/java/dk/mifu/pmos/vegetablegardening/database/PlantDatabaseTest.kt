package dk.mifu.pmos.vegetablegardening.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dk.mifu.pmos.vegetablegardening.TestUtils
import dk.mifu.pmos.vegetablegardening.models.Plant
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import java.io.IOException
import java.util.*

class PlantDatabaseTest {
    private lateinit var db: AppDatabase
    private lateinit var plantDao: PlantDao

    companion object {
        private val today = Date(0)

        private fun testPlantWithAllData(name: String): Plant {
            val plant = Plant(name)
            plant.category = "Category"
            plant.earliest = today
            plant.latest = today
            plant.sowing = true
            plant.cropRotation = "Crop Rotation"
            plant.quantity = "Quantity"
            plant.distance = 100
            plant.fertilizer = "Fertilizer"
            plant.harvest = "Harvest"
            return plant
        }

        private fun testPlantWithoutData(name: String): Plant {
            return Plant(name)
        }

        private fun testPlantWithCategory(name: String, category: String): Plant {
            val plant = Plant(name)
            plant.category = category
            return plant
        }

        private fun testPlantWithEarliest(name: String, earliest: Date): Plant {
            val plant = Plant(name)
            plant.earliest = earliest
            return plant
        }

        private fun testPlantWithLatest(name: String, latest: Date): Plant {
            val plant = Plant(name)
            plant.latest = latest
            return plant
        }

        private fun testPlantWithSowing(name: String, sowing: Boolean): Plant {
            val plant = Plant(name)
            plant.sowing = sowing
            return plant
        }

        private fun testPlantWithCropRotation(name: String, cropRotation: String): Plant {
            val plant = Plant(name)
            plant.cropRotation = cropRotation
            return plant
        }

        private fun testPlantWithQuantity(name: String, quantity: String): Plant {
            val plant = Plant(name)
            plant.quantity = quantity
            return plant
        }

        private fun testPlantWithDistance(name: String, distance: Int): Plant {
            val plant = Plant(name)
            plant.distance = distance
            return plant
        }

        private fun testPlantWithFertilizer(name: String, fertilizer: String): Plant {
            val plant = Plant(name)
            plant.fertilizer = fertilizer
            return plant
        }

        private fun testPlantWithHarvest(name: String, harvest: String): Plant {
            val plant = Plant(name)
            plant.harvest = harvest
            return plant
        }
    }

    @BeforeEach
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        plantDao = db.plantDao()
    }

    @AfterEach
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    @Nested
    inner class CreatePlantTest {
        private fun createPlantParameters(): Iterable<Pair<Plant, Plant>> {
            return listOf(
                    Pair(testPlantWithAllData("Test1"), testPlantWithAllData("Test1")),
                    Pair(testPlantWithoutData("Test2"), testPlantWithoutData("Test2")),
            )
        }

        @TestFactory
        fun createPlantTest() = createPlantParameters().map { (plant, expected) ->
            DynamicTest.dynamicTest("Create a plant has same info as when created") {
                plantDao.insert(plant)
                val byName = plantDao.findByName(plant.name)
                Assertions.assertEquals(expected.name, byName!!.name)
                Assertions.assertEquals(expected.category, byName.category)
                Assertions.assertEquals(expected.earliest, byName.earliest)
                Assertions.assertEquals(expected.latest, byName.latest)
                Assertions.assertEquals(expected.sowing, byName.sowing)
                Assertions.assertEquals(expected.cropRotation, byName.cropRotation)
                Assertions.assertEquals(expected.quantity, byName.quantity)
                Assertions.assertEquals(expected.distance, byName.distance)
                Assertions.assertEquals(expected.fertilizer, byName.fertilizer)
                Assertions.assertEquals(expected.harvest, byName.harvest)
            }
        }
    }

    @Nested
    inner class UpdatePlantTest {
        private fun updatePlantParameters(): Iterable<Pair<Plant, Plant>> {
            val category = "New Category"
            val earliest = Date(5)
            val latest = Date(5)
            val sowing = false
            val cropRotation = "New Crop Rotation"
            val quantity = "New Quantity"
            val distance = 200
            val fertilizer = "New Fertilizer"
            val harvest = "New Harvest"

            return listOf(
                    Pair(testPlantWithAllData("Test1"), testPlantWithCategory("Test1", category)),
                    Pair(testPlantWithAllData("Test2"), testPlantWithEarliest("Test2", earliest)),
                    Pair(testPlantWithAllData("Test3"), testPlantWithLatest("Test3", latest)),
                    Pair(testPlantWithAllData("Test4"), testPlantWithSowing("Test4", sowing)),
                    Pair(testPlantWithAllData("Test5"), testPlantWithCropRotation("Test5", cropRotation)),
                    Pair(testPlantWithAllData("Test6"), testPlantWithQuantity("Test6", quantity)),
                    Pair(testPlantWithAllData("Test7"), testPlantWithDistance("Test7", distance)),
                    Pair(testPlantWithAllData("Test8"), testPlantWithFertilizer("Test8", fertilizer)),
                    Pair(testPlantWithAllData("Test9"), testPlantWithHarvest("Test9", harvest)),
            )
        }

        @TestFactory
        fun updateBedWithLocationTest() = updatePlantParameters().map { (plant, expected) ->
            DynamicTest.dynamicTest("Update plant updates field") {
                plantDao.insert(plant)

                plantDao.update(expected)

                val byName = plantDao.findByName(plant.name)
                Assertions.assertEquals(expected.name, byName!!.name)
                Assertions.assertEquals(expected.category, byName.category)
                Assertions.assertEquals(expected.earliest?.time, byName.earliest?.time)
                Assertions.assertEquals(expected.latest, byName.latest)
                Assertions.assertEquals(expected.sowing, byName.sowing)
                Assertions.assertEquals(expected.cropRotation, byName.cropRotation)
                Assertions.assertEquals(expected.quantity, byName.quantity)
                Assertions.assertEquals(expected.distance, byName.distance)
                Assertions.assertEquals(expected.fertilizer, byName.fertilizer)
                Assertions.assertEquals(expected.harvest, byName.harvest)
            }
        }
    }

    @Nested
    @DisplayName("Delete plant")
    inner class DeletePlantTest {
        @Test
        @DisplayName("with name deletes plant from database")
        fun deletePlantTest() {
            val name = "Test1"
            val plant = testPlantWithAllData(name)
            plantDao.insert(plant)

            plantDao.delete(name)
            val byName = plantDao.findByName(name)

            Assertions.assertNull(byName)
        }

        @Test
        @DisplayName("with name deletes only that plant from database")
        fun deletePlantDoesNotDeleteWrongTest() {
            // plant 1
            val name1 = "Test1"
            val plant1 = testPlantWithAllData(name1)
            plantDao.insert(plant1)
            // plant 2
            val name2 = "Test2"
            val plant2 = testPlantWithAllData(name2)
            plantDao.insert(plant2)

            // Delete plant 1
            plantDao.delete(name1)
            val byName = plantDao.findByName(name2)

            // Ensure plant 2 still in database
            Assertions.assertNotNull(byName)
        }

        @Test
        @DisplayName("that does not exist does nothing")
        fun deletePlantNoExistDoesNothingTest() {
            val nameToDelete = "Delete"
            val nameToKeep = "Keep"
            val plant = testPlantWithAllData(nameToKeep)
            plantDao.insert(plant)

            plantDao.delete(nameToDelete)
            val byName = plantDao.findByName(nameToKeep)

            Assertions.assertNotNull(byName)
        }
    }

    @Nested
    @DisplayName("Find plant")
    inner class FindPlantTest {
        @Test
        @DisplayName("that exists returns not null")
        fun findPlantThatExistsTest() {
            val name = "Test1"
            val plant = testPlantWithAllData(name)
            plantDao.insert(plant)

            val byName = plantDao.findByName(name)

            Assertions.assertNotNull(byName)
        }

        @Test
        @DisplayName("that does not exists returns null")
        fun findBedThatDoesNotExistTest() {
            val byName = plantDao.findByName("Test")

            Assertions.assertNull(byName)
        }
    }

    @Nested
    @DisplayName("Get all plants")
    @ExtendWith(TestUtils.InstantExecutorExtension::class)
    inner class GetAllPlantsTest {
        @Test
        @DisplayName("when empty returns empty list")
        fun getAllPlantsEmptyTest() {
            val plants = plantDao.getAll()

            plants.observe(TestUtils.TestLifeCycleOwner(), {
                Assertions.assertTrue(it.isEmpty())
            })
        }

        @Test
        @DisplayName("when not empty returns list with plants")
        fun getAllPlantsNotEmptyTest() {
            val name = "Test1"
            val plant = testPlantWithAllData(name)
            plantDao.insert(plant)

            val plants = plantDao.getAll()

            plants.observe(TestUtils.TestLifeCycleOwner(), {
                Assertions.assertTrue(it.isNotEmpty())
            })
        }

        @Test
        @DisplayName("when multiple returns list with multiple plants")
        fun getAllPlantsMultipleTest() {
            val name1 = "Test1"
            val plant1 = testPlantWithAllData(name1)
            plantDao.insert(plant1)

            val name2 = "Test2"
            val plant2 = testPlantWithAllData(name2)
            plantDao.insert(plant2)

            val plants = plantDao.getAll()

            plants.observe(TestUtils.TestLifeCycleOwner(), {
                Assertions.assertEquals(2, it.size)
            })
        }
    }
}