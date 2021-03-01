package dk.mifu.pmos.vegetablegardening.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReader
import com.opencsv.CSVReaderBuilder
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.models.Plant
import java.io.*

class PlantViewModel(application: Application) : AndroidViewModel(application) {
    val categoryTitles: LiveData<List<String>> by lazy {
        loadCategoryTitles()
    }
    val plants: LiveData<MutableList<Plant>> by lazy {
        loadPlants()
    }

    private fun loadPlants(): LiveData<MutableList<Plant>> {
        val data = createReader(1)?.readAll()

        val plants: MutableLiveData<MutableList<Plant>> = MutableLiveData()
        plants.value = mutableListOf()

        data?.forEach { plant ->
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

    private fun loadCategoryTitles(): LiveData<List<String>> {
        val data = createReader(0)?.readNext()

        val list: MutableList<String> = mutableListOf()

        data?.forEach {
            list.add(it)
        }

        val categories: MutableLiveData<List<String>> = MutableLiveData()
        categories.value = list

        return categories
    }

    private fun createReader(skipLines: Int): CSVReader? {
        val context: Context = getApplication()
        val stream = context.resources.openRawResource(R.raw.plantdata)
        val parser = CSVParserBuilder().withSeparator(';').build()
        return CSVReaderBuilder(InputStreamReader(stream)).withCSVParser(parser).withSkipLines(skipLines).build()

    }
}