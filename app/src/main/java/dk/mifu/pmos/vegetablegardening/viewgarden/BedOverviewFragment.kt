package dk.mifu.pmos.vegetablegardening.viewgarden

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dk.mifu.pmos.vegetablegardening.databinding.FragmentBedOverviewBinding

class BedOverviewFragment: Fragment() {
    private lateinit var binding: FragmentBedOverviewBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentBedOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }
}