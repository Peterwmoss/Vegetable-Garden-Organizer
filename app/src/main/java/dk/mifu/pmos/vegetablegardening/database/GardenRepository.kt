package dk.mifu.pmos.vegetablegardening.database

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

    suspend fun findBed(name: String) {
        gardenDao.findByName(name)
    }

    fun getAllBeds(): LiveData<List<Bed>> = gardenDao.getAll()
}