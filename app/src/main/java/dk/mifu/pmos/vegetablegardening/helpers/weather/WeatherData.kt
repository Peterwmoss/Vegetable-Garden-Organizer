package dk.mifu.pmos.vegetablegardening.helpers.weather

import android.content.Context
import android.location.Location
import android.util.Log
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import dk.mifu.pmos.vegetablegardening.BuildConfig
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.models.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

abstract class WeatherData(private val context: Context) {
    protected abstract fun handleResponse(weather: Weather?)

    companion object {
        private const val PERIOD = "latest-month"
        private const val LIMIT = 1000
        private const val RAINED_MILLIMETERS_CUTOFF = 2

        private val TAG = WeatherData::class.simpleName
    }

    suspend fun getLastRained(location: Location) {
        withContext(Dispatchers.IO) {
            val lon = location.longitude
            val lat = location.latitude

            val minLon = lon - 0.4
            val maxLon = lon + 0.4
            val minLat = lat - 0.4
            val maxLat = lat + 0.4
            val boundaryBox = "${minLon},${minLat},${maxLon},${maxLat}"

            val apiBaseUrl = context.resources.getString(R.string.weather_api_url)
            val parameters = "parameterId=precip_past1h&period=${PERIOD}&limit=${LIMIT}&bbox=${boundaryBox}"
            val url = String.format(apiBaseUrl, parameters)

            val queue = Volley.newRequestQueue(context)
            val request = object : JsonObjectRequest(Method.GET, url, null,
                    { response ->
                        Log.d(TAG, "json response: $response")
                        handleResponse(jsonToLastRained(response))
                    },
                    { error -> Log.e("getLastRained()", error.toString()) }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val map = mutableMapOf<String, String>()
                    map.putAll(super.getHeaders())
                    map["X-Gravitee-Api-Key"] = BuildConfig.WEATHER_KEY
                    return map
                }
            }
            queue.add(request)
        }
    }

    private fun jsonToLastRained(json: JSONObject) : Weather? {
        val map = TreeMap<Int, Double>() // Day in year to MM rained

        val features = json.getJSONArray("features")
        val first = features.getJSONObject(0)
        val station = getStationIdFromFeature(first)
        map[getDateFromFeature(first)] = getRainedMMFromFeature(first)

        for (i in 1 until features.length()) {
            val feature = features.getJSONObject(i)
            if (getStationIdFromFeature(feature) == station) {
                val date = getDateFromFeature(feature)
                if (map[date] != null) {
                    map[date] = map[date]!! + getRainedMMFromFeature(feature)
                } else {
                    map[date] = getRainedMMFromFeature(feature)
                }
            }
        }

        val sorted = map.toSortedMap(compareByDescending { it })

        sorted.entries.forEach {
            if (it.value >= RAINED_MILLIMETERS_CUTOFF) {
                val cal = Calendar.getInstance()
                cal.set(Calendar.DAY_OF_YEAR, it.key)
                return Weather(cal.time, it.value)
            }
        }

        return null
    }

    private fun getRainedMMFromFeature(feature: JSONObject): Double {
        val properties = feature.getJSONObject("properties")
        return properties.getDouble("value")
    }

    private fun getDateFromFeature(jsonObj: JSONObject): Int {
        val properties = jsonObj.getJSONObject("properties")
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale("da", "DK"))
        val date = dateFormat.parse(properties.getString("observed"))!!
        val cal = Calendar.getInstance()
        cal.time = date
        return cal.get(Calendar.DAY_OF_YEAR)
    }

    private fun getStationIdFromFeature(jsonObj: JSONObject): Int {
        val properties = jsonObj.getJSONObject("properties")
        return properties.getInt("stationId")
    }
}