package dk.mifu.pmos.vegetablegardening.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReader
import com.opencsv.CSVReaderBuilder
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.database.AppDatabase
import dk.mifu.pmos.vegetablegardening.database.PlantRepository
import dk.mifu.pmos.vegetablegardening.models.Plant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*

class PlantViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        suspend fun getUserPlants(context: Context): LiveData<MutableList<Plant>> {
            return withContext(Dispatchers.IO) {
                val dao = AppDatabase.getDatabase(context).plantDao()
                val repository = PlantRepository(dao)
                return@withContext repository.getAllPlants()
            }
        }
    }

    val categoryTitles: LiveData<List<String>> by lazy {
        loadCategoryTitles()
    }
    val plants: LiveData<List<Plant>> by lazy {
        loadPlants()
    }

    private fun loadPlants(): LiveData<List<Plant>> {
        val data = createReader(1)?.readAll()

        val plants: MutableLiveData<List<Plant>> = MutableLiveData()
        val list: MutableList<Plant> = mutableListOf()

        data?.forEach { plant ->
            list.add(
                    Plant(
                            name = plant[0],
                            category = plant[1],
                            earliest = toDate(plant[2]),
                            latest = toDate(plant[3]),
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

        plants.value = list

        return plants
    }

    private fun loadCategoryTitles(): LiveData<List<String>> {
        val data = createReader(0)?.readNext()

        val list: MutableList<String> = mutableListOf()

        data?.forEach { list.add(it) }

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

    private fun toDate(dateString: String?): Date? {
        val format = SimpleDateFormat("d. MMMM", Locale("da", "DK"))
        return if (dateString != null && dateString.isNotBlank()){
            val c = Calendar.getInstance()
            val date = format.parse(dateString)
            val year = c.get(Calendar.YEAR)
            c.time = date!!
            c.set(Calendar.YEAR, year)
            c.time
        } else {
            null
        }
    }
}