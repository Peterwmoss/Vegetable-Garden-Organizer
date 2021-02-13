package dk.mifu.pmos.vegetablegardening.creategarden

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.data.Coordinate
import dk.mifu.pmos.vegetablegardening.data.CurrentGardenViewModel
import dk.mifu.pmos.vegetablegardening.data.Garden
import dk.mifu.pmos.vegetablegardening.data.Location
import kotlinx.android.synthetic.main.fragment_specify_location.*

class SpecifyLocationFragment: Fragment() {
    private val currentGardenViewModel: CurrentGardenViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_specify_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        outdoors_button.setOnClickListener { startCreateGridFragment(Location.Outdoors) }
        indoors_button.setOnClickListener { startCreateGridFragment(Location.Indoors) }
        greenhouse_button.setOnClickListener{ startCreateGridFragment(Location.Greenhouse) }
    }

    private fun startCreateGridFragment(location: Location){
        currentGardenViewModel.garden.value = Garden(location)
      
        // Navigate to next view
        requireView().findNavController().navigate(SpecifyLocationFragmentDirections.nextAction())
    }
}
