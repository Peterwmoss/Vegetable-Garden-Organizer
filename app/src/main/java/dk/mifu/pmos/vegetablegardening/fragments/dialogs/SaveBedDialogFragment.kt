package dk.mifu.pmos.vegetablegardening.fragments.dialogs

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.database.GardenRepository
import dk.mifu.pmos.vegetablegardening.database.AppDatabase
import dk.mifu.pmos.vegetablegardening.models.Bed
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SaveBedDialogFragment : DialogFragment() {
    private val bed: BedViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val editText = EditText(requireContext())
        editText.hint = getString(R.string.name_hint_text)
        editText.requestFocus()

        val builder = AlertDialog.Builder(requireActivity())
        return builder.setTitle(getString(R.string.name_bed_text))
                .setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }
                .setPositiveButton(getString(R.string.save)) { _, _ ->
                    val text = editText.text.toString()
                    if (text.isEmpty()) {
                        Toast.makeText(requireActivity(), getString(R.string.no_name_given_text), Toast.LENGTH_SHORT).show()
                    }
                    else {
                        bed.name = editText.text.toString()
                        run {
                            MainScope().launch(Dispatchers.IO) {
                                val dao = AppDatabase.getDatabase(requireContext()).bedDao()
                                val repository = GardenRepository(dao)
                                repository.insertBed(Bed(bed.name!!, bed.bedLocation!!, bed.plants!!.toMutableMap()))
                            }
                        }
                        requireActivity().finish()
                    }
                }
                .setView(editText)
                .create()
    }

    companion object {
        const val TAG = "SaveBedDialog"
    }
}