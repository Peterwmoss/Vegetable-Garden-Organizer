package dk.mifu.pmos.vegetablegardening.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.opencsv.CSVParserBuilder
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.models.Plant
import java.io.*
import java.util.*
import com.opencsv.CSVReader
import com.opencsv.CSVReaderBuilder

class PlantViewModel(application: Application) : AndroidViewModel(application) {
    val plants: LiveData<MutableList<Plant>> by lazy {
        loadPlants()
    }

    private fun loadPlants(): LiveData<MutableList<Plant>> {
        val context: Context = getApplication()

        val stream = context.resources.openRawResource(R.raw.plantdata)
        val parser = CSVParserBuilder().withSeparator(';').build()
        val reader = CSVReaderBuilder(InputStreamReader(stream)).withCSVParser(parser).withSkipLines(1).build()
        val data = reader.readAll()

        val plants: MutableLiveData<MutableList<Plant>> = MutableLiveData()
        plants.value = mutableListOf()

        data.forEach { plant ->
            plants.value?.add(
                Plant(
                    name = plant[0],
                    category = plant[1],
                    earliest = plant[2],
                    latest = plant[3],
                    sowing = plant[4]=="såning",
                    cropRotation = plant[5],
                    quantity = plant[6],
                    sowingDepth = plant[7],
                    distance = plant[8].toIntOrNull(),
                    fertilizer = plant[9],
                    harvest = plant[10]
                )
            )
        }

        return plants
    }
}