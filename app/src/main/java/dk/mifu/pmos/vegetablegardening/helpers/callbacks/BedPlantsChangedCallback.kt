package dk.mifu.pmos.vegetablegardening.helpers.callbacks

import androidx.databinding.ObservableMap
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.Plant

abstract class BedPlantsChangedCallback: ObservableMap.OnMapChangedCallback<ObservableMap<Coordinate, Plant>, Coordinate, Plant>() {
}