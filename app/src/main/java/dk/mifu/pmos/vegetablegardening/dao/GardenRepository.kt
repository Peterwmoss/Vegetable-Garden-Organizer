package dk.mifu.pmos.vegetablegardening.dao

import androidx.lifecycle.LiveData
import dk.mifu.pmos.vegetablegardening.models.Bed

class GardenRepository(private val gardenDao: GardenDao) {

    suspend fun insertBed(bed: Bed) {
        gardenDao.insert(bed)
    }

    suspend fun updateBed(bed: Bed) {
        gardenDao.update(bed)
    }

    suspend fun deleteBed(bed: Bed) {
        gardenDao.delete(bed)
    }

    fun getAllBeds(): LiveData<List<Bed>> = gardenDao.getAll()
}