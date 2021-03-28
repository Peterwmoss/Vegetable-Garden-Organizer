package dk.mifu.pmos.vegetablegardening.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.databinding.FragmentCropRotationGuideDialogBinding

class CropRotationGuideDialogFragment: DialogFragment() {
    private lateinit var binding: FragmentCropRotationGuideDialogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCropRotationGuideDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtonListener()
        val params = dialog!!.window!!.attributes
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.toolbar_crop_rotation)
    }

    private fun setButtonListener(){
        binding.closeButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}