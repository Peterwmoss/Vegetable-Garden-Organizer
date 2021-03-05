package dk.mifu.pmos.vegetablegardening.helpers.weather

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*

class WeatherDataLocationService : Service() {
    private var startMode: Int = 0             // indicates how to behave if the service is killed
    private var binder: IBinder? = null        // interface for clients that bind
    private var allowRebind: Boolean = true   // indicates whether onRebind should be used

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    companion object {
        private const val UPDATE_INTERVAL = 5000L
        private const val FASTEST_INTERVAL = 1000L
    }

    private fun sendLocation(location: Location) {
        Log.d("sendLocation()", location.toString())
        val intent = Intent("sendLocation")
        val bundle = Bundle()
        bundle.putParcelable("location", location)
        intent.putExtra("location", bundle)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    override fun onCreate() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    sendLocation(location)
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startLocationUpdates()
        return startMode
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return allowRebind
    }

    override fun onRebind(intent: Intent?) {
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun startLocationUpdates() {
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return

        val locationRequest = LocationRequest.create()
        locationRequest.apply {
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            interval = UPDATE_INTERVAL
            fastestInterval = FASTEST_INTERVAL
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }
}