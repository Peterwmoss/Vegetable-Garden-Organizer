package dk.mifu.pmos.vegetablegardening.database

import androidx.lifecycle.LiveData
import dk.mifu.pmos.vegetablegardening.enums.BedLocation
import dk.mifu.pmos.vegetablegardening.models.Bed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BedRepository(private val bedDao: BedDao) {

    suspend fun insertBed(bed: Bed) {
        withContext(Dispatchers.IO) {
            bedDao.insert(bed)
        }
    }

    suspend fun updateBed(bed: Bed) {
        withContext(Dispatchers.IO) {
            bedDao.update(bed)
        }
    }

    suspend fun deleteBed(name: String) {
        withContext(Dispatchers.IO) {
            bedDao.delete(name)
        }
    }

    fun findBedsByName(name: String): List<Bed> {
        return bedDao.findBedsByName(name)
    }

    fun findBedByPrimaryKeys(name: String, season: Int): Bed? {
        return bedDao.findByPrimaryKeys(name, season)
    }

    suspend fun findOrder(location: BedLocation, season: Int): Int? {
        return withContext(Dispatchers.IO){
            return@withContext bedDao.findOrder(location.toString(), season)
        }
    }

    fun findBedsWithLocation(location: BedLocation): LiveData<List<Bed>>{
        return bedDao.findBedsWithLocation(location.toString())
    }

    fun findBedsWithSeason(season: Int): List<Bed>{
        return bedDao.findBedsWithSeason(season)
    }
}