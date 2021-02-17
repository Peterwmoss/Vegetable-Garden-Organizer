package dk.mifu.pmos.vegetablegardening.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dk.mifu.pmos.vegetablegardening.models.Garden

class CurrentGardenViewModel : ViewModel() {
    val garden = MutableLiveData<Garden>()
}