package dk.mifu.pmos.vegetablegardening.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import dk.mifu.pmos.vegetablegardening.models.Plant

@Dao
interface PlantDao {
    @Query("SELECT * FROM plants")
    fun getAll(): LiveData<List<Plant>>

    @Query("SELECT * FROM plants where name LIKE (:name) LIMIT 1")
    fun findByName(name: String): Plant

    @Insert
    fun insert(plant: Plant)

    @Update
    fun update(plant: Plant)

    @Query("DELETE FROM plants where name LIKE (:name)")
    fun delete(name: String)
}
