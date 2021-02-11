package dk.mifu.pmos.vegetablegardening.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CurrentGardenViewModel : ViewModel() {
    val garden = MutableLiveData<Garden>()
}