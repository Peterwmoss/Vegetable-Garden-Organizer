package dk.mifu.pmos.vegetablegardening.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import dk.mifu.pmos.vegetablegardening.models.Garden

@Dao
interface GardenDao {
    @Query("SELECT * FROM gardens")
    fun getAll(): LiveData<List<Garden>>

    @Query("SELECT * FROM gardens where name LIKE (:name) LIMIT 1")
    fun findByName(name: String): Garden

    @Insert
    fun insert(garden: Garden)

    @Update
    fun update(garden: Garden)

    @Delete
    fun delete(garden: Garden)
}