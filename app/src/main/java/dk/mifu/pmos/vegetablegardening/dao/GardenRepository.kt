package dk.mifu.pmos.vegetablegardening.dao

import androidx.lifecycle.LiveData
import dk.mifu.pmos.vegetablegardening.models.Garden

class GardenRepository(private val gardenDao: GardenDao) {

    suspend fun insertGarden(garden: Garden) {
        gardenDao.insert(garden)
    }

    suspend fun updateGarden(garden: Garden) {
        gardenDao.update(garden)
    }

    suspend fun deleteGarden(garden: Garden) {
        gardenDao.delete(garden)
    }

    fun getAllGardens(): LiveData<List<Garden>> = gardenDao.getAll()
}