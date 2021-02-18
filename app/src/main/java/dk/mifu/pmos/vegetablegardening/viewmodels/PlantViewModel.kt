package dk.mifu.pmos.vegetablegardening.viewmodels

import android.app.Application
import android.content.Context
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

        val stream = context.resources.openRawResource(R.raw.plantdata)
        val reader = CSVReader(InputStreamReader(stream))
        val data = reader.readAll()

        val plants = mutableListOf<Plant>()
        for (i in 1 until data.size){ //Data starts at second line, hence loop start at 1
            val plantDataList = data[i][0].split(";")
            plants.add(Plant(plantDataList[0]))
        }

        return plants
    }
}