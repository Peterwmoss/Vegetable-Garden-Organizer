package dk.mifu.pmos.vegetablegardening.database

import androidx.lifecycle.LiveData
import androidx.room.*
import dk.mifu.pmos.vegetablegardening.models.Bed

@Dao
interface BedDao {
    @Query("SELECT * FROM beds")
    fun getAll(): LiveData<List<Bed>>

    @Query("SELECT * FROM beds where name LIKE (:name) LIMIT 1")
    fun findByName(name: String): Bed

    @Insert
    fun insert(bed: Bed)

    @Update
    fun update(bed: Bed)

    @Query("DELETE FROM beds where name LIKE (:name)")
    fun delete(name: String)
}