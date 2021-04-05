// Heavily inspired by https://github.com/android/location-samples/blob/432d3b72b8c058f220416958b444274ddd186abd/LocationUpdatesForegroundService/

package dk.mifu.pmos.vegetablegardening.activities

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import dk.mifu.pmos.vegetablegardening.BuildConfig
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.database.AppDatabase
import dk.mifu.pmos.vegetablegardening.database.SeasonRepository
import dk.mifu.pmos.vegetablegardening.databinding.ActivityMainBinding
import dk.mifu.pmos.vegetablegardening.helpers.weather.LocationService
import dk.mifu.pmos.vegetablegardening.helpers.weather.LocationUtils
import dk.mifu.pmos.vegetablegardening.helpers.weather.WeatherData
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel
import dk.mifu.pmos.vegetablegardening.viewmodels.LocationViewModel
import dk.mifu.pmos.vegetablegardening.viewmodels.SeasonViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    private val seasonViewModel: SeasonViewModel by viewModels()
    private val locationViewModel: LocationViewModel by viewModels()

    // Location service elements
    private lateinit var weatherDataReceiver : WeatherDataReceiver
    private var service : LocationService? = null
    private var bound = false
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i(TAG, "Connected to service")
            val binder = service as LocationService.LocationBinder
            this@MainActivity.service = binder.getService()
            this@MainActivity.service?.requestLocationUpdates()
            bound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i(TAG, "Disconnected from service")
            service = null
            bound = false
        }
    }

    companion object {
        private val TAG = MainActivity::class.simpleName
        const val REQUEST_PERMISSIONS_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        weatherDataReceiver = WeatherDataReceiver()

        if (!LocationUtils.hasRequestedLocationPermissions(this)) {
            if (!checkPermissions()) {
                requestPermissions()
            }
        }

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        val drawerLayout = binding.drawerLayout
        navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(setOf(R.id.gardenOverviewFragment, R.id.lexiconFragment, R.id.newSeasonFragment, R.id.weatherDataFragment), drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)

        setUpSeasonsInDrawer()
    }

    override fun onStart() {
        super.onStart()
        bindService(Intent(this, LocationService::class.java), serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(weatherDataReceiver, IntentFilter(LocationService.ACTION_BROADCAST))
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(weatherDataReceiver)
        super.onPause()
    }

    override fun onStop() {
        if (bound) {
            unbindService(serviceConnection)
            bound = false
        }
        super.onStop()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun checkPermissions(): Boolean {
        return PackageManager.PERMISSION_GRANTED == checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun requestPermissions() {
        Log.i(TAG, "Displaying permission rationale to user")
        LocationUtils.setHasRequestedLocationPermissions(this, true)
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.location_request_title))
                .setMessage(R.string.location_permission_rationale)
                .setPositiveButton(R.string.yes) { _, _ ->
                    Log.i(TAG, "Requesting location permissions.")
                    requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSIONS_REQUEST_CODE)
                }
                .setNegativeButton(R.string.no) { _, _ ->
                    Log.i(TAG, "User denied locationdata")
                    createSettingsSnackBar().show()
                }
                .create()
                .show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.i(TAG, "onRequestedPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> {
                    Log.i(TAG, "Interaction was cancelled")
                }
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    service?.requestLocationUpdates()
                }
                else -> {
                    createSettingsSnackBar().show()
                }
            }
        }
    }

    private fun setUpSeasonsInDrawer(){
        val seasonsMenu = binding.navigationView.menu.findItem(R.id.gardenOverviewFragment).subMenu
        val dao = AppDatabase.getDatabase(this).seasonDao()
        val seasons = SeasonRepository(dao).getAllSeasons()

        seasons.observe(this, { list ->
            seasonsMenu.clear()

            list.sortedByDescending { it.season }.forEach {
                val item = seasonsMenu.add(it.season.toString())
                item.icon = ContextCompat.getDrawable(this, R.drawable.bed)
                item.setOnMenuItemClickListener {
                    seasonViewModel.currentSeason.value = item.title.toString().toInt()
                    findNavController(R.id.nav_host_fragment).navigate(R.id.gardenOverviewFragment)
                    return@setOnMenuItemClickListener false
                }
            }
            binding.navigationView.invalidate()
            setUpNavigation()
        })
    }

    private fun createSettingsSnackBar() : Snackbar {
        return Snackbar.make(
                binding.root,
                R.string.location_permission_denied,
                Snackbar.LENGTH_LONG)
                .setAction(R.string.settings) {
                    // Go to settings
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                    intent.data = uri
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
    }

    private fun setUpNavigation(){
        binding.navigationView.setupWithNavController(navController)
    }

    private inner class WeatherDataReceiver: BroadcastReceiver() {
        private val weatherData = object : WeatherData(applicationContext) {
            override fun handleResponse(date: Date?) {
                Log.d("handleResponse()", "date: $date")
                if (date != null) {
                    locationViewModel.lastRained.value = date
                }
            }
        }

        override fun onReceive(context: Context?, intent: Intent?) {
            Log.i(TAG, "Location intent received")
            val location = intent?.getParcelableExtra<Location>(LocationService.EXTRA_LOCATION)
            MainScope().launch {
                locationViewModel.location.value = location
                location?.let { weatherData.getLastRained(it) }
                this@MainActivity.service?.removeLocationUpdates()
            }
        }
    }
}
