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
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.database.AppDatabase
import dk.mifu.pmos.vegetablegardening.database.BedRepository
import dk.mifu.pmos.vegetablegardening.databinding.FragmentSaveBedDialogBinding
import dk.mifu.pmos.vegetablegardening.helpers.KeyboardHelper.Companion.hideKeyboard
import dk.mifu.pmos.vegetablegardening.helpers.KeyboardHelper.Companion.showKeyboard
import dk.mifu.pmos.vegetablegardening.models.Bed
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel
import dk.mifu.pmos.vegetablegardening.viewmodels.SeasonViewModel
import kotlinx.coroutines.*

class SaveBedDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentSaveBedDialogBinding

    private val bedViewModel: BedViewModel by activityViewModels()
    private val seasonViewModel: SeasonViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSaveBedDialogBinding.inflate(inflater, container, false)

        if (!bedViewModel.name.isNullOrBlank()) {
            binding.bedNameEditText.setText(bedViewModel.name)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

        binding.cancelSaveBedButton.setOnClickListener {
            dialog?.cancel()
        }

        binding.saveBedButton.setOnClickListener {
            val name = binding.bedNameEditText.text.toString()
            if (bedViewModel.name.isNullOrBlank()) {
                MainScope().launch {
                    val exists = async { exists(name) }
                    if (!exists.await()) {
                        saveInDatabase(name)
                        val navOptions = NavOptions.Builder().setPopUpTo(R.id.areaOverviewFragment, true).build()
                        val bundle = Bundle()
                        bundle.putSerializable("Location", bedViewModel.bedLocation!!)
                        findNavController().navigate(R.id.areaOverviewFragment, bundle, navOptions)
                    } else
                        Toast.makeText(requireContext(), getString(R.string.guide_bed_already_exists), Toast.LENGTH_LONG).show()
                }
            } else {
                MainScope().launch(Dispatchers.IO) {
                    val dao = AppDatabase.getDatabase(requireContext()).bedDao()
                    val repository = BedRepository(dao)
                    if (name != bedViewModel.name) {
                        deleteOldFromDatabase()
                        saveInDatabase(name)
                    } else {
                        repository.updateBed(Bed(bedViewModel.name!!, seasonViewModel.currentSeason.value!!, bedViewModel.bedLocation!!, bedViewModel.plants!!.toMap(), bedViewModel.columns, bedViewModel.rows, bedViewModel.order))
                    }
                }
                val navOptions = NavOptions.Builder().setPopUpTo(R.id.bedOverviewFragment, true).build()
                findNavController().navigate(R.id.bedOverviewFragment, null, navOptions)
            }
        }

        binding.bedNameEditText.requestFocus()
    }

    override fun onStart() {
        super.onStart()
        showKeyboard(context)
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard(context)
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.toolbar_create_grid)
    }

    private suspend fun exists(name: String) : Boolean {
        return withContext(Dispatchers.IO) {
            val dao = AppDatabase.getDatabase(requireContext()).bedDao()
            val repository = BedRepository(dao)
            val bed = repository.findBedByPrimaryKeys(name, seasonViewModel.currentSeason.value!!)
            return@withContext bed != null
        }
    }

    private suspend fun saveInDatabase(name: String) {
        bedViewModel.name = name
        withContext(Dispatchers.IO) {
            val dao = AppDatabase.getDatabase(requireContext()).bedDao()
            val repository = BedRepository(dao)
            val order = repository.findOrder(bedViewModel.bedLocation!!, seasonViewModel.currentSeason.value!!)
            if(order != null)
                bedViewModel.order = order+1
            repository.insertBed(
                    Bed(bedViewModel.name!!,
                        seasonViewModel.currentSeason.value!!,
                        bedViewModel.bedLocation!!,
                        bedViewModel.plants!!.toMap(),
                        bedViewModel.columns,
                        bedViewModel.rows,
                        bedViewModel.order))
        }
    }

    private suspend fun deleteOldFromDatabase() {
        withContext(Dispatchers.IO) {
            val dao = AppDatabase.getDatabase(requireContext()).bedDao()
            val repository = BedRepository(dao)
            repository.deleteBed(bedViewModel.name!!)
        }
    }
}