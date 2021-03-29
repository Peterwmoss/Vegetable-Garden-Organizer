// Heavily inspired by https://github.com/android/location-samples/blob/432d3b72b8c058f220416958b444274ddd186abd/LocationUpdatesForegroundService/

package dk.mifu.pmos.vegetablegardening.helpers.weather

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.location.Location
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*
import dk.mifu.pmos.vegetablegardening.R

class LocationService : Service() {
    companion object {
        private const val PACKAGE_NAME = "dk.mifu.pmos.vegetablegardening"
        private val TAG = LocationService::class.simpleName

        // Channel for notifications
        private const val NOTIFICATION_ID = 12345678
        private const val CHANNEL_ID = "channel_01"
        const val ACTION_BROADCAST = "$PACKAGE_NAME.broadcast"
        const val EXTRA_LOCATION = "$PACKAGE_NAME.location"
        private const val EXTRA_STARTED_FROM_NOTIFICATION = "$PACKAGE_NAME.started_from_notification"

        private const val UPDATE_INTERVAL = 5000L
        private const val FASTEST_INTERVAL = 1000L
    }

    private val binder: IBinder = LocationBinder()

    private var changingConfiguration = false
    private lateinit var notificationManager: NotificationManager

    private lateinit var serviceHandler: Handler
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

        val handlerThread = HandlerThread(TAG)
        handlerThread.start()
        serviceHandler = Handler(handlerThread.looper)
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val channel = NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "Service started")
        val startedFromNotification = intent?.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION, false) ?: false

        // We got here because the user decided to remove location updates from the notification
        if (startedFromNotification) {
            removeLocationUpdates()
            stopSelf()
        }
        return START_NOT_STICKY
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        changingConfiguration = true
    }

    // Service is bound, so should no longer be in foreground
    override fun onBind(intent: Intent?): IBinder {
        Log.i(TAG, "in onBind()")
        stopForeground(true)
        changingConfiguration = false

        return binder
    }

    override fun onRebind(intent: Intent?) {
        Log.i(TAG, "in onRebind()")
        stopForeground(true)
        changingConfiguration = false
        super.onRebind(intent)
    }

    // Service no longer bound, so should be in foreground
    override fun onUnbind(intent: Intent?): Boolean {
        Log.i(TAG, "Last client unbound from service")

        if (!changingConfiguration && LocationUtils.requestingLocationUpdates(this)) {
            Log.i(TAG, "Starting foreground service")
            startForeground(NOTIFICATION_ID, getNotification())
        }
        return true
    }

    override fun onDestroy() {
        serviceHandler.removeCallbacksAndMessages(null)
    }

    fun requestLocationUpdates() {
        Log.i(TAG, "Requesting location updates")
        LocationUtils.setRequestingLocationUpdates(this, true)
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
        LocationUtils.setRequestingLocationUpdates(this, false)
        stopSelf()
    }

    // TODO Figure out if the notification part is needed
    private fun getNotification(): Notification {
        val intent = Intent(this, LocationService::class.java)
        val text = LocationUtils.getLocationText(location)

        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true)

        val servicePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val activityPendingIntent = PendingIntent.getActivity(this, 0, Intent(this, LocationService::class.java), 0)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .addAction(R.drawable.ic_flower, "Start activity", activityPendingIntent)
            .addAction(R.drawable.ic_delete, "Remove location updates", servicePendingIntent)
            .setContentText(text)
            .setContentTitle(LocationUtils.getLocationTitle(this))
            .setOngoing(true)

        return builder.build()
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

        if (serviceIsRunningInForeground(this))
            notificationManager.notify(NOTIFICATION_ID, getNotification())
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create()
        locationRequest.apply {
            interval = UPDATE_INTERVAL
            fastestInterval = FASTEST_INTERVAL
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun serviceIsRunningInForeground(context: Context) : Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (javaClass.name == service.service.className) {
                if (service.foreground)
                    return true
            }
        }
        return false
    }
}