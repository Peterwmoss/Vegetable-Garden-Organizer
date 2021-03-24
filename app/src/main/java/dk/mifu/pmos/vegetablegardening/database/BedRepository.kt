package dk.mifu.pmos.vegetablegardening.database

import androidx.lifecycle.LiveData
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

    fun findBed(name: String): Bed? {
        return bedDao.findByName(name)
    }

    fun getAllBeds(): LiveData<List<Bed>> = bedDao.getAll()
}