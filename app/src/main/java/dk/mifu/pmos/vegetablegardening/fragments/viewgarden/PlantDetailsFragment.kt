package dk.mifu.pmos.vegetablegardening.fragments.viewgarden

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.databinding.FragmentPlantDetailsBinding
import dk.mifu.pmos.vegetablegardening.viewmodels.PlantViewModel
import java.text.SimpleDateFormat
import java.util.*

class PlantDetailsFragment: Fragment() {
    private val args: PlantDetailsFragmentArgs by navArgs()
    private val plantViewModel: PlantViewModel by activityViewModels()

    private lateinit var binding: FragmentPlantDetailsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPlantDetailsBinding.inflate(inflater, container, false)

        val myPlant = args.plant
        val plant = plantViewModel.plants.value?.first { plant -> plant.name == myPlant.name }
        val titles = plantViewModel.categoryTitles.value

        binding.plantName.text = myPlant.name

        binding.gridlayout.columnCount = 2
        addTextInfoLine(titles?.get(1), plant?.category)
        addTextInfoLine(titles?.get(2), formatDate(plant?.earliest))
        addTextInfoLine(titles?.get(3), formatDate(plant?.latest))
        addTextInfoLine(titles?.get(4), formatSowingBoolean(plant?.sowing))
        addTextInfoLine(titles?.get(5), plant?.cropRotation)
        addTextInfoLine(titles?.get(6), plant?.sowingDepth)
        addTextInfoLine(titles?.get(7), plant?.distance.toString())
        addTextInfoLine(titles?.get(8), plant?.quantity)
        addTextInfoLine(titles?.get(9), plant?.fertilizer)
        addTextInfoLine(titles?.get(10), plant?.harvest)
        addTextInfoLine(getString(R.string.planted_date_text), formatDate(myPlant.plantedDate))
        addTextInfoLine(getString(R.string.last_watered_text), formatDate(myPlant.wateredDate))
        addTextInfoLine(getString(R.string.harvested_text), formatDate(myPlant.harvestedDate))

        return binding.root
    }

    private fun addTextInfoLine(categoryText: String?, dataText: String?){
        val categoryParams = GridLayout.LayoutParams()
        categoryParams.setMargins(0,0, resources.getDimension(R.dimen.spacing_small).toInt(),0)

        val category = TextView(context)
        val data = TextView(context)

        category.run {
            text = categoryText
            layoutParams = categoryParams
        }

        data.text = dataText

        binding.gridlayout.run {
            addView(category)
            addView(data)
        }
    }

    private fun formatDate(date: Date?): String {
        val pattern = "dd. MMMM"
        val simpleDateFormat = SimpleDateFormat(pattern, Locale("da", "DK"))
        return if(date != null) simpleDateFormat.format(date)
        else getString(R.string.missing_info)
    }

    private fun formatSowingBoolean(sowing: Boolean?): String{
        return if( sowing != null){
            if(sowing) getString(R.string.sow)
            else getString(R.string.plant)
        } else {
            getString(R.string.missing_info)
        }
    }
}