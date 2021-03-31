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
import dk.mifu.pmos.vegetablegardening.databinding.FragmentEditNotesDialogBinding
import dk.mifu.pmos.vegetablegardening.helpers.KeyboardHelper
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel

class EditNotesDialogFragment: DialogFragment() {
    private lateinit var binding: FragmentEditNotesDialogBinding

    private val args: EditNotesDialogFragmentArgs by navArgs()

    private val bedViewModel: BedViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentEditNotesDialogBinding.inflate(inflater, container, false)

        binding.cancelButton.setOnClickListener {
            dialog?.cancel()
        }

        binding.saveNotesButton.setOnClickListener {
            val plantWithNotes = args.myPlant
            plantWithNotes.notes = binding.notesEditText.text.toString()
            bedViewModel.plants?.put(args.coordinate, plantWithNotes)
            dismiss()
        }

        if (args.myPlant.notes != null)
            binding.notesEditText.setText(args.myPlant.notes)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        KeyboardHelper.showKeyboard(context, binding.notesEditText)
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.title = bedViewModel.name
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val params = dialog!!.window!!.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params
    }
}