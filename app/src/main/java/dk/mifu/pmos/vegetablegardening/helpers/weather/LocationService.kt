// Heavily inspired by https://github.com/android/location-samples/blob/432d3b72b8c058f220416958b444274ddd186abd/LocationUpdatesForegroundService/

package dk.mifu.pmos.vegetablegardening.helpers.weather

import android.app.*
import android.content.Intent
import android.content.res.Configuration
import android.location.Location
import android.os.*
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*

class LocationService : Service() {
    companion object {
        private const val PACKAGE_NAME = "dk.mifu.pmos.vegetablegardening"
        private val TAG = LocationService::class.simpleName

        const val ACTION_BROADCAST = "$PACKAGE_NAME.broadcast"
        const val EXTRA_LOCATION = "$PACKAGE_NAME.location"

        private const val UPDATE_INTERVAL = 20000L
        private const val FASTEST_INTERVAL = 10000L
    }

    private val binder: IBinder = LocationBinder()

    private var changingConfiguration = false

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

    private var location: Location? = null

    inner class LocationBinder: Binder() {
        fun getService(): LocationService = this@LocationService
    }

    override fun onCreate() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                onNewLocation(locationResult.lastLocation)
            }
        }

        createLocationRequest()
        getLastLocation()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "Service started")
        return START_NOT_STICKY
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        changingConfiguration = true
    }

    // Service is bound, so should no longer be in foreground
    override fun onBind(intent: Intent?): IBinder {
        Log.i(TAG, "in onBind()")
        changingConfiguration = false

        return binder
    }

    override fun onRebind(intent: Intent?) {
        Log.i(TAG, "in onRebind()")
        changingConfiguration = false
        super.onRebind(intent)
    }

    // Service no longer bound, so should be in foreground
    override fun onUnbind(intent: Intent?): Boolean {
        Log.i(TAG, "Last client unbound from service")
        return true
    }

    fun requestLocationUpdates() {
        Log.i(TAG, "Requesting location updates")
        startService(Intent(applicationContext, LocationService::class.java))
        try {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper()!!)
        } catch (unlikely: SecurityException) {
            Log.e(TAG, "No permissions. $unlikely")
        }
    }

    fun removeLocationUpdates() {
        Log.i(TAG, "Removing location updates")
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        stopSelf()
    }

    private fun getLastLocation() {
        try {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    location = task.result
                } else {
                    Log.w(TAG, "Failed to get location.")
                }
            }
        } catch (unlikely: SecurityException) {
            Log.e(TAG, "No permissions. $unlikely")
        }
    }

    private fun onNewLocation(location: Location) {
        Log.i(TAG, "New location: $location")

        this.location = location

        val intent = Intent(ACTION_BROADCAST)
        intent.putExtra(EXTRA_LOCATION, location)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create()
        locationRequest.apply {
            interval = UPDATE_INTERVAL
            fastestInterval = FASTEST_INTERVAL
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }
}