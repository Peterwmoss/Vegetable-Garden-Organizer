package dk.mifu.pmos.vegetablegardening.viewmodels

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.databinding.ObservableArrayMap
import androidx.databinding.ObservableMap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import dk.mifu.pmos.vegetablegardening.enums.BedLocation
import dk.mifu.pmos.vegetablegardening.helpers.callbacks.UpdateBedCallback
import dk.mifu.pmos.vegetablegardening.helpers.predicates.NeedsWaterPredicate
import dk.mifu.pmos.vegetablegardening.helpers.weather.LocationService
import dk.mifu.pmos.vegetablegardening.helpers.weather.WeatherData
import dk.mifu.pmos.vegetablegardening.models.Bed
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.MyPlant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap

class BedViewModel(application: Application) : AndroidViewModel(application) {
    var name : String? = null
    var bedLocation : BedLocation? = null
    var plants : ObservableMap<Coordinate, MyPlant>? = null
    var tileIds : MutableMap<Coordinate, Int>? = null
    var plantsToWater : MutableLiveData<MutableMap<Coordinate, MyPlant>> = MutableLiveData()

    fun setBed(bed: Bed){
        plantsToWater.value = null
        val map = ObservableArrayMap<Coordinate, MyPlant>()
        bed.plants.forEach {
            map[it.key] = it.value
        }

        name = bed.name
        bedLocation = bed.bedLocation
        plants = map
        tileIds = HashMap()

        map.addOnMapChangedCallback(UpdateBedCallback(name!!, bedLocation!!, getApplication()))

        getWeatherData()
    }

    private fun getWeatherData() {
        getApplication<Application>().startService(Intent(getApplication(), LocationService::class.java))
        LocalBroadcastManager.getInstance(getApplication()).registerReceiver(WeatherDataReceiver(), IntentFilter("sendLocation"))
    }

    private fun setPlantsToWater(date: Date) {
        val map = HashMap<Coordinate, MyPlant>()
        val filteredPlants = plants?.filterValues(NeedsWaterPredicate(date))
        if (filteredPlants != null) {
            map.putAll(filteredPlants)
            plantsToWater.value = map
        }
    }

    private inner class WeatherDataReceiver: BroadcastReceiver() {
        private val weatherData = object : WeatherData(getApplication()) {
            override fun handleResponse(date: Date?) {
                Log.d("handleResponse()", "date: $date")
                if (date != null) { setPlantsToWater(date) }
                getApplication<Application>().stopService(Intent(getApplication(), LocationService::class.java))
            }
        }

        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("onReceive()", "Location intent received")
            viewModelScope.launch(Dispatchers.IO) {
                val bundle = intent?.getBundleExtra("location")
                val location = bundle?.getParcelable<android.location.Location>("location")
                location?.let { weatherData.getLastRained(it) }
            }
        }
    }
}