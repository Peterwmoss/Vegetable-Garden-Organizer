package dk.mifu.pmos.vegetablegardening.fragments.viewgarden

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Constraints
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.activities.CreateBedActivity
import dk.mifu.pmos.vegetablegardening.database.GardenDao
import dk.mifu.pmos.vegetablegardening.database.GardenRepository
import dk.mifu.pmos.vegetablegardening.database.AppDatabase
import dk.mifu.pmos.vegetablegardening.databinding.FragmentGardenOverviewBinding
import dk.mifu.pmos.vegetablegardening.enums.BedLocation.Greenhouse
import dk.mifu.pmos.vegetablegardening.enums.BedLocation.Outdoors
import dk.mifu.pmos.vegetablegardening.models.Bed
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel

class GardenOverviewFragment : Fragment() {
    private lateinit var binding: FragmentGardenOverviewBinding

    private val bedViewModel: BedViewModel by activityViewModels()
    private var gardenDb: GardenDao? = null
    private var repository: GardenRepository? = null

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentGardenOverviewBinding.inflate(inflater, container, false)
        gardenDb = AppDatabase.getDatabase(requireContext()).gardenDao()
        repository = GardenRepository(gardenDb!!)

        val recyclerView = binding.gardensRecyclerView

        recyclerView.layoutManager = GridLayoutManager(context, 2)
        repository?.getAllBeds()?.observe(viewLifecycleOwner, {
            val adapter = GardenOverviewAdapter(it)
            recyclerView.adapter = adapter
            binding.emptyGardenTextView.visibility = if (adapter.itemCount == 0) View.VISIBLE else View.GONE
        })

        binding.newLocationBtn.setOnClickListener {
            navigateToCreateBedActivity()
        }

        (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.bed_text)

        return binding.root
    }

    private inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bedImage: ImageButton = view.findViewById(R.id.bed_image_button)
        val bedName: TextView = view.findViewById(R.id.bed_name_text)
        var bed: Bed? = null

        init {
            bedImage.setOnClickListener {
                bedViewModel.setBed(bed!!)
                navigateToBedOverviewFragment()
            }
        }
    }

    private inner class GardenOverviewAdapter(private val dataSet: List<Bed>): RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_garden, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bedName.text = dataSet[position].name
            holder.bed = dataSet[position]

            when (dataSet[position].bedLocation) {
                Outdoors -> holder.bedImage.setImageResource(R.drawable.outdoors_normal)
                Greenhouse -> holder.bedImage.setImageResource(R.drawable.greenhouse_normal)
            }

            holder.bedImage.layoutParams = Constraints.LayoutParams(
                    Constraints.LayoutParams.WRAP_CONTENT,
                    Constraints.LayoutParams.WRAP_CONTENT
            )
        }

        override fun getItemCount(): Int {
            return dataSet.size
        }
    }

    private fun navigateToCreateBedActivity() {
        val createIntent = Intent(context, CreateBedActivity::class.java)
        startActivity(createIntent)
    }

    private fun navigateToBedOverviewFragment() {
        requireView().findNavController().navigate(GardenOverviewFragmentDirections.seeBedAction())
    }
}