package dk.mifu.pmos.vegetablegardening.viewgarden

import android.content.Intent
import androidx.fragment.app.Fragment
import dk.mifu.pmos.vegetablegardening.creategarden.CreateBedActivity

abstract class GardenOverviewNavigation: Fragment() {
    protected fun navigateToCreateBedActivity() {
        val createIntent = Intent(context, CreateBedActivity::class.java)
        startActivity(createIntent)
    }

    protected fun navigateToBedOverviewFragment() {
        // TODO
    }
}