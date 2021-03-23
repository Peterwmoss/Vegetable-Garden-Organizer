package dk.mifu.pmos.vegetablegardening.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dk.mifu.pmos.vegetablegardening.models.Bed
import dk.mifu.pmos.vegetablegardening.models.Plant

@Database(entities = [Bed::class, Plant::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bedDao(): BedDao
    abstract fun plantDao(): PlantDao

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