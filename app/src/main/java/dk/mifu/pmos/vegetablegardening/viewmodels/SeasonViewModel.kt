package dk.mifu.pmos.vegetablegardening.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dk.mifu.pmos.vegetablegardening.database.AppDatabase
import dk.mifu.pmos.vegetablegardening.database.SeasonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class SeasonViewModel(application: Application): AndroidViewModel(application) {
    var currentSeason: MutableLiveData<Int>

    init {
        runBlocking(Dispatchers.IO) {
            val dao = AppDatabase.getDatabase(getApplication()).seasonDao()
            val repository = SeasonRepository(dao)
            val latestSeason = repository.getLatestSeason()
            currentSeason = if (latestSeason == null) {
                val year = Calendar.getInstance().get(Calendar.YEAR)
                repository.insertSeason(year)
                MutableLiveData(year)
            } else {
                MutableLiveData(latestSeason)
            }
        }
    }
}