package dk.mifu.pmos.vegetablegardening.creategarden

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.ObservableArrayMap
import androidx.databinding.ObservableMap
import androidx.fragment.app.activityViewModels
import dk.mifu.pmos.vegetablegardening.databinding.FragmentCreateGridBinding
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.Plant
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel
import dk.mifu.pmos.vegetablegardening.views.GridTile
import dk.mifu.pmos.vegetablegardening.views.SaveBedDialog

class CreateGridFragment : CreateGridNavigation() {
    private lateinit var binding: FragmentCreateGridBinding

    private val bed: BedViewModel by activityViewModels()

    private var height = 0
    private var width = 0
    private var tileSideLength = 0

    private val START = ConstraintSet.START
    private val END = ConstraintSet.END
    private val TOP = ConstraintSet.TOP
    private val BOTTOM = ConstraintSet.BOTTOM

    //Initial number of grid tiles
    private var columns = 1
    private var rows = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentCreateGridBinding.inflate(inflater, container, false)

        // Initialize current bed
        bed.plants = ObservableArrayMap()
        bed.tileIds = HashMap()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        width = Resources.getSystem().displayMetrics.widthPixels
        height = Resources.getSystem().displayMetrics.heightPixels
        tileSideLength = width/4

        insertInitialGridTiles()
        setListeners()
        setSaveBedListener()

        bed.plants?.addOnMapChangedCallback(Callback())
    }

    private fun setSaveBedListener() {
        binding.saveGardenButton.setOnClickListener {

            val dialog = SaveBedDialog()
            dialog.show(childFragmentManager, SaveBedDialog.TAG)
        }
    }

    private inner class Callback : ObservableMap.OnMapChangedCallback<ObservableMap<Coordinate, Plant>, Coordinate, Plant>() {
        override fun onMapChanged(sender: ObservableMap<Coordinate, Plant>?, key: Coordinate?) {
            val id = bed.tileIds?.get(key)!!
            requireView().findViewById<Button>(id).text = bed.plants?.get(key)?.name
        }
    }

    private fun gridTileListener(coordinate: Coordinate): View.OnClickListener {
        return View.OnClickListener {
            navigateToChoosePlantFragment(coordinate)
        }
    }

    private fun insertInitialGridTiles(){
        val coordinate1 = Coordinate(0,0)
        val initialTile1 = GridTile(requireContext(), gridTileListener(coordinate1), binding, tileSideLength)
        binding.parentLayout.addView(initialTile1)
        bed.tileIds?.set(coordinate1, initialTile1.id)
        initialTile1.snapToGrid(null,null,false)

        val coordinate2 = Coordinate(0,1)
        val initialTile2 = GridTile(requireContext(), gridTileListener(coordinate2), binding, tileSideLength)
        binding.parentLayout.addView(initialTile2)
        bed.tileIds?.set(coordinate2, initialTile2.id)
        initialTile2.snapToGrid(null,initialTile1.id,false)

        rows++

        addTiles(column = true)
    }

    private fun setListeners() {
        binding.addColumnButton.setOnClickListener{
            addTiles(column = true)
            if(columns==4){
                binding.addColumnButton.visibility = View.GONE
                changePlacementOfRemoveButton(
                    full = true,
                    column = true,
                    buttonId = binding.removeColumnButton.id
                )
            }
            if(binding.removeColumnButton.visibility == View.GONE){
                binding.removeColumnButton.visibility = View.VISIBLE
            }
        }

        binding.addRowButton.setOnClickListener{
            addTiles(column = false)
            if(height-(tileSideLength*rows) < tileSideLength){ //If there isn't enough room for a whole row more
                binding.addRowButton.visibility = View.GONE
                changePlacementOfRemoveButton(
                    full = true,
                    column = false,
                    buttonId = binding.removeRowButton.id
                )
            }
            if(binding.removeRowButton.visibility == View.GONE){
                binding.removeRowButton.visibility = View.VISIBLE
            }
        }

        binding.removeColumnButton.setOnClickListener{
            removeTiles(column = true)
            if(binding.addColumnButton.visibility == View.GONE){
                binding.addColumnButton.visibility = View.VISIBLE
                changePlacementOfRemoveButton(
                    full = false,
                    column = true,
                    buttonId = binding.removeColumnButton.id
                )
            }
            if(columns==2){
                binding.removeColumnButton.visibility = View.GONE
            }
        }

        binding.removeRowButton.setOnClickListener{
            removeTiles(column = false)
            if(binding.addRowButton.visibility == View.GONE){
                binding.addRowButton.visibility = View.VISIBLE
                changePlacementOfRemoveButton(
                    full = false,
                    column = false,
                    buttonId = binding.removeRowButton.id
                )
            }

            if(rows==2){
                binding.removeRowButton.visibility = View.GONE
            }
        }
    }

    private fun removeTiles(column: Boolean) {
        for (i in 0 until if (column) rows else columns) {
            val coordinate = if (column) Coordinate(columns-1, i) else Coordinate(i, rows-1)
            val gridTileId = bed.tileIds?.get(coordinate)
            val gridTile = requireView().findViewById<Button>(gridTileId!!)
            binding.parentLayout.removeView(gridTile)

            bed.tileIds?.remove(coordinate)
        }
        if (column) columns-- else rows--
        snapButtonsToRestOfGrid(column)
    }

    private fun addTiles(column: Boolean) {
        for (i in 0 until if (column) rows else columns) {
            val coordinate = if (column) Coordinate(columns, i) else Coordinate(i, rows)
            val gridTile = GridTile(requireContext(), gridTileListener(coordinate), binding, tileSideLength)
            binding.parentLayout.addView(gridTile)

            bed.tileIds?.set(coordinate, gridTile.id) //Update garden with new tile

            val prevTileId = bed.tileIds?.get(if (column) Coordinate(columns-1, i) else Coordinate(i-1, rows))
            val upperTileId = bed.tileIds?.get(if (column) Coordinate(columns, i - 1) else Coordinate(i, rows - 1))
            gridTile.snapToGrid(prevTileId, upperTileId, column)
        }
        if (column) columns++ else rows++
        snapButtonsToRestOfGrid(column)
    }

    private fun snapButtonsToRestOfGrid(column: Boolean) {
        val tileId = bed.tileIds?.get(if (column) Coordinate(columns-1, 0) else Coordinate(0, rows-1))
        val constraintSet = ConstraintSet()
        constraintSet.apply {
            clone(binding.parentLayout)
            if(column){
                connect(binding.addColumnButton.id, START, tileId!!, END)
                connect(binding.addColumnButton.id, TOP, binding.parentLayout.id, TOP)
            } else {
                connect(binding.addRowButton.id, TOP, tileId!!, BOTTOM)
                connect(binding.addRowButton.id, START, binding.parentLayout.id, START)
            }
            applyTo(binding.parentLayout)
        }
    }

    private fun changePlacementOfRemoveButton(full: Boolean, column: Boolean, buttonId: Int){
        val furthestTileId = bed.tileIds?.get(if (column) Coordinate(columns-1,0) else Coordinate(0, rows-1))
        val constraintSet = ConstraintSet()
        constraintSet.apply {
            clone(binding.parentLayout)
            if(full){
                connect(buttonId, START, if(column) furthestTileId!! else binding.parentLayout.id, START)
                connect(buttonId, TOP, if(column) binding.parentLayout.id else furthestTileId!!, TOP)
            } else {
                if(column) {
                    connect(buttonId, START, binding.addColumnButton.id, START)
                    connect(buttonId, TOP, binding.addColumnButton.id, BOTTOM)
                } else {
                    connect(buttonId, START, binding.addRowButton.id, END)
                    connect(buttonId, TOP, binding.addRowButton.id, TOP)
                }
            }

            applyTo(binding.parentLayout)
        }
    }
}
