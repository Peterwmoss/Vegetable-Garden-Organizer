package dk.mifu.pmos.vegetablegardening.fragments.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.databinding.FragmentWeatherDataBinding
import dk.mifu.pmos.vegetablegardening.helpers.Formatter
import dk.mifu.pmos.vegetablegardening.viewmodels.LocationViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class WeatherDataFragment : Fragment() {
    private lateinit var binding: FragmentWeatherDataBinding

    private val locationViewModel: LocationViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWeatherDataBinding.inflate(inflater, container, false)

        val formatter = Formatter(requireContext())

        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context))

        binding.map.setTileSource(TileSourceFactory.MAPNIK)
        binding.map.isTilesScaledToDpi = true
        val controller = binding.map.controller

        if (locationViewModel.location == null) {
            controller.setZoom(8.0)
            controller.setCenter(GeoPoint(55.7,10.6))
        } else {
            val location = locationViewModel.location!!
            controller.setZoom(11.0)
            controller.setCenter(GeoPoint(location.latitude,location.longitude))
        }

        val locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), binding.map)
        locationOverlay.enableMyLocation()
        binding.map.overlays.add(locationOverlay)

        binding.lastRainedText.text = formatter.formatDate(locationViewModel.lastRained)

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.weather_data)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }
}