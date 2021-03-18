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
import dk.mifu.pmos.vegetablegardening.databinding.FragmentSetGerminationDialogBinding
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel

class SetGerminationDialogFragment: DialogFragment() {
    private lateinit var binding: FragmentSetGerminationDialogBinding
    private val args: SetGerminationDialogFragmentArgs by navArgs()
    private val bedViewModel: BedViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSetGerminationDialogBinding.inflate(inflater, container, false)

        binding.cancelGerminationButton.setOnClickListener {
            dialog?.cancel()
        }

        binding.saveGerminationButton.setOnClickListener {
            val plant = args.myPlant
            plant.germinated = binding.germinationSwitch.isChecked
            bedViewModel.plants?.put(args.coordinate, plant)
            dismiss()
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

        params.width = WindowManager.LayoutParams.MATCH_PARENT

        dialog!!.window!!.attributes = params
    }
}