package dk.mifu.pmos.vegetablegardening.creategarden

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.mifu.pmos.vegetablegardening.data.Location
import dk.mifu.pmos.vegetablegardening.databinding.FragmentCreateGridBinding

class CreateGridFragment(private val location: Location) : Fragment() {
    private lateinit var binding: FragmentCreateGridBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentCreateGridBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.locationTextView.text = location.toString()
    }
}
