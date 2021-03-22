package dk.mifu.pmos.vegetablegardening.database

import androidx.lifecycle.LiveData
import dk.mifu.pmos.vegetablegardening.models.Season
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SeasonRepository(private val seasonDao: SeasonDao) {
    fun getAllSeasons(): LiveData<List<Season>> = seasonDao.getAll()

    suspend fun insertSeason(year: Int) {
        withContext(Dispatchers.IO) {
            seasonDao.insert(Season(year))
        }
    }

    fun getLatestSeason(): Int? = seasonDao.getLatestSeason()

    fun getSeason(year: Int): Int? = seasonDao.findByName(year)?.season
}