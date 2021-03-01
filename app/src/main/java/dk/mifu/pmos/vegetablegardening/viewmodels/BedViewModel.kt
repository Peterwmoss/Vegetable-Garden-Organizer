package dk.mifu.pmos.vegetablegardening.viewmodels

import android.app.Application
import androidx.databinding.ObservableArrayMap
import androidx.databinding.ObservableMap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dk.mifu.pmos.vegetablegardening.database.GardenRepository
import dk.mifu.pmos.vegetablegardening.database.AppDatabase
import dk.mifu.pmos.vegetablegardening.enums.Location
import dk.mifu.pmos.vegetablegardening.models.Bed
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.Plant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BedViewModel(application: Application) : AndroidViewModel(application) {
    var name : String? = null
    var location : Location? = null
    var plants : ObservableMap<Coordinate, Plant>? = null
    var tileIds : MutableMap<Coordinate, Int>? = null

    fun setBed(bed: Bed){
        val map = ObservableArrayMap<Coordinate, Plant>()
        bed.plants.forEach {
            map[it.key] = it.value
        }

        map.addOnMapChangedCallback(Callback())

        name = bed.name
        location = bed.location
        plants = map
    }

    private inner class Callback : ObservableMap.OnMapChangedCallback<ObservableMap<Coordinate, Plant>, Coordinate, Plant>() {
        override fun onMapChanged(sender: ObservableMap<Coordinate, Plant>?, key: Coordinate?) {
            viewModelScope.launch(Dispatchers.IO) {
                val dao = AppDatabase.getDatabase(getApplication()).gardenDao()
                val repository = GardenRepository(dao)
                repository.updateBed(Bed(name!!, location!!, sender!!))
            }
        }
    }
}