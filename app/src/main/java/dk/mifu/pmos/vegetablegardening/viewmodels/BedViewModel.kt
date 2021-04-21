package dk.mifu.pmos.vegetablegardening.viewmodels

import android.app.Application
import androidx.databinding.ObservableArrayMap
import androidx.databinding.ObservableMap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import dk.mifu.pmos.vegetablegardening.enums.BedLocation
import dk.mifu.pmos.vegetablegardening.helpers.predicates.NeedsWaterPredicate
import dk.mifu.pmos.vegetablegardening.models.Bed
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.MyPlant
import java.util.*
import kotlin.collections.HashMap

class BedViewModel(application: Application) : AndroidViewModel(application) {
    var name : String? = null
    var season: Int? = null
    var bedLocation : BedLocation? = null
    var plants : ObservableMap<Coordinate, MyPlant>? = null
    var tileIds : MutableMap<Coordinate, Int>? = null
    var plantsToWater : MutableLiveData<MutableMap<Coordinate, MyPlant>> = MutableLiveData()
    var columns = 0
    var rows = 0
    var order = 0

    fun setBed(bed: Bed){
        plantsToWater.value = null
        val map = ObservableArrayMap<Coordinate, MyPlant>()
        bed.plants.forEach {
            map[it.key] = it.value
        }

        name = bed.name
        season = bed.season
        bedLocation = bed.bedLocation
        plants = map
        tileIds = HashMap()
        columns = bed.columns
        rows = bed.rows
        order = bed.order
    }

    fun clear() {
        name = null
        bedLocation = null
        plants = null
        tileIds = null
        plantsToWater = MutableLiveData()
        columns = 0
        rows = 0
        order = 0
    }

    fun setPlantsToWater(date: Date?) {
        val map = HashMap<Coordinate, MyPlant>()
        val filteredPlants = plants?.filterValues(NeedsWaterPredicate(date))
        if (filteredPlants != null) {
            map.putAll(filteredPlants)
            plantsToWater.value = map
        }
    }
}