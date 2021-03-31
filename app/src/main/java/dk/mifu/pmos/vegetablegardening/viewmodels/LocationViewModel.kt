package dk.mifu.pmos.vegetablegardening.viewmodels

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import java.util.*

class LocationViewModel(application: Application): AndroidViewModel(application) {
    var location: Location? = null
    var lastRained: Date? = null
}