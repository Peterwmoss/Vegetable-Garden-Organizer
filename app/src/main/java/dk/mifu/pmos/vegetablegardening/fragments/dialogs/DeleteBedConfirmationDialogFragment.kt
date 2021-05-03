package dk.mifu.pmos.vegetablegardening.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.skydoves.balloon.createBalloon
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.database.AppDatabase
import dk.mifu.pmos.vegetablegardening.database.BedRepository
import dk.mifu.pmos.vegetablegardening.databinding.FragmentDeleteBedConfirmationDialogBinding
import dk.mifu.pmos.vegetablegardening.helpers.predicates.HigherOrderPredicate
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel
import dk.mifu.pmos.vegetablegardening.viewmodels.SeasonViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeleteBedConfirmationDialogFragment : DialogFragment() {
    private lateinit var binding : FragmentDeleteBedConfirmationDialogBinding

    private val bedViewModel: BedViewModel by activityViewModels()
    private val seasonViewModel: SeasonViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDeleteBedConfirmationDialogBinding.inflate(inflater, container, false)

        binding.cancelButton.setOnClickListener {
            dialog?.cancel()
        }

        binding.deleteBedButton.setOnClickListener {
            deleteCurrentBed()
            Toast.makeText(requireContext(), getString(R.string.toast_bed_deleted), Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.gardenOverviewFragment)
        }

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.title = bedViewModel.name
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val params = dialog!!.window!!.attributes

        params.width = WindowManager.LayoutParams.WRAP_CONTENT

        dialog!!.window!!.attributes = params
    }

    private fun deleteCurrentBed() {
        MainScope().launch(Dispatchers.IO) {
            val dao = AppDatabase.getDatabase(requireContext()).bedDao()
            val repository = BedRepository(dao)

            updateOrders(repository)

            repository.deleteBed(bedViewModel.name!!, seasonViewModel.currentSeason.value!!)

        }
    }

    private suspend fun updateOrders(repository: BedRepository) {
        return withContext(Dispatchers.IO) {
            val beds = repository.findBedsWithSeasonAndLocation(seasonViewModel.currentSeason.value!!, bedViewModel.bedLocation!!)
            val currentBed = repository.findBedByPrimaryKeys(bedViewModel.name!!, seasonViewModel.currentSeason.value!!)
            val filteredBeds = beds.filter(HigherOrderPredicate(currentBed!!))
            filteredBeds.forEach {
                it.order--
                repository.updateBed(it)
            }
        }
    }

}