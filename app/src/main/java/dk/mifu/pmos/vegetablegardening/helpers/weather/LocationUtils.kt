package dk.mifu.pmos.vegetablegardening.helpers.weather

import android.content.Context
import androidx.preference.PreferenceManager

class LocationUtils {
    companion object {

        private const val KEY_REQUESTED_LOCATION_PERMISSIONS = "requested_location_permissions"

        fun hasRequestedLocationPermissions(context: Context): Boolean {
            return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(KEY_REQUESTED_LOCATION_PERMISSIONS, false)
        }

        fun setHasRequestedLocationPermissions(context: Context, hasRequested: Boolean) {
            PreferenceManager.getDefaultSharedPreferences(context)
                    .edit()
                    .putBoolean(KEY_REQUESTED_LOCATION_PERMISSIONS, hasRequested)
                    .apply()
        }
    }
}