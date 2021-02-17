package dk.mifu.pmos.vegetablegardening.creategarden

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import dk.mifu.pmos.vegetablegardening.viewmodels.CurrentGardenViewModel
import dk.mifu.pmos.vegetablegardening.models.Garden
import dk.mifu.pmos.vegetablegardening.enums.Location
import dk.mifu.pmos.vegetablegardening.databinding.FragmentSpecifyLocationBinding

class SpecifyLocationFragment: Fragment() {
    private lateinit var binding: FragmentSpecifyLocationBinding

    private val currentGardenViewModel: CurrentGardenViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSpecifyLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.outdoorsButton.setOnClickListener { startCreateGridFragment(Location.Outdoors) }
        binding.indoorsButton.setOnClickListener { startCreateGridFragment(Location.Indoors) }
        binding.greenhouseButton.setOnClickListener{ startCreateGridFragment(Location.Greenhouse) }
    }

    private fun startCreateGridFragment(location: Location){
        currentGardenViewModel.garden.value = Garden(location, "Test")
      
        // Navigate to next view
        requireView().findNavController().navigate(SpecifyLocationFragmentDirections.nextAction())
    }
}
