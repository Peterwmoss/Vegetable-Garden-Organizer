package dk.mifu.pmos.vegetablegardening.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
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