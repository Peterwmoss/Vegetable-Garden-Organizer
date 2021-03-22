package dk.mifu.pmos.vegetablegardening.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dk.mifu.pmos.vegetablegardening.models.Bed
import dk.mifu.pmos.vegetablegardening.models.Season

@Dao
interface SeasonDao {
    @Query("SELECT * FROM seasons")
    fun getAll(): LiveData<List<Season>>

    @Insert
    fun insert(season: Season)

    @Query("SELECT MAX(season) FROM seasons")
    fun getLatestSeason(): Int?

    @Query("SELECT * FROM seasons where season = (:year) LIMIT 1")
    fun findByName(year: Int): Season?
}