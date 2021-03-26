package dk.mifu.pmos.vegetablegardening.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dk.mifu.pmos.vegetablegardening.TestUtils.InstantExecutorExtension
import dk.mifu.pmos.vegetablegardening.TestUtils.TestLifeCycleOwner
import dk.mifu.pmos.vegetablegardening.enums.BedLocation
import dk.mifu.pmos.vegetablegardening.enums.BedLocation.Greenhouse
import dk.mifu.pmos.vegetablegardening.enums.BedLocation.Outdoors
import dk.mifu.pmos.vegetablegardening.models.Bed
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.MyPlant
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.*
import java.io.IOException


class AppDatabaseTest {
    private lateinit var db: AppDatabase

    @AfterEach
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    @Test
    @DisplayName("Database loads without crashing")
    fun databaseLoadsWithoutCrashing() {
        assertDoesNotThrow {
            val context = ApplicationProvider.getApplicationContext<Context>()
            db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        }
    }
}