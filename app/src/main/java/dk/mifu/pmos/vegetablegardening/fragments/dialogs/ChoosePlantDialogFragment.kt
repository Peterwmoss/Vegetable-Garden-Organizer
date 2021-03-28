package dk.mifu.pmos.vegetablegardening.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.databinding.FragmentChoosePlantBinding
import dk.mifu.pmos.vegetablegardening.helpers.predicates.LocationPlantPredicate
import dk.mifu.pmos.vegetablegardening.helpers.recyclerviews.PlantAdapter
import dk.mifu.pmos.vegetablegardening.helpers.recyclerviews.PlantViewHolder
import dk.mifu.pmos.vegetablegardening.helpers.search.PlantFilter
import dk.mifu.pmos.vegetablegardening.models.MyPlant
import dk.mifu.pmos.vegetablegardening.models.Plant
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel
import dk.mifu.pmos.vegetablegardening.viewmodels.PlantViewModel

class ChoosePlantDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentChoosePlantBinding

    private val plantViewModel: PlantViewModel by activityViewModels()
    private val bedViewModel: BedViewModel by activityViewModels()

    private val args: ChoosePlantDialogFragmentArgs by navArgs()

    private var adapter : PlantAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentChoosePlantBinding.inflate(inflater, container, false)

        val recyclerView = binding.choosePlantRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        plantViewModel.plants.observe(viewLifecycleOwner, {
            val plants = it
                .filter(LocationPlantPredicate(bedViewModel.bedLocation))
                .filter(args.predicate)
            adapter = Adapter(plants)
            recyclerView.adapter = adapter

            PlantFilter.setupSearch(adapter, binding.searchPlantEdittext)
        })

        binding.clearPlantButton.setOnClickListener {
            bedViewModel.plants?.remove(args.coordinate)
            dismiss()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val params = dialog!!.window!!.attributes

        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.MATCH_PARENT

        dialog!!.window!!.attributes = params

        binding.searchPlantEdittext.requestFocus()
    }

    override fun onStop() {
        super.onStop()
        if (bedViewModel.name.isNullOrBlank()) {
            (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.toolbar_create_grid)
        } else {
            (activity as AppCompatActivity).supportActionBar?.title = bedViewModel.name
        }
    }

    private inner class ViewHolder(view: View) : PlantViewHolder(view) {
        init {
            view.setOnClickListener {
                bedViewModel.plants?.set(args.coordinate, MyPlant(plant.name))
                navigateBack()
            }
        }
    }

    private inner class Adapter(dataSet: List<Plant>) : PlantAdapter(dataSet) {
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(viewGroup.context) .inflate(R.layout.list_item_plant, viewGroup, false)
            return ViewHolder(view)
        }
    }

    private fun navigateBack() {
        dialog?.dismiss()
    }
}