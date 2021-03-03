package dk.mifu.pmos.vegetablegardening.helpers

import android.content.Context
import android.location.Location
import android.util.Log
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import dk.mifu.pmos.vegetablegardening.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

abstract class Weather(private val context: Context) {
    protected abstract fun handleResponse(json: JSONObject)

    companion object {
        private const val PERIOD = "latest-week"
        private const val LIMIT = 10000
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

            val url = "https://dmigw.govcloud.dk/v2/metObs/collections/observation/items?parameterId=precip_past10min&period=${PERIOD}&limit=${LIMIT}&bbox=${boundaryBox}"

            val queue = Volley.newRequestQueue(context)
            val request = object : JsonObjectRequest(Method.GET, url, null,
                    { response ->
                        Log.d("getLastRained()", "Response received: ${response.toString().subSequence(0,100)}")
                        handleResponse(response)
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
}