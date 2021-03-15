package dk.mifu.pmos.vegetablegardening.helpers.callbacks

import androidx.databinding.ObservableMap
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.MyPlant

abstract class BedPlantsChangedCallback: ObservableMap.OnMapChangedCallback<ObservableMap<Coordinate, MyPlant>, Coordinate, MyPlant>()