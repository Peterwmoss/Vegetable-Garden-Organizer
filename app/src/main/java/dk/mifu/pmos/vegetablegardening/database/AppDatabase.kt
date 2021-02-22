package dk.mifu.pmos.vegetablegardening.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dk.mifu.pmos.vegetablegardening.dao.GardenDao
import dk.mifu.pmos.vegetablegardening.models.Bed

@Database(entities = [Bed::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gardenDao(): GardenDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null)
                return tempInstance

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java, "app-database"
                ).build()

                INSTANCE = instance
                return instance
            }
        }
    }
}