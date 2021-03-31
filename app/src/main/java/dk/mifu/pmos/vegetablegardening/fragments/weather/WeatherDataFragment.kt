package dk.mifu.pmos.vegetablegardening.fragments.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import dk.mifu.pmos.vegetablegardening.databinding.FragmentWeatherDataBinding
import dk.mifu.pmos.vegetablegardening.helpers.Formatter
import dk.mifu.pmos.vegetablegardening.viewmodels.LocationViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint

class WeatherDataFragment : Fragment() {
    private lateinit var binding: FragmentWeatherDataBinding

    private val locationViewModel: LocationViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWeatherDataBinding.inflate(inflater, container, false)

        val formatter = Formatter(requireContext())

        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context))

        binding.map.setTileSource(TileSourceFactory.MAPNIK)
        val controller = binding.map.controller
        controller.setZoom(8.0)
        controller.setCenter(GeoPoint(55.7,10.6))

        binding.lastRainedText.text = formatter.formatDate(locationViewModel.lastRained)

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