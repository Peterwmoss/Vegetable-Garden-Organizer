package dk.mifu.pmos.vegetablegardening.database

import androidx.lifecycle.LiveData
import androidx.room.*
import dk.mifu.pmos.vegetablegardening.enums.BedLocation
import dk.mifu.pmos.vegetablegardening.models.Bed
import dk.mifu.pmos.vegetablegardening.models.Season

@Dao
interface BedDao {
    @Query("SELECT * FROM beds ORDER BY `order`")
    fun getAll(): LiveData<List<Bed>>

    @Query("SELECT * FROM beds where name LIKE (:name) ORDER BY season DESC")
    fun findBedsByName(name: String): List<Bed>

    @Query("SELECT * FROM beds where name LIKE (:name) AND season = (:season) LIMIT 1")
    fun findByPrimaryKeys(name: String, season: Int): Bed?

    @Insert
    fun insert(bed: Bed)

    @Update
    fun update(bed: Bed)

    @Query("DELETE FROM beds where name LIKE (:name)")
    fun delete(name: String)

    @Query("SELECT MAX(`order`) FROM beds where bedLocation LIKE (:location) AND season = (:season)")
    fun findOrder(location: String, season: Int): Int?

    @Query("SELECT * FROM beds where bedLocation LIKE (:location)")
    fun findBedsWithLocation(location: String): LiveData<List<Bed>>

    @Query("SELECT * FROM beds where season = (:season)")
    fun findBedsWithSeason(season: Int): List<Bed>
}