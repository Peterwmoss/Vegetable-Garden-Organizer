package dk.mifu.pmos.vegetablegardening.database

import androidx.lifecycle.LiveData
import dk.mifu.pmos.vegetablegardening.enums.BedLocation
import dk.mifu.pmos.vegetablegardening.models.Bed
import dk.mifu.pmos.vegetablegardening.models.Season
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

    fun findBed(name: String): Bed? {
        return bedDao.findByName(name)
    }

    fun getAllBeds(): LiveData<List<Bed>> = bedDao.getAll()

    suspend fun findOrder(location: BedLocation, season: Int): Int? {
        return withContext(Dispatchers.IO){
            return@withContext bedDao.findOrder(location.toString(), season)
        }
    }
}