package dk.mifu.pmos.vegetablegardening.viewgarden

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.databinding.FragmentGardenOverviewBinding
import dk.mifu.pmos.vegetablegardening.enums.Location.*
import dk.mifu.pmos.vegetablegardening.models.Garden
import dk.mifu.pmos.vegetablegardening.viewmodels.GardenViewModel

class GardenOverviewFragment : Fragment() {
    private lateinit var binding: FragmentGardenOverviewBinding

    private val gardenViewModel: GardenViewModel by activityViewModels()

    private lateinit var adapter: GardenOverviewAdapter

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentGardenOverviewBinding.inflate(inflater, container, false)

        val recyclerView = binding.gardensRecyclerView

        recyclerView.layoutManager = GridLayoutManager(context, 2)
        adapter = GardenOverviewAdapter(gardenViewModel.gardens)
        recyclerView.adapter = adapter

        return binding.root
    }

    private inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val gardenImage: ImageButton = view.findViewById(R.id.garden_image_button)
        val gardenName: TextView = view.findViewById(R.id.garden_name_text)

        init {
            gardenImage.setOnClickListener { /* TODO Do something */ }
        }
    }

    private inner class GardenOverviewAdapter(private val dataSet: MutableList<Garden>): RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_garden, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.gardenName.text = dataSet[position].name

            when (dataSet[position].location) {
                Outdoors -> holder.gardenImage.setImageResource(R.drawable.outdoors_normal)
                Indoors -> holder.gardenImage.setImageResource(R.drawable.indoors_normal)
                Greenhouse -> holder.gardenImage.setImageResource(R.drawable.greenhouse_normal)
            }
        }

        override fun getItemCount(): Int {
            return dataSet.size
        }
    }
}