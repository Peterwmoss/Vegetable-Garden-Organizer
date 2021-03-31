package dk.mifu.pmos.vegetablegardening.helpers.weather

import android.content.Context
import android.location.Location
import androidx.preference.PreferenceManager
import dk.mifu.pmos.vegetablegardening.R

// Lots of inspiration taken from https://github.com/android/location-samples/blob/432d3b72b8c058f220416958b444274ddd186abd/LocationUpdatesForegroundService/

class LocationUtils {
    companion object {
        private const val KEY_REQUESTING_LOCATION_UPDATES = "requesting_location_updates"

        fun requestingLocationUpdates(context: Context): Boolean {
            return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false)
        }

        fun setRequestingLocationUpdates(context: Context, requestingLocationUpdates: Boolean) {
            PreferenceManager.getDefaultSharedPreferences(context)
                    .edit()
                    .putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates)
                    .apply()
        }

        fun getLocationText(location: Location?): String {
            return if (location == null) "Unknown Location" else "(${location.latitude}, ${location.longitude})"
        }

        fun getLocationTitle(context: Context): String {
            return context.getString(R.string.location_updated)
        }
    }
}