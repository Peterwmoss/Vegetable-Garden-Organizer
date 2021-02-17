package dk.mifu.pmos.vegetablegardening.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.models.Plant
import java.io.*
import java.util.*
import com.opencsv.CSVReader

class PlantViewModel(application: Application) : AndroidViewModel(application) {
    val plants: List<Plant> by lazy {
        loadPlants()
    }

    private fun loadPlants(): List<Plant> {
        val context: Context = getApplication()
        val stream = context.resources.openRawResource(R.raw.cabbage)
        val reader = CSVReader(InputStreamReader(stream))
        val data = reader.readAll()
        Log.e("Testplant", data[1][0].toString())

        val tomato = Plant("Tomato", Date(), Date(),true,"yeet", "yeet", "yeet",1,"yeet", "yeet")
        return listOf(tomato)
    }
}