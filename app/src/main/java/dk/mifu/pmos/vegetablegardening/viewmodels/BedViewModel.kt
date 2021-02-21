package dk.mifu.pmos.vegetablegardening.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dk.mifu.pmos.vegetablegardening.models.Bed

class BedViewModel : ViewModel() {
    val garden = MutableLiveData<Bed>()
}