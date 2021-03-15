package dk.mifu.pmos.vegetablegardening.helpers.weather

import android.content.Context
import android.location.Location
import android.util.Log
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import dk.mifu.pmos.vegetablegardening.BuildConfig
import dk.mifu.pmos.vegetablegardening.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

abstract class WeatherData(private val context: Context) {
    protected abstract fun handleResponse(date: Date?)

    companion object {
        private const val PERIOD = "latest-month"
        private const val LIMIT = 10000
        private const val RAINED_MILLIMETERS_CUTOFF = 0.5
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
            val parameters = "parameterId=precip_past10min&period=${PERIOD}&limit=${LIMIT}&bbox=${boundaryBox}"
            val url = String.format(apiBaseUrl, parameters)

            val queue = Volley.newRequestQueue(context)
            val request = object : JsonObjectRequest(Method.GET, url, null,
                    { response ->
                        Log.d("getLastRained()", "Response received: $response")
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

    private fun jsonToLastRained(json: JSONObject) : Date? {
        Log.d("jsonToWeather()", "json input: $json")

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale("da", "DK"))

        val features = json.getJSONArray("features")
        for (i in 0 until features.length()) {
            val feature = features.getJSONObject(i)
            Log.d("feature", feature.toString())

            val properties = feature.getJSONObject("properties")
            if (properties.getDouble("value") >= RAINED_MILLIMETERS_CUTOFF)
                return dateFormat.parse(properties.getString("observed"))
        }
        return null
    }
}