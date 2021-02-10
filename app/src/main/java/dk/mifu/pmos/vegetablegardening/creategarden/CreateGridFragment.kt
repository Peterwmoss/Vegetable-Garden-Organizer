package dk.mifu.pmos.vegetablegardening.creategarden

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.mifu.pmos.vegetablegardening.R
import kotlinx.android.synthetic.main.fragment_create_grid.*

class CreateGridFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_create_grid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        insert_plant_btn.setOnClickListener {
            // TODO update when GridTiles starts fragment
            val choosePlantFragment = ChoosePlantFragment(Pair(0,0))
            fragmentManager?.beginTransaction()?.add(R.id.fragment_container, choosePlantFragment)?.commit()
        }
    }
}
