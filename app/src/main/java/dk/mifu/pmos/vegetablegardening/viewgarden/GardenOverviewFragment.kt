package dk.mifu.pmos.vegetablegardening.viewgarden

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.creategarden.CreateBedActivity
import dk.mifu.pmos.vegetablegardening.databinding.FragmentGardenOverviewBinding
import dk.mifu.pmos.vegetablegardening.enums.Location.*
import dk.mifu.pmos.vegetablegardening.models.Bed
import dk.mifu.pmos.vegetablegardening.viewmodels.GardenViewModel
import dk.mifu.pmos.vegetablegardening.viewmodels.PlantViewModel
import dk.mifu.pmos.vegetablegardening.views.PlantInfoDialog

class GardenOverviewFragment : Fragment() {
    private lateinit var binding: FragmentGardenOverviewBinding

    private val gardenViewModel: GardenViewModel by activityViewModels()
    private val plantViewModel: PlantViewModel by activityViewModels()

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentGardenOverviewBinding.inflate(inflater, container, false)

        val recyclerView = binding.gardensRecyclerView

        recyclerView.layoutManager = GridLayoutManager(context, 2)
        gardenViewModel.gardens.observe(viewLifecycleOwner, {
            val adapter = GardenOverviewAdapter(it)
            recyclerView.adapter = adapter
        })

        binding.newLocationBtn.setOnClickListener {
            val createIntent = Intent(context, CreateBedActivity::class.java)
            startActivity(createIntent)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireView().findNavController().navigate(GardenOverviewFragmentDirections.showPlantInfo())
        //PlantInfoDialog(plantViewModel.plants.value!![0]).show(childFragmentManager, PlantInfoDialog.TAG)
    }

    private inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val gardenImage: ImageButton = view.findViewById(R.id.garden_image_button)
        val gardenName: TextView = view.findViewById(R.id.garden_name_text)

        init {
            gardenImage.setOnClickListener { /* TODO Do something */ }
        }
    }

    private inner class GardenOverviewAdapter(private val dataSet: List<Bed>): RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_garden, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.gardenName.text = dataSet[position].name

            when (dataSet[position].location) {
                Outdoors -> holder.gardenImage.setImageResource(R.drawable.outdoors_normal)
                Greenhouse -> holder.gardenImage.setImageResource(R.drawable.greenhouse_normal)
            }
        }

        override fun getItemCount(): Int {
            return dataSet.size
        }
    }
}