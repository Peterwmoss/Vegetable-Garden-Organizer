package dk.mifu.pmos.vegetablegardening.fragments.dialogs

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.database.BedRepository
import dk.mifu.pmos.vegetablegardening.database.AppDatabase
import dk.mifu.pmos.vegetablegardening.helpers.KeyboardHelper.Companion.hideKeyboard
import dk.mifu.pmos.vegetablegardening.helpers.KeyboardHelper.Companion.showKeyboard
import dk.mifu.pmos.vegetablegardening.models.Bed
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel
import dk.mifu.pmos.vegetablegardening.viewmodels.SeasonViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SaveBedDialogFragment : DialogFragment() {
    private val bedViewModel: BedViewModel by activityViewModels()
    private val seasonViewModel: SeasonViewModel by activityViewModels()

    companion object {
        const val TAG = "SaveBedDialog"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val editText = EditText(requireContext())
        editText.hint = getString(R.string.name)
        editText.requestFocus()
        showKeyboard(context)

        val builder = AlertDialog.Builder(requireActivity())
        return builder.setTitle(getString(R.string.name_your_bed))
                .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton(getString(R.string.save)) { _, _ ->
                    val text = editText.text.toString()
                    if (text.isEmpty()) {
                        Toast.makeText(requireActivity(), getString(R.string.no_bed_name_given), Toast.LENGTH_SHORT).show()
                    } else {
                        saveInDatabase(editText)
                        findNavController().navigate(SaveBedDialogFragmentDirections.saveBedAction())
                    }
                }
                .setView(editText)
                .create()
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard(context)
    }

    private fun saveInDatabase(editText: EditText) {
        bedViewModel.name = editText.text.toString()
        run {
            MainScope().launch(Dispatchers.IO) {
                val dao = AppDatabase.getDatabase(requireContext()).bedDao()
                val repository = BedRepository(dao)
                repository.insertBed(Bed(bedViewModel.name!!, seasonViewModel.currentSeason.value!!, bedViewModel.bedLocation!!, bedViewModel.plants!!.toMap(), bedViewModel.columns, bedViewModel.rows))
            }
        }
    }
}