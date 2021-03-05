package dk.mifu.pmos.vegetablegardening.database

import androidx.lifecycle.LiveData
import dk.mifu.pmos.vegetablegardening.models.Bed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GardenRepository(private val gardenDao: GardenDao) {

    suspend fun insertBed(bed: Bed) {
        withContext(Dispatchers.IO) {
            gardenDao.insert(bed)
        }
    }

    suspend fun updateBed(bed: Bed) {
        withContext(Dispatchers.IO) {
            gardenDao.update(bed)
        }
    }

    suspend fun deleteBed(bed: Bed) {
        withContext(Dispatchers.IO) {
            gardenDao.delete(bed)
        }
    }

    suspend fun findBed(name: String) {
        withContext(Dispatchers.IO) {
            gardenDao.findByName(name)
        }
    }

    fun getAllBeds(): LiveData<List<Bed>> = gardenDao.getAll()
}