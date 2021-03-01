package dk.mifu.pmos.vegetablegardening.helpers

import android.content.Context
import android.util.Log
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import dk.mifu.pmos.vegetablegardening.BuildConfig

class Weather {
    companion object {
        fun getWeatherData(context: Context) {
            val queue = Volley.newRequestQueue(context)
            val url = "https://dmigw.govcloud.dk/v2/metObs"

            val request = object : StringRequest(Method.GET, url,
                    { response ->
                        Log.e("Shit", response.toString())
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