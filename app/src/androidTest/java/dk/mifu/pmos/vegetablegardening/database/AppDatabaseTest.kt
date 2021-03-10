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
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.io.IOException

@RunWith(Parameterized::class)
class AppDatabaseTest(
    private val bed: Bed,
    private val bedName: String,
    private val bedLocation: BedLocation,
    private val plants: Map<Coordinate, MyPlant>
    ) {
    private lateinit var gardenDao: GardenDao
    private lateinit var db: AppDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        gardenDao = db.gardenDao()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    companion object  {
        @JvmStatic
        @Parameterized.Parameters
        fun beds(): Iterable<Array<Any>> {
            val pair1 = Pair(Coordinate(0,0), MyPlant("Plant1"))
            val pair2 = Pair(Coordinate(1,1), MyPlant("Plant2"))
            return arrayListOf(
                // Name
                arrayOf(Bed("Test1", Outdoors, HashMap()), "Test1", Outdoors, HashMap<Coordinate, MyPlant>()),
                arrayOf(Bed("Test2", Outdoors, HashMap()), "Test2", Outdoors, HashMap<Coordinate, MyPlant>()),

                // Location
                arrayOf(Bed("Test", Outdoors, HashMap()), "Test", Outdoors, HashMap<Coordinate, MyPlant>()),
                arrayOf(Bed("Test", Greenhouse, HashMap()), "Test", Greenhouse, HashMap<Coordinate, MyPlant>()),

                // Plants
                arrayOf(Bed("Test", Greenhouse), "Test", Greenhouse, HashMap<Coordinate, MyPlant>()),
                arrayOf(Bed("Test", Greenhouse, mapOf(pair1)), "Test", Greenhouse, mapOf(pair1)),
                arrayOf(Bed("Test", Greenhouse, mapOf(pair1, pair2)), "Test", Greenhouse, mapOf(pair1, pair2)),
            )
        }
    }

    @Test
    @Throws(Exception::class)
    fun createBedWithNameTest() {
        gardenDao.insert(bed)
        val byName = gardenDao.findByName(bed.name)
        assertThat(byName.name, `is`(bedName))
    }

    @Test
    @Throws(Exception::class)
    fun createBedWithLocationTest() {
        gardenDao.insert(bed)
        val byName = gardenDao.findByName(bed.name)
        assertThat(byName.bedLocation, `is`(bedLocation))
    }

    @Test
    @Throws(Exception::class)
    fun createBedWithPlantsTest() {
        gardenDao.insert(bed)
        val byName = gardenDao.findByName(bed.name)
        assertThat(byName.plants, `is`(plants))
    }
}