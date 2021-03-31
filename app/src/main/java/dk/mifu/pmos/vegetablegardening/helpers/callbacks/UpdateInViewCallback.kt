package dk.mifu.pmos.vegetablegardening.helpers.callbacks

import androidx.databinding.ObservableMap
import dk.mifu.pmos.vegetablegardening.databinding.FragmentPlantDetailsDialogBinding
import dk.mifu.pmos.vegetablegardening.helpers.Formatter
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.MyPlant

class UpdateInViewCallback(
        private val coordinate: Coordinate,
        private val binding: FragmentPlantDetailsDialogBinding,
        private val formatter: Formatter
    ) : BedPlantsChangedCallback() {

    override fun onMapChanged(sender: ObservableMap<Coordinate, MyPlant>?, key: Coordinate?) {
        if (key != coordinate) return

        val plant = sender!![key]!!

        binding.sortText.text = formatter.formatString(plant.sort)
        binding.germinatedText.text = formatter.formatDate(plant.germinated)
        binding.plantedText.text = formatter.formatDate(plant.plantedDate)
        binding.wateredText.text = formatter.formatDate(plant.wateredDate)
        binding.harvestedText.text = formatter.formatDate(plant.harvestedDate)
        binding.notesText.text = formatter.formatString(plant.notes)
    }
}