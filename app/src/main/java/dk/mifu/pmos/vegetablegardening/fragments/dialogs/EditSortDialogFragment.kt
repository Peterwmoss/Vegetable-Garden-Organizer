package dk.mifu.pmos.vegetablegardening.fragments.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import dk.mifu.pmos.vegetablegardening.databinding.FragmentEditSortDialogBinding
import dk.mifu.pmos.vegetablegardening.helpers.KeyboardHelper
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel

class EditSortDialogFragment : DialogFragment(){
    private lateinit var binding: FragmentEditSortDialogBinding

    private val args: EditSortDialogFragmentArgs by navArgs()

    private val bedViewModel: BedViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentEditSortDialogBinding.inflate(inflater, container, false)

        binding.cancelButton.setOnClickListener {
            dialog?.cancel()
        }

        binding.saveSortButton.setOnClickListener {
            val plantWithSort = args.myPlant
            plantWithSort.sort = binding.editSortEditText.text.toString()
            bedViewModel.plants?.put(args.coordinate, plantWithSort)
            dismiss()
        }

        binding.editSortEditText.requestFocus()
        KeyboardHelper.showKeyboard(context)

        return binding.root
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        KeyboardHelper.hideKeyboard(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val params = dialog!!.window!!.attributes

        params.width = WindowManager.LayoutParams.MATCH_PARENT

        dialog!!.window!!.attributes = params
    }
}