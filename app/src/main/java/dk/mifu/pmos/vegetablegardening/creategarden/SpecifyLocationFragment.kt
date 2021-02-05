package dk.mifu.pmos.vegetablegardening.creategarden

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.data.Location
import kotlinx.android.synthetic.main.fragment_specify_location.*

class SpecifyLocationFragment: Fragment() {
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
        val createGridFragment = CreateGridFragment(location)
        fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, createGridFragment)?.commit()
    }
}
