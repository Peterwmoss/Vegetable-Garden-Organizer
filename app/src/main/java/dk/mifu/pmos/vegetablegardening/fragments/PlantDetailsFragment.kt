package dk.mifu.pmos.vegetablegardening.fragments

import android.os.Bundle
import android.view.*
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.databinding.FragmentPlantDetailsBinding
import dk.mifu.pmos.vegetablegardening.helpers.callbacks.UpdateSortInViewCallback
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel
import dk.mifu.pmos.vegetablegardening.viewmodels.PlantViewModel
import dk.mifu.pmos.vegetablegardening.views.Tooltip
import java.text.SimpleDateFormat
import java.util.*

class PlantDetailsFragment: Fragment() {
    private val args: PlantDetailsFragmentArgs by navArgs()
    private val plantViewModel: PlantViewModel by activityViewModels()
    private val bedViewModel: BedViewModel by activityViewModels()

    private lateinit var binding: FragmentPlantDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.tooltip -> {
                Tooltip.newTooltip(requireContext(), getString(R.string.guide_create_grid_text), requireView().rootView.findViewById(R.id.tooltip))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPlantDetailsBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).supportActionBar?.title = bedViewModel.name

        val myPlant = args.myplant
        val plant = args.plant
        val titles = plantViewModel.categoryTitles.value

        binding.plantName.text = plant.name

        binding.gridlayout.columnCount = 2
        addTextInfoLine(titles?.get(1), plant.category)
        addTextInfoLine(titles?.get(2), formatDate(plant.earliest))
        addTextInfoLine(titles?.get(3), formatDate(plant.latest))
        addTextInfoLine(titles?.get(4), formatSowingBoolean(plant.sowing))
        addTextInfoLine(titles?.get(5), plant.cropRotation)
        addTextInfoLine(titles?.get(6), plant.quantity)
        addTextInfoLine(titles?.get(7), plant.sowingDepth)
        addTextInfoLine(titles?.get(8), plant.distance.toString())
        addTextInfoLine(titles?.get(9), plant.fertilizer)
        addTextInfoLine(titles?.get(10), plant.harvest)
        if (myPlant != null) {
            addTextInfoLine(getString(R.string.seasons_text), myPlant.seasons.toString())
            addTextInfoLine(getString(R.string.last_watered_text), formatDate(myPlant.wateredDate))
            addTextInfoLine(getString(R.string.harvested_text), formatDate(myPlant.harvestedDate))

            val editSortButton = binding.editSortButton

            editSortButton.visibility = View.VISIBLE

            if (myPlant.sort.isBlank()) {
                editSortButton.text = getString(R.string.add_sort_text)
                bedViewModel.plants?.addOnMapChangedCallback(UpdateSortInViewCallback(args.coordinate!!,getString(R.string.sort), myPlant.sort, ::addTextInfoLine, binding.editSortButton))
            } else {
                editSortButton.text = getString(R.string.edit_sort_text)
                bedViewModel.plants?.addOnMapChangedCallback(UpdateSortInViewCallback(args.coordinate!!, addTextInfoLine(getString(R.string.sort), myPlant.sort), binding.editSortButton))
            }

            binding.editSortButton.setOnClickListener {
                findNavController().navigate(PlantDetailsFragmentDirections.editSort(myPlant, args.coordinate!!))
            }
        }

        return binding.root
    }

    private fun addTextInfoLine(categoryText: String?, dataText: String?): TextView {
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

        return data
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