package dk.mifu.pmos.vegetablegardening.helpers.callbacks

import android.widget.Button
import android.widget.TextView
import androidx.databinding.ObservableMap
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.models.Coordinate
import dk.mifu.pmos.vegetablegardening.models.MyPlant

class UpdateSortInViewCallback() : BedPlantsChangedCallback() {
    private lateinit var coordinate: Coordinate
    private lateinit var header: String
    private lateinit var data: String
    private lateinit var textViewFunction: (String?, String?) -> TextView
    private var textView: TextView? = null
    private lateinit var button: Button

    constructor(coordinate: Coordinate, header: String, data: String, textViewFunction: (String?, String?) -> TextView, button: Button) : this() {
        this.coordinate = coordinate
        this.header = header
        this.data = data
        this.textViewFunction = textViewFunction
        this.button = button
    }

    constructor(coordinate: Coordinate, textView: TextView, button: Button) : this() {
        this.coordinate = coordinate
        this.textView = textView
        this.button = button
    }

    override fun onMapChanged(sender: ObservableMap<Coordinate, MyPlant>?, key: Coordinate?) {
        if (key != coordinate)
            return

        val value = sender!![key]!!

        if (textView == null) {
            textView = textViewFunction(header, data)
        }
        textView!!.text = value.sort

        if (value.sort.isBlank())
            button.setText(R.string.add_sort_text)
        else
            button.setText(R.string.edit_sort_text)
    }
}