package dk.mifu.pmos.vegetablegardening.fragments.weather

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.activities.MainActivity
import dk.mifu.pmos.vegetablegardening.databinding.FragmentWeatherDataBinding
import dk.mifu.pmos.vegetablegardening.helpers.Formatter
import dk.mifu.pmos.vegetablegardening.viewmodels.LocationViewModel
import dk.mifu.pmos.vegetablegardening.views.Tooltip
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.OverlayItem

class WeatherDataFragment : Fragment() {
    private lateinit var binding: FragmentWeatherDataBinding

    private val locationViewModel: LocationViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_default, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.tooltip -> {
                Tooltip.newTooltip(requireContext(), getString(R.string.tooltip_weather_data), requireView().rootView.findViewById(R.id.tooltip))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWeatherDataBinding.inflate(inflater, container, false)

        setupPermissionButton()
        setupMap()
        setupLastRainedText()

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

    private fun setupPermissionButton() {
        if (PackageManager.PERMISSION_GRANTED != requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            binding.turnOnLocationPermissions.visibility = View.VISIBLE
            binding.turnOnLocationPermissions.setOnClickListener {
                requireActivity().requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MainActivity.REQUEST_PERMISSIONS_REQUEST_CODE)
            }
        }
    }

    private fun setupMap() {
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context))

        binding.map.setTileSource(TileSourceFactory.MAPNIK)
        binding.map.isTilesScaledToDpi = true
        val controller = binding.map.controller

        controller.setZoom(6.0)
        controller.setCenter(GeoPoint(55.7,10.6))

        locationViewModel.location.observe(viewLifecycleOwner, {
            controller.zoomTo(11.0)
            val location = GeoPoint(it.latitude, it.longitude)
            controller.animateTo(location)

            val locationItem = OverlayItem(getString(R.string.your_location), getString(R.string.you_are_here), location)
            locationItem.setMarker(ContextCompat.getDrawable(requireContext(), R.drawable.location))
            val locationOverlay = ItemizedIconOverlay(context, mutableListOf(locationItem), object : ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
                override fun onItemLongPress(index: Int, item: OverlayItem?): Boolean { return false }
                override fun onItemSingleTapUp(index: Int, item: OverlayItem?): Boolean { return true }
            })
            binding.map.overlays.add(locationOverlay)

        })
    }

    private fun setupLastRainedText() {
        val formatter = Formatter(requireContext())
        locationViewModel.weather.observe(viewLifecycleOwner, {
            if (it.date != null) {
                binding.lastRainedText.text = String.format(getString(R.string.last_rained_text, formatter.formatDate(it.date), formatter.formatRain(it.mm)))
            }
        })
    }
}