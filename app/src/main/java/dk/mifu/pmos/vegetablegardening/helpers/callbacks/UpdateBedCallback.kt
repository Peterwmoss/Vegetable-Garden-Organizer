package dk.mifu.pmos.vegetablegardening.helpers.callbacks

import android.app.Application
import androidx.databinding.ObservableMap
import dk.mifu.pmos.vegetablegardening.database.AppDatabase
import dk.mifu.pmos.vegetablegardening.database.BedRepository
import dk.mifu.pmos.vegetablegardening.enums.BedLocation
import dk.mifu.pmos.vegetablegardening.models.Bed
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.MyPlant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UpdateBedCallback(
        private val name: String,
        private val season: Int,
        private val bedLocation: BedLocation,
        private val application: Application,
        private val columns: Int,
        private val rows: Int): BedPlantsChangedCallback() {
    override fun onMapChanged(sender: ObservableMap<Coordinate, MyPlant>?, key: Coordinate?) {
        GlobalScope.launch(Dispatchers.IO) {
            val dao = AppDatabase.getDatabase(application).bedDao()
            val repository = BedRepository(dao)
            repository.updateBed(Bed(name, season, bedLocation, sender!!, columns, rows))
        }
    }
}