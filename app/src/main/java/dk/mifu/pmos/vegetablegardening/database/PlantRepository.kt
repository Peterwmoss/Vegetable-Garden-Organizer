package dk.mifu.pmos.vegetablegardening.database

import androidx.lifecycle.LiveData
import dk.mifu.pmos.vegetablegardening.models.Plant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlantRepository(private val plantDao: PlantDao) {

    suspend fun insertPlant(plant: Plant) {
        withContext(Dispatchers.IO) {
            plantDao.insert(plant)
        }
    }

    suspend fun updatePlant(plant: Plant) {
        withContext(Dispatchers.IO) {
            plantDao.update(plant)
        }
    }

    suspend fun deletePlant(name: String) {
        withContext(Dispatchers.IO) {
            plantDao.delete(name)
        }
    }

    fun findPlant(name: String): Plant? {
        return plantDao.findByName(name)
    }

    fun getAllPlants(): LiveData<MutableList<Plant>> = plantDao.getAll()
}
