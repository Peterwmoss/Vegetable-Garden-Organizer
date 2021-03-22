package dk.mifu.pmos.vegetablegardening.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.database.AppDatabase
import dk.mifu.pmos.vegetablegardening.database.SeasonRepository
import dk.mifu.pmos.vegetablegardening.databinding.FragmentNewSeasonBinding
import dk.mifu.pmos.vegetablegardening.helpers.KeyboardHelper
import dk.mifu.pmos.vegetablegardening.viewmodels.SeasonViewModel
import kotlinx.coroutines.*

class NewSeasonFragment: Fragment() {
    private lateinit var binding: FragmentNewSeasonBinding
    private val seasonViewModel: SeasonViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewSeasonBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.toolbar_new_season)

        binding.saveSeasonButton.setOnClickListener {
            val year = binding.year.text.toString().toInt()
            handleSave(year)
            seasonViewModel.currentSeason = MutableLiveData(year)
        }

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        KeyboardHelper.hideKeyboard(context)
    }

    private fun handleSave(year: Int) {
        MainScope().launch {
            val exists = async { exists(year) }
            if(!exists.await()){
                saveInDatabase(year)
                findNavController().navigate(NewSeasonFragmentDirections.navigateToBeds())
            } else {
                Toast.makeText(context, getString(R.string.already_exists_season), Toast.LENGTH_LONG).show()
            }
        }
    }

    private suspend fun exists(year: Int): Boolean {
        val dao = AppDatabase.getDatabase(requireContext()).seasonDao()
        val repository = SeasonRepository(dao)
        return withContext(Dispatchers.IO) {
            return@withContext repository.getSeason(year) != null
        }
    }

    private suspend fun saveInDatabase(year: Int){
        val dao = AppDatabase.getDatabase(requireContext()).seasonDao()
        val repository = SeasonRepository(dao)
        withContext(Dispatchers.IO){
            repository.insertSeason(year)
        }
    }
}