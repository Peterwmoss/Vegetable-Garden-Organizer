package dk.mifu.pmos.vegetablegardening.fragments.creategarden

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel
import dk.mifu.pmos.vegetablegardening.enums.BedLocation
import dk.mifu.pmos.vegetablegardening.databinding.FragmentSpecifyLocationBinding

class SpecifyLocationFragment: Fragment() {
    private lateinit var binding: FragmentSpecifyLocationBinding

    private val bedViewModel: BedViewModel by activityViewModels()

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
        binding.outdoorsButton.setOnClickListener { startCreateGridFragment(BedLocation.Outdoors) }
        binding.greenhouseButton.setOnClickListener{ startCreateGridFragment(BedLocation.Greenhouse) }
    }

    private fun startCreateGridFragment(bedLocation: BedLocation){
        bedViewModel.bedLocation = bedLocation
        navigateToNextView()
    }

    private fun navigateToNextView() {
        requireView().findNavController().navigate(SpecifyLocationFragmentDirections.nextAction())
    }
}
