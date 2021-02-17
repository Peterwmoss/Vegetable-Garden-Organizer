package dk.mifu.pmos.vegetablegardening.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import dk.mifu.pmos.vegetablegardening.dao.GardenRepository
import dk.mifu.pmos.vegetablegardening.database.AppDatabase
import dk.mifu.pmos.vegetablegardening.enums.Location
import dk.mifu.pmos.vegetablegardening.models.Garden
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class GardenViewModel(application: Application) : AndroidViewModel(application) {
    private val gardenDb = AppDatabase.getDatabase(getApplication()).gardenDao()
    private val repository = GardenRepository(gardenDb)
    val gardens: LiveData<List<Garden>> = repository.getAllGardens()
}