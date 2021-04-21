package dk.mifu.pmos.vegetablegardening.viewmodels

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import dk.mifu.pmos.vegetablegardening.models.Weather

class LocationViewModel(application: Application): AndroidViewModel(application) {
    val location: MutableLiveData<Location> = MutableLiveData()
    val weather: MutableLiveData<Weather> = MutableLiveData()
}