package dk.mifu.pmos.vegetablegardening.fragments.lexicon

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
import dk.mifu.pmos.vegetablegardening.helpers.Formatter
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel
import dk.mifu.pmos.vegetablegardening.viewmodels.PlantViewModel
import dk.mifu.pmos.vegetablegardening.views.Tooltip
import java.text.SimpleDateFormat
import java.util.*

class PlantDetailsFragment: Fragment() {
    private val args: PlantDetailsFragmentArgs by navArgs()
    private val plantViewModel: PlantViewModel by activityViewModels()

    private lateinit var binding: FragmentPlantDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        plantViewModel.plants.observe(viewLifecycleOwner, {
            if (it.contains(args.plant))
                inflater.inflate(R.menu.toolbar_default, menu)
            else
                inflater.inflate(R.menu.toolbar_editable, menu)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.tooltip -> {
                Tooltip.newTooltip(requireContext(), getString(R.string.tooltip_plant_details_not_planted), requireView().rootView.findViewById(R.id.tooltip))
                true
            }
            R.id.edit -> {
                findNavController().navigate(PlantDetailsFragmentDirections.editPlant().setPlant(args.plant))
                true
            }
            R.id.delete -> {
                findNavController().navigate(PlantDetailsFragmentDirections.deletePlantAction(args.plant))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPlantDetailsBinding.inflate(inflater, container, false)

        val plant = args.plant
        val titles = plantViewModel.categoryTitles.value

        (activity as AppCompatActivity).supportActionBar?.title = plant.name

        binding.plantName.text = plant.name
        binding.gridlayout.columnCount = 2

        val formatter = Formatter(requireContext())

        addTextInfoLine(titles?.get(1), plant.category)
        addTextInfoLine(titles?.get(2), formatter.formatDate(plant.earliest))
        addTextInfoLine(titles?.get(3), formatter.formatDate(plant.latest))
        addTextInfoLine(titles?.get(4), formatter.formatSowingBoolean(plant.sowing))
        addTextInfoLine(titles?.get(5), plant.cropRotation)
        addTextInfoLine(titles?.get(6), plant.quantity)
        addTextInfoLine(titles?.get(7), plant.sowingDepth)
        addTextInfoLine(titles?.get(8), plant.distance.toString())
        addTextInfoLine(titles?.get(9), plant.fertilizer)
        addTextInfoLine(titles?.get(10), plant.harvest)

        return binding.root
    }

    private fun addTextInfoLine(categoryText: String?, dataText: String?) {
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
}