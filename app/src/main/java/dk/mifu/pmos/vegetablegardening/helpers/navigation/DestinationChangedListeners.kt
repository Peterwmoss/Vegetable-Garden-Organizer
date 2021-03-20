package dk.mifu.pmos.vegetablegardening.helpers.navigation

import android.content.Context
import androidx.navigation.NavController
import dk.mifu.pmos.vegetablegardening.database.AppDatabase
import dk.mifu.pmos.vegetablegardening.database.GardenRepository
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DestinationChangedListeners {
    companion object {
        fun reloadBedFromDatabaseListener(bedViewModel: BedViewModel, context: Context): NavController.OnDestinationChangedListener {
            return NavController.OnDestinationChangedListener { controller, destination, arguments ->
                MainScope().launch(Dispatchers.Main) {
                    val def = async(Dispatchers.IO) {
                        val dao = AppDatabase.getDatabase(context).bedDao()
                        val repository = GardenRepository(dao)
                        return@async repository.findBed(bedViewModel.name!!)
                    }
                    bedViewModel.setBed(def.await())
                }
            }
        }
    }
}