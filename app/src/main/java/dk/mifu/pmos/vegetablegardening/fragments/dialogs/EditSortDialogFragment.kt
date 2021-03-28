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
import dk.mifu.pmos.vegetablegardening.databinding.FragmentEditSortDialogBinding
import dk.mifu.pmos.vegetablegardening.helpers.KeyboardHelper.Companion.hideKeyboard
import dk.mifu.pmos.vegetablegardening.helpers.KeyboardHelper.Companion.showKeyboard
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel

class EditSortDialogFragment : DialogFragment() {
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

        if (args.myPlant.sort != null)
            binding.editSortEditText.setText(args.myPlant.sort)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        showKeyboard(context, binding.editSortEditText)
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.title = bedViewModel.name
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard(context, binding.editSortEditText)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val params = dialog!!.window!!.attributes

        params.width = WindowManager.LayoutParams.MATCH_PARENT

        dialog!!.window!!.attributes = params
    }
}