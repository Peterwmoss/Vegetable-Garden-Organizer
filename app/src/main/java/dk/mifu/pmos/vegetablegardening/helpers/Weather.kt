package dk.mifu.pmos.vegetablegardening.helpers

import android.content.Context
import android.util.Log
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import dk.mifu.pmos.vegetablegardening.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.*

abstract class Weather(protected val date: Date, private val context: Context) {
    protected abstract fun handleResponse(json: JSONObject)

    suspend fun getLastRained() {
        withContext(Dispatchers.IO) {
            val queue = Volley.newRequestQueue(context)
            val url = "https://dmigw.govcloud.dk/v2/metObs/collections/observation/items?stationId=06187&datetime=2020-01-01T00:00:00Z/2021-02-01T23:59:59Z"

            val request = object : JsonObjectRequest(Method.GET, url, null,
                    { response ->
                        handleResponse(response)
                    },
                    { error -> Log.e("Lort", error.toString()) }) {
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