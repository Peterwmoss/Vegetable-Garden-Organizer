package dk.mifu.pmos.vegetablegardening.fragments.viewgarden

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import dk.mifu.pmos.vegetablegardening.databinding.FragmentEmptyTileDialogBinding

class EmptyTileDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentEmptyTileDialogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEmptyTileDialogBinding.inflate(inflater, container, false)
        return binding.root
    }
}