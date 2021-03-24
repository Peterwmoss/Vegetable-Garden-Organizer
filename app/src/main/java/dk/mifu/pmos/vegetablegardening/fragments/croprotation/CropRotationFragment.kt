package dk.mifu.pmos.vegetablegardening.fragments.croprotation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.databinding.FragmentCropRotationBinding
import dk.mifu.pmos.vegetablegardening.viewmodels.PlantViewModel

class CropRotationFragment: Fragment() {
    private lateinit var binding: FragmentCropRotationBinding
    private lateinit var adapter: CropRotationAdapter
    private val plantViewModel: PlantViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentCropRotationBinding.inflate(inflater, container, false)

        val recyclerView = binding.cropRotationRecyclerView

        recyclerView.layoutManager = GridLayoutManager(context, 1)

        adapter = CropRotationAdapter()
        recyclerView.adapter = adapter


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.crop_rotation)
    }

    private inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    private inner class CropRotationAdapter(): RecyclerView.Adapter<CropRotationFragment.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CropRotationFragment.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_bed, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: CropRotationFragment.ViewHolder, position: Int) {

        }

        override fun getItemCount(): Int {
            return 0
        }
    }
}