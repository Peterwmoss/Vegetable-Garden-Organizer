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
        // TODO Fix so that it does this automatically in the constructor if no map is given
        val tileIds = HashMap<Coordinate, Int>()
        tileIds[Coordinate(0,0)] = R.id.tile1_image_view
        tileIds[Coordinate(1,0)] = R.id.tile2_image_view
        tileIds[Coordinate(0,1)] = R.id.tile3_image_view
        tileIds[Coordinate(1,1)] = R.id.tile4_image_view
        currentGardenViewModel.garden.value = Garden(location, null, HashMap(), tileIds)

        // Navigate to next view
        requireView().findNavController().navigate(SpecifyLocationFragmentDirections.nextAction())
    }
}
